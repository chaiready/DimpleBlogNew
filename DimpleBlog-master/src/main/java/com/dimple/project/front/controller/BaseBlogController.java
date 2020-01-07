package com.dimple.project.front.controller;

import com.dimple.framework.web.controller.BaseController;


public class BaseBlogController  extends BaseController {

    public String blogLoginPage(){
        return "front/login/login";
    }
}
