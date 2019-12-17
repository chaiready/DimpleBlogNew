package com.dimple.project.king.suggest.controller;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dimple.common.utils.security.ShiroUtils;
import com.dimple.framework.web.controller.BaseController;
import com.dimple.framework.web.domain.AjaxResult;
import com.dimple.project.king.func.domain.Func;
import com.dimple.project.king.func.service.IFuncService;
import com.dimple.project.king.suggest.domain.SuggestEntity;
import com.dimple.project.king.suggest.service.SuggestService;
import com.dimple.project.system.notice.service.INoticeService;
import com.dimple.project.system.user.domain.User;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 建议
 * 
 * @author ls2008
 * @date 2019-12-10 14:52:24
 */
@Controller
@RequestMapping(value = "/king/suggest")
public class SuggestController extends BaseController {

  @Autowired
  private SuggestService service;
  @Autowired
  private IFuncService funcService;
  @Autowired
  INoticeService noticeService;

  @ResponseBody
  @RequestMapping("/add")
  public AjaxResult add(String content) {
    SuggestEntity suggest = new SuggestEntity();
    suggest.setContent(content);
    suggest.setCreateTime(new Date());
    User user = ShiroUtils.getSysUser();
    if(user!=null){
      suggest.setCreateBy(user.getLoginName());
    }
    service.save(suggest);
    return AjaxResult.success("提交成功");
  }


  @GetMapping("/toList")
  public String toList(Model model, Integer pageNum, String directPage) {
    User user = ShiroUtils.getSysUser();
    QueryWrapper<SuggestEntity> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("create_by", user.getLoginName()).orderByDesc("create_time");
    PageHelper.startPage(changePageNum(pageNum, directPage), 10);
    List<SuggestEntity> objList = service.list(queryWrapper);
    PageInfo<SuggestEntity> pageInfo = new PageInfo<>(objList);
    model.addAttribute("objList", pageInfo);
    
    List<Func> funcList = funcService.findBbsByCreator(user.getLoginName());
    model.addAttribute("funcList", funcList);
    
 // 查询通知
    model.addAttribute("notices", noticeService.selectNoticeListDisplay());
    
    return "king/suggest/suggest_list";
  }
}
