package com.dimple.project.front.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.dimple.common.utils.StringUtils;
import com.dimple.common.utils.security.ShiroUtils;
import com.dimple.framework.aspectj.lang.annotation.VLog;
import com.dimple.framework.web.controller.BaseController;
import com.dimple.framework.web.domain.AjaxResult;
import com.dimple.project.blog.blog.domain.Blog;
import com.dimple.project.blog.blog.service.BlogService;
import com.dimple.project.blog.category.domain.Category;
import com.dimple.project.blog.category.service.CategoryService;
import com.dimple.project.blog.comment.domain.Comment;
import com.dimple.project.blog.comment.service.CommentService;
import com.dimple.project.blog.tag.domain.Tag;
import com.dimple.project.blog.tag.service.TagService;
import com.dimple.project.front.service.HomeService;
import com.dimple.project.king.func.domain.Func;
import com.dimple.project.king.func.service.IFuncService;
import com.dimple.project.link.service.LinkService;
import com.dimple.project.system.carouselMap.service.CarouselMapService;
import com.dimple.project.system.notice.service.INoticeService;
import com.dimple.project.system.user.domain.User;
import com.dimple.project.system.user.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;

/**
 * 用户定义
 * 
 * @author 90907
 *
 */
@Slf4j
@Controller
@RequestMapping("/bbs")
public class CustomController extends BaseController {
  @Autowired
  HomeService homeService;
  @Autowired
  BlogService blogService;
  @Autowired
  CategoryService categoryService;
  @Autowired
  TagService tagService;
  @Autowired
  LinkService linkService;
  @Autowired
  INoticeService noticeService;
  @Autowired
  CarouselMapService carouselMapService;
  @Autowired
  private UserService userService;
  @Autowired
  private IFuncService funcService;
  @Autowired
  CommentService commentService;

  /**
   * 设置前台页面公用的部分代码 均设置Redis缓存
   */
  private void setCommonMessage(Model model, String loginName) {
    // 获取分类下拉项中的分类
    // model.addAttribute("categories",
    // categoryService.selectSupportCategoryList());
    List<Func> funcList = funcService.findByCreator(loginName);
    List<Func> defaultFucs = new ArrayList<Func>();
    // Func indexFunc = new Func();
    // indexFunc.setUrl("/bbs/" + loginName + "/index.html");
    // indexFunc.setFuncName("首页");
    // defaultFucs.add(indexFunc);
    for (Func func : funcList) {
      func.setUrl("/bbs/" + loginName + func.getUrl());
      defaultFucs.add(func);
    }
    model.addAttribute("funcList", defaultFucs);

    // 查询所有的标签
    model.addAttribute("tags", tagService.selectTagList(new Tag()));
    // 查询最近更新的文章
//    model.addAttribute("newestUpdateBlog", blogService.selectNewestUpdateBlog());
    // 查询文章排行
    model.addAttribute("blogRanking", blogService.selectBlogRanking());
    // 查询推荐博文
    model.addAttribute("supportBlog", blogService.selectSupportBlog());
    // 查询通知
    model.addAttribute("notices", noticeService.selectNoticeListDisplay());
    // 获取友链信息
    model.addAttribute("links", linkService.selectLinkListFront());
    // 设置当前访问主页名
    model.addAttribute("loginName", loginName);

    model.addAttribute("curUser", ShiroUtils.getSysUser());
  }

  @GetMapping("/{loginName}.html")
  @VLog(title = "用户首页")
  public String defaultIndex(@PathVariable String loginName, Integer pageNum, Model model) {
    setCommonMessage(model, loginName);
    PageHelper.startPage(pageNum == null ? 1 : pageNum, 12, "create_time desc");
    // Blog blog = new Blog();
    // blog.setCreateBy(loginName);
    // model.addAttribute("blogs", new PageInfo<>(homeService.selectBlogsByCreator(blog)));
    model.addAttribute("blogs", new PageInfo<>(homeService.selectBlogListByFuncId(1)));

    // 放置轮播图
    model.addAttribute("carouselMaps", carouselMapService.selectCarouselMapListFront());

    // 查询用户信息
    User user = userService.selectUserByLoginName(loginName);
    if (user != null) {
      model.addAttribute("user", user);
      return "front/custom/index";
    }
    return "front/index";
  }

  @GetMapping("/{loginName}/index.html")
  @VLog(title = "用户首页")
  public String loginNameIndex(@PathVariable String loginName, Integer pageNum, Model model) {
    return defaultIndex(loginName, pageNum, model);
  }

  @GetMapping("/{loginName}/images.html")
  @VLog(title = "用户首页")
  public String images(@PathVariable String loginName, Integer pageNum, Model model) {
    setCommonMessage(model, loginName);
    return "front/images";
  }


  /**
   * 跳转到登录页
   * 
   * @param model
   * @return
   */
  @VLog(title = "跳转到登录页")
  @GetMapping("/front/toLogin")
  public String toLogin(Model model) {
    return "front/login/login";
  }


  @PostMapping("/front/login")
  @ResponseBody
  public AjaxResult frontLogin(String loginName, String password, Model model) {
    UsernamePasswordToken token = new UsernamePasswordToken(loginName, password, false);
    Subject subject = SecurityUtils.getSubject();
    try {
      subject.login(token);
      return success(loginName);
    } catch (AuthenticationException e) {
      String msg = "用户或密码错误";
      if (StringUtils.isNotEmpty(e.getMessage())) {
        msg = e.getMessage();
      }
      return error(msg);
    }
  }

  @RequestMapping("/front/loginSuc")
  public String loginSuc(Model model) {
    User user = ShiroUtils.getSysUser();
    if (user != null) {
      Integer pageNum = 0;
      setCommonMessage(model, user.getLoginName());
      PageHelper.startPage(pageNum == null ? 1 : pageNum, 12, "create_time desc");
      model.addAttribute("blogs", new PageInfo<>(homeService.selectFrontBlogList(new Blog())));
      // 放置轮播图
      model.addAttribute("carouselMaps", carouselMapService.selectCarouselMapListFront());

      model.addAttribute("user", user);
      return redirect("/front/custom/index");
    }
    return "front/index";
  }

  /**
   * 注册
   * 
   * @param loginName
   * @param password
   * @param model
   * @return
   */
  @GetMapping("/front/toReg")
  public String frontToReg(String loginName, String password, Model model) {
    return "front/login/reg";
  }

  @PostMapping("/front/reg")
  @ResponseBody
  public AjaxResult frontReg(User user) {
    try {
      user.setUserName(user.getLoginName());
      userService.regUser(user);
      return success(user.getLoginName());
    } catch (DuplicateKeyException e) {
      log.error(e.getMessage());
      return error("该用户已注册");
    } catch (Exception e) {
      log.error(e.getMessage());
      return error("注册失败，请联系管理员");
    }
  }



  @VLog(title = "分类")
  @GetMapping({"/{loginName}/func/{funcId}.html"})
  public String funcBlog(@PathVariable String loginName, @PathVariable Integer funcId,
      Integer pageNum, Model model) {
    setCommonMessage(model, loginName);
    // model.addAttribute("category", categoryService.selectCategoryById(categoryId));
    Category category = new Category();
    category.setCategoryTitle("");
    category.setDescription("");
    category.setCreateTime(new Date());
    model.addAttribute("category", category);
    PageHelper.startPage(pageNum == null ? 1 : pageNum, 10, "create_time desc");
    model.addAttribute("blogs", new PageInfo<>(homeService.selectBlogListByFuncId(funcId)));
    model.addAttribute("funcId", funcId);
    return "front/custom/category";
  }


  /**
   * 留言
   */
  @VLog(title = "留言页")
  @GetMapping("/front/comment.html")
  public String leaveComment(Model model) {
    User user = ShiroUtils.getSysUser();
    if(user==null){
      return toLogin(model);
    }
    setCommonMessage(model,user.getLoginName());
    Comment comment = new Comment();
    comment.setPageId(-1000);
    model.addAttribute("comments", commentService.selectCommentListForFront(comment));
    model.addAttribute("pageId", -1000);
    return "front/custom/comment";
  }
  
  @VLog(title = "搜索")
  @GetMapping("/front/search/{loginName}.html")
  public String search(@PathVariable String loginName,String search_word, Integer pageNum, Model model) {
      setCommonMessage(model,loginName);
      PageHelper.startPage(pageNum == null ? 1 : pageNum, 10, "create_time desc");
      Blog blog = new Blog();
      blog.setTitle(search_word);
      List<Blog> blogs = blogService.selectBlogList(blog);
      model.addAttribute("blogs", new PageInfo<>(blogs));
      model.addAttribute("keyWord", search_word);
      return "front/custom/search";
  }
}
