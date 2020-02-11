package com.dimple.framework.web.controller;

import java.beans.PropertyEditorSupport;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.dimple.common.utils.DateUtils;
import com.dimple.common.utils.StringUtils;
import com.dimple.common.utils.security.ShiroUtils;
import com.dimple.common.utils.sql.SqlUtil;
import com.dimple.framework.web.domain.AjaxResult;
import com.dimple.framework.web.page.PageDomain;
import com.dimple.framework.web.page.TableDataInfo;
import com.dimple.framework.web.page.TableSupport;
import com.dimple.project.system.user.domain.User;

/**
 * @className: BaseController
 * @description: web层通用数据处理
 * @auther: Dimple
 * @Date: 2019/3/13
 * @Version: 1.1
 */
public class BaseController {
    /**
     * 将前台传递过来的日期格式的字符串，自动转化为Date类型
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(DateUtils.parseDate(text));
            }
        });
    }

    /**
     * 设置请求分页数据
     */
    protected void startPage() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize)) {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.startPage(pageNum, pageSize, orderBy);
        }
    }

    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected TableDataInfo getDataTable(List<?> list) {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(0);
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }

    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    protected AjaxResult toAjax(int rows) {
        return rows > 0 ? success() : error();
    }

    /**
     * 响应返回结果
     *
     * @param result 结果
     * @return 操作结果
     */
    protected AjaxResult toAjax(boolean result) {
        return result ? success() : error();
    }

    /**
     * 返回成功
     */
    public AjaxResult success() {
        return AjaxResult.success();
    }

    /**
     * 返回失败消息
     */
    public AjaxResult error() {
        return AjaxResult.error();
    }

    /**
     * 返回成功消息
     */
    public AjaxResult success(String message) {
        return AjaxResult.success(message);
    }

    /**
     * 返回失败消息
     */
    public AjaxResult error(String message) {
        return AjaxResult.error(message);
    }

    /**
     * 返回错误码消息
     */
    public AjaxResult error(int code, String message) {
        return AjaxResult.error(code, message);
    }

    /**
     * 页面跳转
     */
    public String redirect(String url) {
        return StringUtils.format("redirect:{}", url);
    }

    public User getSysUser() {
        return ShiroUtils.getSysUser();
    }

    public void setSysUser(User user) {
        ShiroUtils.setSysUser(user);
    }

    public Long getUserId() {
        return getSysUser().getUserId();
    }

    public String getLoginName() {
        return getSysUser().getLoginName();
    }

    public Integer changePageNum(Integer pageNum, String directPage) {
        if (!StringUtils.isEmpty(directPage)) {
            pageNum = NumberUtils.toInt(directPage, 1);// 转换失败返回默认值1
        }
        pageNum = pageNum == null ? 1 : pageNum;
        return pageNum;
    }
    
    /**
	 * 获取浏览器类型
	 * 
	 * @author wzw 2014年11月13日
	 * @param request
	 * @return IE9: Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0) FireFox: Mozilla/5.0 (Windows
	 *         NT 6.1; WOW64; rv:33.0) Gecko/20100101 Firefox/33.0 360:Mozilla/5.0 (Windows NT 6.1; WOW64)
	 *         AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36 IE10: Mozilla/5.0 (compatible;
	 *         MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0) google: Mozilla/5.0 (Windows NT 6.1; WOW64)
	 *         AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.104 Safari/537.36
	 */
	public static String getExplorerType(HttpServletRequest request) {
		try {
			String agent = request.getHeader("User-Agent");
			if (StringUtils.isBlank(agent))
				return null;
			if (agent.contains("MSIE")) {
				return agent.split(";")[1];
			} else if (agent.contains("Firefox")) {
				return "Firefox";
			} else if (agent.contains("Chrome")) {
				return "Chrome";
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String encode(String fileName, String encode) {
		String name = null;
		try {
			name = URLEncoder.encode(fileName, encode);
			name = name.replace("+", "%20");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return name;
	}
	
	public void setExcelResponse(String filename,HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		response.reset();// 清空输出流
		response.setContentType("application/octet-stream; charset=utf-8");
		String explorerType = getExplorerType(request);

		
		if (explorerType == null || explorerType.contains("IE")) {// IE
			response.setHeader("Content-Disposition",
					"attachment; filename=\"" + encode(filename, "utf-8") + ".xlsx" + "\"");
		} else {// fireFox/Chrome
			response.setHeader("Content-Disposition",
					"attachment; filename=" + new String(filename.getBytes("utf-8"), "ISO8859-1") + ".xlsx");
		}
		response.setContentType("application/msexcel");// 定义输出类型
	}
}
