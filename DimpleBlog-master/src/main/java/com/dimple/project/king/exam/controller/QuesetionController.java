package com.dimple.project.king.exam.controller;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dimple.common.utils.text.Convert;
import com.dimple.framework.aspectj.lang.annotation.Log;
import com.dimple.framework.aspectj.lang.enums.BusinessType;
import com.dimple.framework.web.controller.BaseController;
import com.dimple.framework.web.domain.AjaxResult;
import com.dimple.framework.web.page.TableDataInfo;
import com.dimple.project.king.exam.domain.Question;
import com.dimple.project.king.exam.service.QuestionOptionService;
import com.dimple.project.king.exam.service.QuestionService;

/**
 * @className: Controller
 * @description: 问题 信息操作处理
 * @auther: Dimple
 * @Date: 2019/3/13
 * @Version: 1.1
 */
@Controller
@RequestMapping("/king/question")
public class QuesetionController extends BaseController {
	
    private String prefix = "king/question";

    @Autowired
    private QuestionService questionService;
    @Autowired
    QuestionOptionService questionOptionService;
    

    @RequiresPermissions("king:question:view")
    @GetMapping()
    public String question() {
        return prefix + "/question";
    }

    /**
     * 查询问题列表
     */
    @RequiresPermissions("king:question:list")
    @GetMapping("/list")
    @ResponseBody
    public TableDataInfo list(Question question) {
        startPage();
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(question.getQuestionType())){
        	queryWrapper.eq("question_type", question.getQuestionType());
        }
        if(!StringUtils.isEmpty(question.getContent())){
        	queryWrapper.like("content", question.getContent());
        }
        List<Question> list = questionService.list(queryWrapper);
        return getDataTable(list);
    }

    /**
     * 新增问题
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存问题
     */
    @RequiresPermissions("king:question:add")
    @Log(title = "问题", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Question question,String []optionVal,Integer []optionAnswer) {
        return toAjax(questionService.saveQuestionOption(question,optionVal,optionAnswer));
    }

    /**
     * 修改问题
     */
    @GetMapping("/edit/{questionId}")
    public String edit(@PathVariable("questionId") Long questionId, Model model) {
        model.addAttribute("question", questionService.getById(questionId));
        model.addAttribute("optionList", questionOptionService.selectByQuestionIds(new Long[]{questionId}));
        return prefix + "/edit";
    }

    /**
     * 修改保存问题
     */
    @RequiresPermissions("king:question:edit")
    @Log(title = "问题", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Question question, Long[] optionId,String []optionVal,Integer []optionAnswer,Long []delOptinId) {
        return toAjax(questionService.updateQuestionOption(question,optionId,optionVal,optionAnswer,delOptinId));
    }

    /**
     * 删除问题
     */
    @RequiresPermissions("king:question:remove")
    @Log(title = "问题", businessType = BusinessType.DELETE)
    @DeleteMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(questionService.removeByIds(Convert.toStrList(ids)));
    }

}
