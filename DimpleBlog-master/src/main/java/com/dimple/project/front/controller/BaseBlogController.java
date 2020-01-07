package com.dimple.project.front.controller;

import com.dimple.common.utils.security.ShiroUtils;
import com.dimple.framework.web.controller.BaseController;
import com.dimple.project.king.func.domain.Func;
import com.dimple.project.king.func.service.IFuncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import java.util.List;


public class BaseBlogController  extends BaseController {

    @Autowired
    private IFuncService funcService;

    /**
     * 博客登录页
     * @return
     */
    public String blogLoginPage(){
        return "front/login/login";
    }

    /**
     * 博客的首页
     * @return
     */
    public String blogIndex(){
        return "front/index";
    }


    public List<Func> setBLogHead(String loginName, Model model){
        List<Func> funcList = funcService.findBbsByCreator(loginName);
        model.addAttribute("funcList", funcList);
        model.addAttribute("loginName", loginName);
        model.addAttribute("curUser", ShiroUtils.getSysUser());
        return funcList;
    }
}
