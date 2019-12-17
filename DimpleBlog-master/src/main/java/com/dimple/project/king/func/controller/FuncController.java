package com.dimple.project.king.func.controller;

import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dimple.common.constant.Constants;
import com.dimple.common.utils.security.ShiroUtils;
import com.dimple.framework.aspectj.lang.annotation.Log;
import com.dimple.framework.aspectj.lang.enums.BusinessType;
import com.dimple.framework.web.controller.BaseController;
import com.dimple.framework.web.domain.AjaxResult;
import com.dimple.project.front.service.HomeService;
import com.dimple.project.king.func.domain.Func;
import com.dimple.project.king.func.domain.FuncBlogEntity;
import com.dimple.project.king.func.service.FuncBlogService;
import com.dimple.project.king.func.service.IFuncService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * @className: FuncController
 * @description: 菜单信息
 * @auther: Dimple
 * @Date: 2019/3/13
 * @Version: 1.1
 */
@Controller
@RequestMapping("/king/func")
public class FuncController extends BaseController {

  private String prefix = "king/func";

  @Autowired
  private IFuncService funcService;
  @Autowired
  HomeService homeService;
  @Autowired
  private FuncBlogService funcBlogService;

  @RequiresPermissions("king:func:view")
  @GetMapping({"", "/{funcId}.html"})
  public String func(Model model, @PathVariable(required = false) Long funcId, Integer pageNum) {
    String loginName = ShiroUtils.getSysUser().getLoginName();
    List<Func> funcList = funcService.findBbsByCreator(loginName);
    model.addAttribute("funcList", funcList);

    String funcName = "";
    if (CollectionUtils.isNotEmpty(funcList)) {
      if (funcId == null) {
        funcId = funcList.get(0).getId();

      }
      for (Func func : funcList) {
        if (func.getId().longValue() == funcId.longValue()) {
          funcName = func.getFuncName();
        }
      }
    }
    PageHelper.startPage(pageNum == null ? 1 : pageNum, 10, "create_time desc");
    model.addAttribute("blogs", new PageInfo<>(homeService.selectBlogListByFuncId(funcId)));
    model.addAttribute("funcId", funcId);
    model.addAttribute("funcName", funcName);
    return prefix + "/func";
  }


  @RequiresPermissions("king:func:list")
  @GetMapping("/list")
  @ResponseBody
  public List<Func> list(Func func) {
    List<Func> funcList = funcService.selectFuncList(func);
    return funcList;
  }

  /**
   * 删除菜单
   */
  @Log(title = "系统菜单", businessType = BusinessType.DELETE)
  @RequiresPermissions("king:func:remove")
  @GetMapping("/remove/{funcId}")
  @ResponseBody
  public AjaxResult remove(@PathVariable("funcId") Long funcId) {
    if (funcService.selectCountFuncByParentId(funcId) > 0) {
      return error(1, "存在子菜单,不允许删除");
    }
    QueryWrapper<FuncBlogEntity> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("func_id", funcId);
    if(funcBlogService.count(queryWrapper)>0){
      return error("存在博客,不允许删除");
    }
    return toAjax(funcService.removeById(funcId));
  }

  /**
   * 新增
   */
  @GetMapping("/add/{parentId}")
  public String add(@PathVariable("parentId") Long parentId, Model model) {
    Func func = null;
    if (0L != parentId) {
      func = funcService.getById(parentId);
    } else {
      func = new Func();
      func.setId(0L);
      func.setFuncName("主目录");
    }
    model.addAttribute("func", func);
    return prefix + "/add";
  }

  @GetMapping("/onBlogAdd")
  public String onBlogAdd(Model model) {
    Func func = new Func();
    func.setId(0L);
    func.setFuncName("主目录");
    model.addAttribute("func", func);
    return prefix + "/blog_add";
  }

  /**
   * 新增保存菜单
   */
  @Log(title = "系统菜单", businessType = BusinessType.INSERT)
  @RequiresPermissions("king:func:add")
  @PostMapping("/add")
  @ResponseBody
  public AjaxResult addSave(Func func) {
    QueryWrapper<Func> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("create_by", ShiroUtils.getLoginName());
    if(funcService.count(queryWrapper)>=Constants.BLOG_FUNC_COUNT){
      return AjaxResult.error("菜单最多只能有"+Constants.BLOG_FUNC_COUNT+"个");
    }
    return toAjax(funcService.insertFunc(func));
  }

  /**
   * 修改菜单
   */
  @GetMapping("/edit/{funcId}")
  public String edit(@PathVariable("funcId") Long funcId, Model model) {
    model.addAttribute("func", funcService.getById(funcId));
    return prefix + "/blog_edit";
  }

  /**
   * 修改保存菜单
   */
  @Log(title = "系统菜单", businessType = BusinessType.UPDATE)
  @RequiresPermissions("king:func:edit")
  @PostMapping("/edit")
  @ResponseBody
  public AjaxResult editSave(Func func) {
    return toAjax(funcService.updateFunc(func));
  }
  
  
  @Log(title = "系统菜单", businessType = BusinessType.UPDATE)
  @RequiresPermissions("king:func:tofirst")
  @PostMapping("/toFirst/{funcId}")
  @ResponseBody
  public AjaxResult toFirst(@PathVariable("funcId") Long funcId) {
    return toAjax(funcService.toFirst(funcId));
  }

}
