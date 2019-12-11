package com.dimple.project.king.userlog.controller;

import org.springframework.ui.Model;
import javax.servlet.ServletRequest;
import com.dimple.framework.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.dimple.project.king.userlog.domain.UserLogEntity;
import com.dimple.project.king.userlog.service.UserLogService;

/**
 * 建议
 * @author ls2008
 * @date 2019-12-11 14:08:40
 */
@Controller
@RequestMapping(value = "/ver/auditCols")
public class UserLogController extends BaseController{

	@Autowired
	private UserLogService service;

	/**
	 * 
	 * @Title: listView
	 * @author ls2008
	 * @date 2019-12-11 14:08:40
	 * @param @param model
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	@RequestMapping("/list")
	public String listView(Model model) {
		return "modules/ver/auditcols/UserLog_list";
	}

	@RequestMapping("/add")
	public String addView(Model model, ServletRequest request) {
		return "modules/ver/auditcols/UserLog_edit";
	}

	@RequestMapping("/edit")
	public String editView(Model model, ServletRequest request, UserLogEntity entity) {
		return "modules/ver/auditcols/UserLog_edit";
	}

	@RequestMapping("/detail")
	public String detailView(Model model, ServletRequest request, UserLogEntity entity) {
		return "modules/ver/auditcols/UserLog_detail";
	}

}