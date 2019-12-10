package com.dimple.project.king.suggest.controller;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.dimple.common.utils.security.ShiroUtils;
import com.dimple.framework.web.controller.BaseController;
import com.dimple.framework.web.domain.AjaxResult;
import com.dimple.project.king.suggest.domain.SuggestEntity;
import com.dimple.project.king.suggest.service.SuggestService;
import com.dimple.project.system.user.domain.User;

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
}
