package com.dimple.project.file.image;

import com.dimple.common.utils.file.FileUploadUtils;
import com.dimple.common.vo.FileForm;
import com.dimple.framework.aspectj.lang.annotation.Log;
import com.dimple.framework.aspectj.lang.enums.BusinessType;
import com.dimple.framework.config.SystemConfig;
import com.dimple.framework.web.controller.BaseController;
import com.dimple.framework.web.domain.AjaxResult;
import com.dimple.project.common.domain.FileItemInfo;
import com.dimple.project.common.service.FileService;
import com.qiniu.common.QiniuException;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Objects;

/**
 * @className: ImageController
 * @description: 图片文件处理Controller
 * @auther: Dimple
 * @date: 07/24/19
 * @version: 1.0
 */
@Controller
@RequestMapping("/file/image")
@Api
public class ImageController extends BaseController {

    @Autowired
    FileService fileService;

    @GetMapping("/add")
    public String add(Integer serverType, Model model) {
        model.addAttribute("serverType", serverType == null ? FileItemInfo.ServerType.LOCAL.getServerType() : FileItemInfo.ServerType.QI_NIU_YUN.getServerType());
        return "file/image/add";
    }

    @GetMapping("/image")
    @RequiresPermissions("file:image:view")
    public String image() {
        return "file/image/image";
    }
    
    private AjaxResult uploadFile(MultipartFile file, Integer serverType) throws IOException{
      Objects.requireNonNull(serverType, "上传服务器未选定，请重试！");
      //检查文件大小
      FileUploadUtils.assertAllowed(file);
      String path = null;
      if (FileItemInfo.ServerType.QI_NIU_YUN.getServerType() == serverType) {
          path = fileService.insertQiNiuYunImageFile(file);
      } else if (FileItemInfo.ServerType.LOCAL.getServerType() == serverType) {
          path = fileService.insertLocalImageFile(new FileForm(SystemConfig.getImagePath(), file));
      }
      return AjaxResult.success().put("path", path);
    }

    @PostMapping("/add")
    @Log(title = "系统图片", businessType = BusinessType.UPLOAD)
    @ResponseBody
    @RequiresPermissions("file:image:upload")
    public AjaxResult add(@RequestParam("file") MultipartFile file, Integer serverType) throws IOException {
        return uploadFile(file, serverType);
    }
    
    
    @PostMapping("/upload")
    @Log(title = "系统图片", businessType = BusinessType.UPLOAD)
    @ResponseBody
    @RequiresPermissions("file:image:upload")
    public AjaxResult upload(@RequestParam("file_data") MultipartFile file_data, Integer serverType) throws IOException {
      return uploadFile(file_data, serverType);
    }
    
    @PostMapping("/uploadWithSize")
    @Log(title = "系统图片", businessType = BusinessType.UPLOAD)
    @ResponseBody
    @RequiresPermissions("file:image:upload")
    public AjaxResult uploadWithSize(@RequestParam("file") MultipartFile file, Integer serverType,Integer width , Integer height) throws IOException {
      return AjaxResult.success().put("path", fileService.insertLocalImageFile(new FileForm(SystemConfig.getImagePath(), file, width, height)));
    }

    @GetMapping("/list/{serverType}")
    @ResponseBody
    public AjaxResult list(FileItemInfo fileItemInfo, @PathVariable Integer serverType) {
        fileItemInfo.setServerType(serverType);
        List<FileItemInfo> fileItemInfoList = fileService.selectFileItemImageList(fileItemInfo);
        return AjaxResult.success().put("list", fileItemInfoList);
    }

    @PutMapping("/sync")
    @ResponseBody
    @Log(title = "系统图片", businessType = BusinessType.OTHER)
    @RequiresPermissions("file:image:sync")
    public AjaxResult sync(Integer serverType) throws QiniuException {
        Objects.requireNonNull(serverType, "未选定刷新的服务器，请重试！");
        int i = 0;
        if (FileItemInfo.ServerType.QI_NIU_YUN.getServerType() == serverType) {
            i = fileService.syncQiNiuYunImage();
        } else if (FileItemInfo.ServerType.LOCAL.getServerType() == serverType) {
            i = fileService.syncLocalImage();
        }
        return toAjax(i);
    }

    @DeleteMapping()
    @ResponseBody
    @RequiresPermissions("file:image:delete")
    @Log(title = "系统图片", businessType = BusinessType.DELETE)
    public AjaxResult deleteImage(String name, Integer serverType) throws QiniuException {
        Objects.requireNonNull(serverType, "未选定服务器，请重试！");
        int i = 0;
        if (FileItemInfo.ServerType.QI_NIU_YUN.getServerType() == serverType) {
            i = fileService.deleteQiNiuYunImageFile(name);
        } else if (FileItemInfo.ServerType.LOCAL.getServerType() == serverType) {
            i = fileService.deleteLocalImageFile(name);
        }
        return toAjax(i);
    }

    @RequestMapping(value = "/toViewImg")
    public String toViewImg(Model model, HttpServletRequest request, String pk, String imgurl) {
        try {
            imgurl= URLDecoder.decode(imgurl,"UTF-8");
            imgurl=imgurl.replaceAll( "\\\\","/");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        model.addAttribute("imgurl", imgurl);
        return "file/image/img_view";
    }
}
