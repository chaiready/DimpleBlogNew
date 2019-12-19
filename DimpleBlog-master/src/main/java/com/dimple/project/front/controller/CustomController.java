package com.dimple.project.front.controller;

import java.util.Date;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
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
import org.thymeleaf.context.Context;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dimple.common.utils.RandomUtil;
import com.dimple.common.utils.StringUtils;
import com.dimple.common.utils.security.ShiroUtils;
import com.dimple.framework.aspectj.lang.annotation.VLog;
import com.dimple.framework.web.controller.BaseController;
import com.dimple.framework.web.domain.AjaxResult;
import com.dimple.project.blog.blog.domain.Blog;
import com.dimple.project.blog.blog.service.BlogService;
import com.dimple.project.blog.category.service.CategoryService;
import com.dimple.project.blog.comment.domain.Comment;
import com.dimple.project.blog.comment.service.CommentService;
import com.dimple.project.blog.tag.domain.Tag;
import com.dimple.project.blog.tag.service.TagService;
import com.dimple.project.common.service.MailService;
import com.dimple.project.front.service.HomeService;
import com.dimple.project.king.func.domain.Func;
import com.dimple.project.king.func.service.IFuncService;
import com.dimple.project.king.userlog.domain.UserLogEntity;
import com.dimple.project.king.userlog.service.UserLogService;
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
 */
@Slf4j
@Controller
@RequestMapping("/bbs")
public class CustomController extends BaseController {
  
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


    /**
     * 设置前台页面公用的部分代码 均设置Redis缓存
     */
    private void setCommonMessage(Model model, String loginName,Long funcId,Integer pageNum) {
        List<Func> funcList = funcService.findBbsByCreator(loginName);
        model.addAttribute("funcList", funcList);

        if(funcId!=FUNC_NULL){
          if (funcId==FUNC_FIRST&&CollectionUtils.isNotEmpty(funcList)) {
            funcId = funcList.get(0).getId();
            model.addAttribute("funcName", funcList.get(0).getFuncName());
          }else{
            model.addAttribute("funcName", funcService.getById(funcId).getFuncName());
          }
          model.addAttribute("funcId", funcId);
          PageHelper.startPage(pageNum == null ? 1 : pageNum, 12, "create_time desc");
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
        // 设置当前访问主页名
        model.addAttribute("loginName", loginName);

        model.addAttribute("curUser", ShiroUtils.getSysUser());
    }

    @GetMapping("/{loginName}.html")
    @VLog(title = "用户首页")
    public String defaultIndex(@PathVariable String loginName, Integer pageNum, Model model) {
        setCommonMessage(model, loginName,FUNC_FIRST,pageNum);
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
    
    
    @VLog(title = "分类")
    @GetMapping({"/{loginName}/func/{funcId}.html"})
    public String funcBlog(@PathVariable String loginName, @PathVariable Long funcId,
                           Integer pageNum, Model model) {
        List<Func> funcList = funcService.findBbsByCreator(loginName);
        if (CollectionUtils.isNotEmpty(funcList)) {
            if(funcList.get(0).getId().longValue()==funcId.longValue()){
              return defaultIndex(loginName, pageNum, model);
            }
        }
        setCommonMessage(model, loginName,funcId,pageNum);
        model.addAttribute("funcId", funcId);
        model.addAttribute("funcName", funcService.getById(funcId).getFuncName());
        return "front/custom/category";
    }

    
    @GetMapping("/{loginName}/index.html")
    @VLog(title = "用户首页")
    public String loginNameIndex(@PathVariable String loginName, Integer pageNum, Model model) {
        return defaultIndex(loginName, pageNum, model);
    }

    
    @GetMapping("/{loginName}/images.html")
    @VLog(title = "用户首页")
    public String images(@PathVariable String loginName, Integer pageNum, Model model) {
        setCommonMessage(model, loginName,FUNC_NULL,pageNum);
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
    public String toLogin(Model model,String toPage) {
        model.addAttribute("toPage", toPage);
        return "front/login/login";
    }


    @PostMapping("/front/login")
    @ResponseBody
    public AjaxResult frontLogin(String loginName, String password,String toPage, Model model) {
        UsernamePasswordToken token = new UsernamePasswordToken(loginName, password, false);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            if(StringUtils.isEmpty(toPage)){
              return success("/bbs/"+loginName+".html");
            }else{
              return success("/"+toPage);
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
    public String frontToReg(String toPage, Model model) {
        model.addAttribute("toPage", toPage);
        return "front/login/reg";
    }

    
    @PostMapping("/front/reg")
    @ResponseBody
    public AjaxResult frontReg(User user,String toPage, Model model) {
        try {
          String password = user.getPassword();
            user.setUserName(user.getLoginName());
            userService.regUser(user);
            if(StringUtils.isEmpty(toPage)){
              return success("/bbs/"+user.getLoginName()+".html");
            }else{
              return frontLogin(user.getLoginName(), password, toPage, model);
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
     * @param toPage
     * @param model
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
    @GetMapping("/front/comment.html")
    public String leaveComment(Model model) {
        User user = ShiroUtils.getSysUser();
        if (user == null) {
            return toLogin(model,"");
        }
        setCommonMessage(model, user.getLoginName(),FUNC_NULL,DEFAULT_PAGENUM);
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
        PageHelper.startPage(pageNum == null ? 1 : pageNum, 10, "create_time desc");
        Blog blog = new Blog();
        blog.setTitle(search_word);
        List<Blog> blogs = blogService.selectBlogList(blog);
        model.addAttribute("blogs", new PageInfo<>(blogs));
        model.addAttribute("keyWord", search_word);
        return "front/custom/search";
    }
}
