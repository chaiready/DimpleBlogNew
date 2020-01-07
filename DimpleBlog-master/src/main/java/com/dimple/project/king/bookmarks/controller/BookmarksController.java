package com.dimple.project.king.bookmarks.controller;

import com.dimple.common.constant.Constants;
import com.dimple.common.utils.security.ShiroUtils;
import com.dimple.framework.web.controller.BaseController;
import com.dimple.framework.web.domain.AjaxResult;
import com.dimple.project.blog.blog.domain.Blog;
import com.dimple.project.blog.blog.service.BlogService;
import com.dimple.project.king.bookmarks.domain.BookmarksEntity;
import com.dimple.project.king.bookmarks.service.BookmarksService;
import com.dimple.project.king.func.domain.Func;
import com.dimple.project.king.func.service.IFuncService;
import com.dimple.project.system.user.domain.User;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;


/**
 * 建议
 * @author ls2008
 * @date 2020-01-06 16:03:26
 */
@Controller
@RequestMapping(value = "/bookmarks")
public class BookmarksController extends BaseController{

	@Autowired
	private BookmarksService service;
	@Autowired
	private BlogService blogService;
	@Autowired
	private IFuncService funcService;

	/**
	 *
	 * @Title: listView
	 * @author ls2008
	 * @date 2020-01-06 16:03:26
	 * @param @param model
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping("/list.html")
	public String listView(Model model, Integer pageNum, String directPage) {
		User user = ShiroUtils.getSysUser();
		List<Func> funcList = funcService.findBbsByCreator(user.getLoginName());
		model.addAttribute("funcList", funcList);
		PageHelper.startPage(changePageNum(pageNum, "") , Constants.BLOG_PAGE_SIZE);
		model.addAttribute("blogs", new PageInfo<>(blogService.selectBookmarksList(user.getUserId())));
		model.addAttribute("loginName", user.getLoginName());
		model.addAttribute("curUser", user);
		return "king/bookmarks/bookmarks_list";
	}

	@ResponseBody
	@RequestMapping("/add")
	public AjaxResult addView(Long blogId) {
		User user = ShiroUtils.getSysUser();
		Blog blog = blogService.selectBlogById(blogId);
		if(blog == null){
			return AjaxResult.error("博客不存在");
		}
		BookmarksEntity obj = new BookmarksEntity();
		obj.setBlogId(blogId);
		obj.setUserId(user.getUserId());
		obj.setCreateBy(user.getLoginName());
		obj.setCreateTime(new Date());
		service.save(obj);
		return AjaxResult.success();
	}

	@ResponseBody
	@RequestMapping("/del")
	public AjaxResult del(Long objId) {
		User user = ShiroUtils.getSysUser();
		BookmarksEntity obj = service.getById(objId);
		if(obj == null){
			return AjaxResult.error("收藏为空");
		}
		if(obj.getUserId().longValue()!=ShiroUtils.getUserId()){
			return AjaxResult.error("该收藏不属于当前用户");
		}
		service.removeById(objId);
		return AjaxResult.success();
	}
}