package com.dimple.project.king.bookmarks.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dimple.common.constant.Constants;
import com.dimple.common.utils.security.ShiroUtils;
import com.dimple.framework.web.domain.AjaxResult;
import com.dimple.project.blog.blog.domain.Blog;
import com.dimple.project.blog.blog.service.BlogService;
import com.dimple.project.front.controller.BaseBlogController;
import com.dimple.project.king.bookmarks.domain.BookmarksEntity;
import com.dimple.project.king.bookmarks.service.BookmarksService;
import com.dimple.project.system.user.domain.User;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;


/**
 * 建议
 * @author ls2008
 * @date 2020-01-06 16:03:26
 */
@Controller
@RequestMapping(value = "/bookmarks")
public class BookmarksController extends BaseBlogController {

	@Autowired
	private BookmarksService service;
	@Autowired
	private BlogService blogService;

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
		if(user==null){
			return blogLoginPage();
		}
		PageHelper.startPage(changePageNum(pageNum, "") , Constants.BLOG_PAGE_SIZE);
		model.addAttribute("blogs", new PageInfo<>(blogService.selectBookmarksList(user.getUserId())));
		setBLogHead(user.getLoginName(),model);
		return "king/bookmarks/bookmarks_list";
	}

	@ResponseBody
	@RequestMapping("/add")
	public AjaxResult addView(Long blogId) {
		User user = ShiroUtils.getSysUser();
		if(user==null){
			return AjaxResult.error("请重新登录");
		}
		Blog blog = blogService.selectBlogById(blogId);
		if(blog == null){
			return AjaxResult.error("博客不存在");
		}
		QueryWrapper<BookmarksEntity> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("blog_id",blogId);
		queryWrapper.eq("user_id",user.getUserId());
		if(service.count(queryWrapper)>0){
			return AjaxResult.error("该博客已收藏");
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