package com.dimple.project.blog.blog.controller;

import com.dimple.common.constant.BlogConstants;
import com.dimple.common.constant.Constants;
import com.dimple.common.utils.security.ShiroUtils;
import com.dimple.framework.aspectj.lang.annotation.Log;
import com.dimple.framework.aspectj.lang.enums.BusinessType;
import com.dimple.framework.web.controller.BaseController;
import com.dimple.framework.web.domain.AjaxResult;
import com.dimple.framework.web.page.TableDataInfo;
import com.dimple.project.blog.blog.domain.Blog;
import com.dimple.project.blog.blog.service.BlogService;
import com.dimple.project.blog.category.domain.Category;
import com.dimple.project.blog.category.service.CategoryService;
import com.dimple.project.common.service.FileService;
import com.dimple.project.system.user.domain.User;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

/**
 * @className: BlogController
 * @description: 博客处理Controller
 * @auther: Dimple
 * @Date: 2019/3/16
 * @Version: 1.0
 */
@Controller
@RequestMapping("/blog/blog")
public class BlogController extends BaseController {

    @Autowired
    BlogService blogService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    FileService fileService;

    @RequiresPermissions("blog:blog:view")
    @GetMapping()
    public String blog(Model model) {
        model.addAttribute("total", blogService.selectBlogCountByStatus(BlogConstants.BLOG_TOTAL));
        model.addAttribute("published", blogService.selectBlogCountByStatus(BlogConstants.BLOG_PUBLISHED));
        model.addAttribute("draft", blogService.selectBlogCountByStatus(BlogConstants.BLOG_DRAFT));
        model.addAttribute("garbage", blogService.selectBlogCountByStatus(BlogConstants.BLOG_GARBAGE));
        return "blog/blog/blog";
    }

    @GetMapping("/image")
    public String img(String callBackMethod,Integer width , Integer height,Model model) {
      model.addAttribute("callBackMethod", callBackMethod);
      model.addAttribute("width", width==null?0:width);
      model.addAttribute("height", height==null?0:height);
      return "blog/blog/img_king_single";
    }

    @GetMapping("/list")
    @RequiresPermissions("blog:blog:list")
    @ResponseBody
    public TableDataInfo list(Blog blog) {
        startPage();
        User user = ShiroUtils.getSysUser();
        if (!user.isAdmin()) {
          blog.setCreateBy(user.getLoginName());
        }
        List<Blog> list = blogService.selectBlogList(blog);
        return getDataTable(list);
    }

    @GetMapping("/add")
    public String add(Model model,Long funcId) {
        model.addAttribute("categories", categoryService.selectCategoryList(new Category()));
        model.addAttribute("funcId", funcId);
        return "blog/blog/add";
    }
    
    @GetMapping("/add/{funcId}.html")
    public String toAdd(Model model,@PathVariable Long funcId) {
        model.addAttribute("categories", categoryService.selectCategoryList(new Category()));
        model.addAttribute("funcId", funcId);
        return "blog/blog/add_phone";
    }

    @Log(title = "系统博客", businessType = BusinessType.INSERT)
    @RequiresPermissions("blog:blog:add")
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Blog blog,MultipartHttpServletRequest request) {
        int i = blogService.insertBlog(blog,request.getFiles("attachment[]"));
        return toAjax(i);
    }

    @GetMapping("/edit/{blogId}")
    public String edit(@PathVariable Integer blogId, Model model) {
        model.addAttribute("blog", blogService.selectBlogWithTextAndTagsAndCategoryByBlogId(blogId));
        model.addAttribute("categories", categoryService.selectCategoryList(new Category()));
        return "blog/blog/edit";
    }

    @GetMapping("/func/edit/{blogId}")
    public String funcEdit(@PathVariable Integer blogId, Model model) {
        model.addAttribute("blog", blogService.selectBlogWithTextAndTagsAndCategoryByBlogId(blogId));
        model.addAttribute("categories", categoryService.selectCategoryList(new Category()));
        model.addAttribute("fileList",fileService.listByEntityInfo(Constants.FILE_ITEM_ENTITYTYPE_BLOG,Long.valueOf(blogId)));
        return "blog/blog/edit_phone";
    }

    @PutMapping("/edit")
    @RequiresPermissions("blog:blog:edit")
    @Log(title = "系统博客", businessType = BusinessType.UPDATE)
    @ResponseBody
    public AjaxResult editSave(Blog blog, MultipartHttpServletRequest request) {
        return toAjax(blogService.updateBlog(blog,request.getFiles("attachment[]")));
    }

    @PutMapping("/support/{support}")
    @RequiresPermissions("blog:blog:support")
    @Log(title = "系统博客", businessType = BusinessType.UPDATE)
    @ResponseBody
    public AjaxResult supportSave(Integer blogId, @PathVariable String support) {
        return toAjax(blogService.updateBlogSupportById(blogId, support));
    }

    @PutMapping("/status/{status}")
    @RequiresPermissions("blog:blog:status")
    @Log(title = "系统博客", businessType = BusinessType.UPDATE)
    @ResponseBody
    public AjaxResult statusSave(String blogIds, @PathVariable String status) {
        return toAjax(blogService.updateBlogStatusById(blogIds, status));
    }

    @PutMapping("/allowComment/{blogId}/{allowComment}")
    @RequiresPermissions("blog:blog:allowComment")
    @Log(title = "系统博客", businessType = BusinessType.UPDATE)
    @ResponseBody
    public AjaxResult allowCommentSave(@PathVariable Boolean allowComment, @PathVariable Integer blogId) {
        return toAjax(blogService.updateBlogAllowCommentByBlogId(allowComment, blogId));
    }

    @DeleteMapping("/remove")
    @Log(title = "系统博客", businessType = BusinessType.DELETE)
    @RequiresPermissions("blog:blog:remove")
    @ResponseBody
    public AjaxResult remove(Integer[] ids) {

        return toAjax(blogService.deleteBlogById(ids));//
    }


}
