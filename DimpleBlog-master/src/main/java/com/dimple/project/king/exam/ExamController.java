package com.dimple.project.king.exam;

import java.util.ArrayList;
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

import com.alibaba.fastjson.JSON;
import com.dimple.framework.aspectj.lang.annotation.Log;
import com.dimple.framework.aspectj.lang.enums.BusinessType;
import com.dimple.framework.web.controller.BaseController;
import com.dimple.framework.web.domain.AjaxResult;
import com.dimple.framework.web.domain.Ztree;
import com.dimple.project.front.service.HomeService;
import com.dimple.project.king.exam.domain.Question;
import com.dimple.project.king.exam.domain.QuestionOption;
import com.dimple.project.king.func.domain.Func;
import com.dimple.project.king.func.service.IFuncService;
import com.dimple.project.system.role.domain.Role;
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
@RequestMapping("/kaoshi")
public class ExamController extends BaseController {
	
    private String prefix = "king/exam";

    @Autowired
    private IFuncService funcService;
    @Autowired
    HomeService homeService;

    @Autowired
    QuestionService questionService;
    @Autowired
    QuestionOptionService questionOptionService;
    
    @GetMapping()
    public String func(Model model,@PathVariable(required = false) Long funcId,Integer pageNum) {
    	
//    	questionService.insertObj(null);
    	
    	PageHelper.startPage(pageNum == null ? 1 : pageNum, 10, "id asc");
    	
    	List<Question>  questionList = questionService.selectQuestion();
    	if(CollectionUtils.isNotEmpty(questionList)){
    		Long[] questionIds = new Long [questionList.size()];
    		for(int i=0;i<questionList.size();i++){
    			questionIds[i] = questionList.get(i).getId();
    		}
    		List<QuestionOption> optionList = questionOptionService.selectByQuestionIds(questionIds);
    		for(Question question:questionList){
    			List<QuestionOption>  oList = new ArrayList<>();
    			for(QuestionOption qo:optionList){
    				if(qo.getQuestionId().longValue()==question.getId().longValue()){
    					oList.add(qo);
    				}
    			}
    			question.setOptionList(oList);
    		}
    	}
        model.addAttribute("questionList", new PageInfo<>(questionList));
//    	
    	
        List<Func> funcList = new ArrayList<Func>();
        model.addAttribute("funcList", funcList);

        String funcName = "";
        if(CollectionUtils.isNotEmpty(funcList)){
            if(funcId==null){
                funcId = funcList.get(0).getFuncId();

            }
            for(Func func:funcList){
                if(func.getFuncId().longValue()==funcId.longValue()){
                    funcName = func.getFuncName();
                }
            }
        }
        PageHelper.startPage(pageNum == null ? 1 : pageNum, 10, "create_time desc");
        model.addAttribute("blogs", new PageInfo<>(new ArrayList<>()));
        model.addAttribute("funcId", funcId);
        model.addAttribute("funcName", funcName);
        return prefix + "/exam";
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
