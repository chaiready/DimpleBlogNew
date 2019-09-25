package com.dimple.project.king.func.controller;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dimple.framework.aspectj.lang.annotation.Log;
import com.dimple.framework.aspectj.lang.enums.BusinessType;
import com.dimple.framework.web.controller.BaseController;
import com.dimple.framework.web.domain.AjaxResult;
import com.dimple.framework.web.domain.Ztree;
import com.dimple.project.king.func.domain.Func;
import com.dimple.project.king.func.service.IFuncService;
import com.dimple.project.system.role.domain.Role;

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

    @RequiresPermissions("king:func:view")
    @GetMapping()
    public String func() {
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
        return toAjax(funcService.deleteFuncById(funcId));
    }

    /**
     * 新增
     */
    @GetMapping("/add/{parentId}")
    public String add(@PathVariable("parentId") Long parentId, Model model) {
        Func func = null;
        if (0L != parentId) {
            func = funcService.selectFuncById(parentId);
        } else {
            func = new Func();
            func.setFuncId(0L);
            func.setFuncName("主目录");
        }
        model.addAttribute("func", func);
        return prefix + "/add";
    }

    /**
     * 新增保存菜单
     */
    @Log(title = "系统菜单", businessType = BusinessType.INSERT)
    @RequiresPermissions("king:func:add")
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Func func) {
        return toAjax(funcService.insertFunc(func));
    }

    /**
     * 修改菜单
     */
    @GetMapping("/edit/{funcId}")
    public String edit(@PathVariable("funcId") Long funcId, Model model) {
        model.addAttribute("func", funcService.selectFuncById(funcId));
        return prefix + "/edit";
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

    /**
     * 选择菜单图标
     */
    @GetMapping("/icon")
    public String icon() {
        return prefix + "/icon";
    }

    /**
     * 校验菜单名称
     */
    @PostMapping("/checkFuncNameUnique")
    @ResponseBody
    public String checkFuncNameUnique(Func func) {
        return funcService.checkFuncNameUnique(func);
    }

    /**
     * 加载角色菜单列表树
     */
    @GetMapping("/roleFuncTreeData")
    @ResponseBody
    public List<Ztree> roleFuncTreeData(Role role) {
        List<Ztree> ztrees = funcService.roleFuncTreeData(role);
        return ztrees;
    }

    /**
     * 加载所有菜单列表树
     */
    @GetMapping("/funcTreeData")
    @ResponseBody
    public List<Ztree> funcTreeData(Role role) {
        List<Ztree> ztrees = funcService.funcTreeData();
        return ztrees;
    }

    /**
     * 选择菜单树
     */
    @GetMapping("/selectFuncTree/{funcId}")
    public String selectFuncTree(@PathVariable("funcId") Long funcId, Model model) {
        model.addAttribute("func", funcService.selectFuncById(funcId));
        return prefix + "/tree";
    }
}