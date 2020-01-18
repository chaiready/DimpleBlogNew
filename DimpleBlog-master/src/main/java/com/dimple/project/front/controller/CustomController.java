package com.dimple.project.front.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dimple.common.constant.CommonConstant;
import com.dimple.common.constant.Constants;
import com.dimple.common.utils.RandomUtil;
import com.dimple.common.utils.StringUtils;
import com.dimple.common.utils.security.ShiroUtils;
import com.dimple.framework.aspectj.lang.annotation.VLog;
import com.dimple.framework.web.domain.AjaxResult;
import com.dimple.project.blog.blog.domain.Blog;
import com.dimple.project.blog.blog.service.BlogService;
import com.dimple.project.blog.category.service.CategoryService;
import com.dimple.project.blog.comment.domain.Comment;
import com.dimple.project.blog.comment.service.CommentService;
import com.dimple.project.blog.tag.domain.Tag;
import com.dimple.project.blog.tag.service.TagService;
import com.dimple.project.common.service.FileService;
import com.dimple.project.common.service.MailService;
import com.dimple.project.front.service.HomeService;
import com.dimple.project.king.func.domain.Func;
import com.dimple.project.king.func.service.IFuncService;
import com.dimple.project.king.suggest.domain.SuggestEntity;
import com.dimple.project.king.suggest.service.SuggestService;
import com.dimple.project.king.userlog.domain.UserLogEntity;
import com.dimple.project.king.userlog.service.UserLogService;
import com.dimple.project.link.service.LinkService;
import com.dimple.project.system.carouselMap.entity.CarouselMap;
import com.dimple.project.system.carouselMap.service.CarouselMapService;
import com.dimple.project.system.notice.service.INoticeService;
import com.dimple.project.system.user.domain.User;
import com.dimple.project.system.user.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户定义
 *
 * @author 90907
 */
@Slf4j
@Controller
@RequestMapping("/bbs")
public class CustomController extends BaseBlogController {
  
    private long FUNC_FIRST = 0;
    
    private Long FUNC_NULL = null;
    
    private Integer DEFAULT_PAGENUM = 1;
  
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
    @Autowired
    private MailService mailService;
    @Autowired
    private UserLogService userLogService;
    @Autowired
    private SuggestService suggestService;
    @Autowired
    FileService fileService;


    /**
     * 设置前台页面公用的部分代码 均设置Redis缓存
     */
    private void setCommonMessage(Model model, String loginName,Long funcId,Integer pageNum) {
        List<Func> funcList = setBLogHead(loginName,model);
        model.addAttribute("toPage", loginName);
        if(funcId!=FUNC_NULL){
          if (funcId==FUNC_FIRST&&CollectionUtils.isNotEmpty(funcList)) {
            funcId = funcList.get(0).getId();
            model.addAttribute("funcName", funcList.get(0).getFuncName());
          }else if(funcId!=null&&funcId!=0){
            model.addAttribute("funcName", funcService.getById(funcId).getFuncName());
          }
          model.addAttribute("funcId", funcId);
          PageHelper.startPage(changePageNum(pageNum, "") , Constants.BLOG_PAGE_SIZE);
          model.addAttribute("blogs", new PageInfo<>(homeService.selectBlogListByFuncId(funcId)));
        }
        
        // 查询所有的标签
        model.addAttribute("tags", tagService.selectTagList(new Tag()));
        // 查询文章排行
        model.addAttribute("blogRanking", blogService.selectBlogRanking());
        // 查询推荐博文
        model.addAttribute("supportBlog", blogService.selectSupportBlog());
        // 查询通知
        model.addAttribute("notices", noticeService.selectNoticeListDisplay());
        // 获取友链信息
        model.addAttribute("links", linkService.selectLinkListFront());
    }

    @GetMapping({"/{loginName}.html","/{loginName}/index.html"})
    @VLog(title = "用户首页")
    public String defaultIndex(@PathVariable String loginName, Integer pageNum, Model model) {
        setCommonMessage(model, loginName,FUNC_FIRST,pageNum);
        // 放置轮播图
        List<CarouselMap> carouselMaps = carouselMapService.selectByCreateBy(loginName);
        if(CollectionUtils.isEmpty(carouselMaps)){
          carouselMaps.add(new CarouselMap("/front/images/touploadimg.jpg","",""));
        }
        model.addAttribute("carouselMaps", carouselMaps);
        // 查询用户信息
//        User user = userService.selectUserByLoginName(loginName);
//        if (user != null) {
//            model.addAttribute("user", user);
//            return "front/custom/index";
//        }
//        return "front/index";
        return "front/custom/index";
    }
    
    
    @VLog(title = "菜单的博客")
    @GetMapping({"/{loginName}/{funcId}.html"})
    public String funcBlog(@PathVariable String loginName, @PathVariable Long funcId,
                           Integer pageNum, Model model) {
        List<Func> funcList = funcService.findBbsByCreator(loginName);
        if (CollectionUtils.isNotEmpty(funcList)) {
            if(funcList.get(0).getId().longValue()==funcId.longValue()){
              return defaultIndex(loginName, pageNum, model);
            }
        }
        setCommonMessage(model, loginName,funcId,pageNum);
//        model.addAttribute("funcId", funcId);
//        model.addAttribute("funcName", funcService.getById(funcId).getFuncName());
        return "front/custom/category";
    }


    @VLog(title = "博客详情")
    @GetMapping("/{loginName}/{funcId}/{blogId}.html")
    public String article(@PathVariable String loginName, @PathVariable Long funcId,@PathVariable Integer blogId,Integer pageNum, Model model) {
        setCommonMessage(model, loginName, funcId, pageNum);
        Blog blog = blogService.selectBlogWithTextAndTagsAndCategoryByBlogId(blogId);
        //只能访问是已经发表的文章
        if (!CommonConstant.one.equals(blog.getStatus())) {
            return "error/404";
        }
        //增加点击量
        blogService.incrementBlogClick(blogId);
        model.addAttribute("blog", blog);
        model.addAttribute("previousBlog", blogService.selectPreviousBlogByFuncIdAndId(funcId, blogId));
        model.addAttribute("nextBlog", blogService.selectNextBlogByFuncIdAndId(funcId, blogId));
        model.addAttribute("randBlogList", blogService.selectRandBlogList());
        model.addAttribute("comments", commentService.selectCommentListForFront(new Comment(blogId,true)));
        model.addAttribute("fileList",fileService.listByEntityInfo(Constants.FILE_ITEM_ENTITYTYPE_BLOG,Long.valueOf(blogId)));
        return "front/custom/article_summernote";//front/article 将simpleMde 改成 summerNote 编辑器
    }


    @VLog(title = "博客详情")
    @GetMapping("/detail/{blogId}.html")
    public String articleDetail(@PathVariable Integer blogId,Integer pageNum, Model model) {
        User user = ShiroUtils.getSysUser();
        if(user==null){
            return blogLoginPage();
        }
        setCommonMessage(model, user.getLoginName(), FUNC_NULL, pageNum);
        Blog blog = blogService.selectBlogWithTextAndTagsAndCategoryByBlogId(blogId);
        //只能访问是已经发表的文章
        if (!CommonConstant.one.equals(blog.getStatus())) {
            return "error/404";
        }
        //增加点击量
        blogService.incrementBlogClick(blogId);
        model.addAttribute("blog", blog);
        model.addAttribute("comments", commentService.selectCommentListForFront(new Comment(blogId,true)));
        model.addAttribute("fileList",fileService.listByEntityInfo(Constants.FILE_ITEM_ENTITYTYPE_BLOG,Long.valueOf(blogId)));
        return "front/custom/article_summernote_detail";//front/article 将simpleMde 改成 summerNote 编辑器
    }
    
    
    
    @GetMapping("/{loginName}/images.html")
    @VLog(title = "用户首页")
    public String images(@PathVariable String loginName, Integer pageNum, Model model) {
//        setCommonMessage(model, loginName,FUNC_NULL,pageNum);
    	setBLogHead(loginName,model);
        return "front/images";
    }
    
    
    @GetMapping("/{loginName}/suggest.html")
    public String suggestList(Model model,@PathVariable String loginName, Integer pageNum, String directPage) {
      User user = ShiroUtils.getSysUser();
      List<SuggestEntity> objList = new ArrayList<SuggestEntity>();
      if(user!=null){
        QueryWrapper<SuggestEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("create_by", user.getLoginName()).orderByDesc("create_time");
        PageHelper.startPage(changePageNum(pageNum, directPage), Constants.BLOG_PAGE_SIZE);
        objList = suggestService.list(queryWrapper);
      }
      PageInfo<SuggestEntity> pageInfo = new PageInfo<>(objList);
      model.addAttribute("objList", pageInfo);

      setBLogHead(loginName,model);
      // 查询通知
      model.addAttribute("notices", noticeService.selectNoticeListDisplay());

      return "king/suggest/suggest_list";
    }


    /**
     * 跳转到登录页
     *
     * @param model
     * @return
     */
    @VLog(title = "跳转到登录页")
    @GetMapping("/front/toLogin")
    public String toLogin(Model model,String toPage) {
        model.addAttribute("toPage", StringUtils.isEmpty(toPage)?"":toPage);
        return blogLoginPage();
    }


    @PostMapping("/front/login")
    @ResponseBody
    public AjaxResult frontLogin(String loginName, String password,String toPage) {
        UsernamePasswordToken token = new UsernamePasswordToken(loginName, password, false);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            if(StringUtils.isEmpty(toPage)){
              AjaxResult result = AjaxResult.success();
              result.put("redirectPage", "/bbs/"+loginName+".html");
              return result;
            }else{
              AjaxResult result = AjaxResult.success();
              if(toPage.equals(Constants.EXAM_INDEX_PAGE)){
            	  result.put("redirectPage", toPage);
              }else{
            	  result.put("redirectPage", "/bbs/"+toPage+".html");
              }
              return result;
            }
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
          return defaultIndex(user.getLoginName(), DEFAULT_PAGENUM, model);
        }
        return blogIndex();
    }

    /**
     * 注册
     * @param toPage
     * @param model
     * @return
     */
    @GetMapping("/front/toReg")
    public String frontToReg(String toPage, Model model) {
        model.addAttribute("toPage", StringUtils.isEmpty(toPage)?"":toPage);
        return "front/login/reg";
    }

    
    @PostMapping("/front/reg")
    @ResponseBody
    public AjaxResult frontReg(User user,String toPage ){
        try {
          String password = user.getPassword();
            user.setUserName(user.getLoginName());
            userService.regBlogUser(user);
            if(StringUtils.isEmpty(toPage)){
              return success("/bbs/"+user.getLoginName()+".html");
            }else{
              return frontLogin(user.getLoginName(), password, toPage);
            }
        } catch (DuplicateKeyException e) {
            log.error(e.getMessage());
            return error("该用户已注册");
        } catch (AuthenticationException e) {
          String msg = "用户或密码错误";
          if (StringUtils.isNotEmpty(e.getMessage())) {
              msg = e.getMessage();
          }
          return error(msg);
      } catch (Exception e) {
            log.error(e.getMessage());
            return error("注册失败，请联系管理员");
        }
    }

    /**
     * 忘记密码
     * @param loginName 登录名
     * @param model model对象
     * @return
     */
    @ResponseBody
    @RequestMapping("/front/toForgetPwd")
    public AjaxResult toForgetPwd(String loginName, Model model) {
      User user = userService.selectUserByLoginName(loginName);
      if(user == null){
        return AjaxResult.error("用户名不存在");
      }
      if(StringUtils.isEmpty(user.getEmail())){
        return AjaxResult.error("邮箱为空，不能找回密码");
      }
      Context context = new Context();
      String vericode = RandomUtil.randomNum(6);
      context.setVariable("vericode", vericode);
      mailService.sendTemplateMail(user.getEmail(), "【5180it】忘记密码", "forgetPwdEmailTemplate", context);
      UserLogEntity userLog = new UserLogEntity();
      userLog.setLogtype(UserLogEntity.FORGET_PWD);
      userLog.setContent(vericode);
      userLog.setCreateBy(loginName);
      userLog.setCreateTime(new Date());
      userLogService.save(userLog);
      return AjaxResult.success("已发送验证码到【"+user.getEmail()+"】");
    }

    
    @GetMapping("/front/toResetPwd")
    public String toResetPwd(String loginName , String toPage, Model model) {
        model.addAttribute("loginName", loginName);
        model.addAttribute("toPage", toPage);
        return "front/login/reset_pwd";
    }
    
    @ResponseBody
    @RequestMapping("/front/resetPwd")
    public AjaxResult resetPwd(String loginName ,String password , String vericode , String toPage, Model model) {
        User entity = userService.selectUserByLoginName(loginName);
        if(entity == null){
          return AjaxResult.error("用户名不存在");
        }
        model.addAttribute("loginName", loginName);
        model.addAttribute("toPage", toPage);
        QueryWrapper<UserLogEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("create_by", loginName).eq("logtype", UserLogEntity.FORGET_PWD).orderByDesc("create_time");
        Page<UserLogEntity> page=new Page<UserLogEntity>(1,1);
        IPage<UserLogEntity> iPage = userLogService.page(page, queryWrapper);
        List<UserLogEntity> logList = iPage.getRecords();
        if(CollectionUtils.isNotEmpty(logList)){
          UserLogEntity log = logList.get(0);
          if(vericode!=null&&vericode.equals(log.getContent())){
            User user = new User();
            user.setUserId(entity.getUserId());
            user.setPassword(password);
            userService.resetUserPwd(user);
            return AjaxResult.success("修改密码成功");
          }
        }
        return AjaxResult.error("验证码不一致");
    }
    

    /**
     * 留言
     */
    @VLog(title = "留言页")
    @GetMapping("/{loginName}/comment.html")
    public String leaveComment(Model model,@PathVariable String loginName) {
        User user = ShiroUtils.getSysUser();
        if (user == null) {
            return toLogin(model,"");
        }
        setCommonMessage(model, loginName,FUNC_NULL,DEFAULT_PAGENUM);
        Comment comment = new Comment();
        comment.setPageId(-1000);
        model.addAttribute("comments", commentService.selectCommentListForFront(comment));
        model.addAttribute("pageId", -1000);
        return "front/custom/comment";
    }
    

    @VLog(title = "搜索")
    @GetMapping("/front/search/{loginName}.html")
    public String search(@PathVariable String loginName, String search_word, Integer pageNum, Model model) {
        setCommonMessage(model, loginName,FUNC_NULL,DEFAULT_PAGENUM);
        PageHelper.startPage(pageNum == null ? 1 : pageNum, Constants.BLOG_PAGE_SIZE, "create_time desc");
        Blog blog = new Blog();
        blog.setTitle(search_word);
        List<Blog> blogs = blogService.selectBlogList(blog);
        model.addAttribute("blogs", new PageInfo<>(blogs));
        model.addAttribute("keyWord", search_word);
        return "front/custom/search";
    }
    
    @GetMapping("/ws")
    public String ws(Model model) {
        return "front/custom/websocket";
    }
}
