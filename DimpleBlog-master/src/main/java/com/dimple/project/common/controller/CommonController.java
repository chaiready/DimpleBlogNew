package com.dimple.project.common.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimple.common.constant.Constants;
import com.dimple.common.utils.security.ShiroUtils;
import com.dimple.project.common.domain.FileItemInfo;
import com.dimple.project.common.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.dimple.common.constant.CommonConstant;
import com.dimple.common.utils.QrCodeUtils;
import com.dimple.common.utils.StringUtils;
import com.dimple.common.utils.file.FileUploadUtils;
import com.dimple.common.utils.file.FileUtils;
import com.dimple.framework.config.ServerConfig;
import com.dimple.framework.config.SystemConfig;
import com.dimple.framework.web.domain.AjaxResult;

import lombok.extern.slf4j.Slf4j;

/**
 * @className: CommonController
 * @description: 通用请求处理
 * @auther: Dimple
 * @Date: 2019/3/13
 * @Version: 1.1
 */
@Controller
@Slf4j
public class CommonController {

    @Autowired
    private ServerConfig serverConfig;
    @Autowired
    private FileService fileService;

    @GetMapping("/error/{type}")
    public String error(@PathVariable String type) {
        return "error/" + type;
    }

    /**
     * 通用下载请求
     *
     * @param fileName 文件名称
     * @param delete   是否删除
     */
    @GetMapping("common/download")
    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response, HttpServletRequest request) {
        try {
            if (!FileUtils.isValidFilename(fileName)) {
                throw new Exception(StringUtils.format(" 文件名称({})非法，不允许下载。 ", fileName));
            }
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String filePath = SystemConfig.getDownloadPath() + fileName;

            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition",
                    "attachment;fileName=" + setFileDownloadHeader(request, realFileName));
            FileUtils.writeBytes(filePath, response.getOutputStream());
            if (delete) {
                FileUtils.deleteFile(filePath);
            }
        } catch (Exception e) {
            log.error("下载文件失败", e);
        }
    }

    /**
     * 通用上传请求
     */
    @PostMapping("/common/upload")
    @ResponseBody
    public AjaxResult uploadFile(MultipartFile file) throws Exception {
        try {
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(file).getName();
            String url = serverConfig.getUrl() + SystemConfig.getUploadPath() + fileName;
            AjaxResult ajax = AjaxResult.success();
            ajax.put("fileName", fileName);
            ajax.put("url", url);
            return ajax;
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @PostMapping("/common/delFile")
    @ResponseBody
    public AjaxResult delFile(Long fileId) throws Exception {
        FileItemInfo fileInfo = fileService.getById(fileId);
        if(fileInfo == null){
            return AjaxResult.error("文件不存在");
        }
        if(fileInfo.getCreateBy()==null||!fileInfo.getCreateBy().equals(ShiroUtils.getLoginName())){
            return AjaxResult.error("文件不属于当前用户");
        }
        fileInfo.setStatus(Constants.STATUS_DEL);
        fileService.updateById(fileInfo);
        return AjaxResult.success();
    }

    public String setFileDownloadHeader(HttpServletRequest request, String fileName) throws UnsupportedEncodingException {
        final String agent = request.getHeader("USER-AGENT");
        String filename = fileName;
        if (agent.contains(CommonConstant.MSIE)) {
            // IE浏览器
            filename = URLEncoder.encode(filename, "utf-8");
            filename = filename.replace("+", " ");
        } else if (agent.contains(CommonConstant.Firefox)) {
            // 火狐浏览器
            filename = new String(fileName.getBytes(), "ISO8859-1");
        } else if (agent.contains(CommonConstant.Chrome)) {
            // google浏览器
            filename = URLEncoder.encode(filename, "utf-8");
        } else {
            // 其它浏览器
            filename = URLEncoder.encode(filename, "utf-8");
        }
        return filename;
    }
    
    /**
     * 二维码
     * @param request
     * @param response
     */
    @RequestMapping("/qrcode")
    public void qrcode( HttpServletRequest request, HttpServletResponse response) {
        StringBuffer url = request.getRequestURL();
        // 域名
        String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).append("/").toString();

        // 再加上请求链接
        String requestUrl = tempContextUrl;
        System.out.println(requestUrl);
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            QrCodeUtils.encode(requestUrl, "/static/img/png.png", os, true);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }


    
}
