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
import com.dimple.common.utils.security.ShiroUtils;
import com.dimple.framework.aspectj.lang.annotation.Log;
import com.dimple.framework.aspectj.lang.enums.BusinessType;
import com.dimple.framework.web.controller.BaseController;
import com.dimple.framework.web.domain.AjaxResult;
import com.dimple.framework.web.domain.Ztree;
import com.dimple.project.front.service.HomeService;
import com.dimple.project.king.exam.domain.Question;
import com.dimple.project.king.exam.domain.QuestionAnswer;
import com.dimple.project.king.exam.domain.QuestionFavorites;
import com.dimple.project.king.exam.domain.QuestionOption;
import com.dimple.project.king.exam.service.QuestionAnswerService;
import com.dimple.project.king.exam.service.QuestionFavoritesService;
import com.dimple.project.king.exam.service.QuestionOptionService;
import com.dimple.project.king.exam.service.QuestionService;
import com.dimple.project.king.func.domain.Func;
import com.dimple.project.king.func.service.IFuncService;
import com.dimple.project.system.role.domain.Role;
import com.dimple.project.system.user.domain.User;
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
    @Autowired
    private QuestionFavoritesService questionFavoritesService;
    @Autowired
    private QuestionAnswerService questionAnswerService;
    
    private void setFunc(Model model,Long funcId) {
      List<Func> funcList = new ArrayList<Func>();
      model.addAttribute("funcList", funcList);

      String funcName = "";
      if(CollectionUtils.isNotEmpty(funcList)){
          funcId = funcId==null?funcList.get(0).getFuncId():funcId;
          for(Func func:funcList){
              if(func.getFuncId().longValue()==funcId.longValue()){
                  funcName = func.getFuncName();
                  break;
              }
          }
      }
      model.addAttribute("funcId", funcId);
      model.addAttribute("funcName", funcName);
    }
    
    @GetMapping()
    public String list(Model model,@PathVariable(required = false) Long funcId,Integer pageNum) {
        User user = ShiroUtils.getSysUser();
    	PageHelper.startPage(pageNum == null ? 1 : pageNum, 10, "id asc");
    	List<Question>  questionList = questionService.selectQuestion();
    	if(CollectionUtils.isNotEmpty(questionList)){
    		Long[] questionIds = new Long [questionList.size()];
    		for(int i=0;i<questionList.size();i++){
    			questionIds[i] = questionList.get(i).getId();
    		}
    		Long userId = user==null?0l:user.getUserId();
    		List<QuestionOption> optionList = questionOptionService.selectByQuestionIds(questionIds);
            List<QuestionFavorites> qfList = questionFavoritesService.selectByQuestionIds(userId, questionIds);
            List<QuestionAnswer> answerList = questionAnswerService.selectByQuestionIds(userId, questionIds);
    		for(Question question:questionList){
    		    long yourOptionId = 0;
    		    int optionCorrect = 0 ;
    		    //回答
      		    for(QuestionAnswer qa:answerList){
                  if(qa.getQuestionId().longValue()==question.getId().longValue()){
                    question.setHasAnswer(qa.getCorrect());
                    question.setYouAnswer(qa.getOptionOrder());
                    yourOptionId = qa.getOptionId();
                    optionCorrect = qa.getCorrect();
                    break;
                  }
                }
    			List<QuestionOption>  oList = new ArrayList<>();
    			for(QuestionOption qo:optionList){
    				if(qo.getQuestionId().longValue()==question.getId().longValue()){
    				    if(yourOptionId == qo.getId().longValue()){
    				      qo.setCorrect(optionCorrect);
    				    }
    					oList.add(qo);
    				}
    			}
    			question.setOptionList(oList);
    			for(QuestionFavorites qf:qfList){
    			  if(qf.getQuestionId().longValue()==question.getId().longValue()){
    			    question.setHasFavorites(1);
    			    break;
                  }
                }
    		}
    	}
        model.addAttribute("questionList", new PageInfo<>(questionList));
        model.addAttribute("curUser", ShiroUtils.getSysUser());
        setFunc(model, funcId);
        return prefix + "/exam";
    }
    
    @GetMapping("/listFavorites")
    public String listFavorites(Model model,@PathVariable(required = false) Long funcId,Integer pageNum) {
        User user = ShiroUtils.getSysUser();
        Long userId = user==null?0l:user.getUserId();
        PageHelper.startPage(pageNum == null ? 1 : pageNum, 10, "id asc");
        List<Question>  questionList = questionService.selectQuestionFavorites(userId);
        if(CollectionUtils.isNotEmpty(questionList)){
            Long[] questionIds = new Long [questionList.size()];
            for(int i=0;i<questionList.size();i++){
                questionIds[i] = questionList.get(i).getId();
            }
            List<QuestionOption> optionList = questionOptionService.selectByQuestionIds(questionIds);
            List<QuestionFavorites> qfList = questionFavoritesService.selectByQuestionIds(userId, questionIds);
            for(Question question:questionList){
                List<QuestionOption>  oList = new ArrayList<>();
                for(QuestionOption qo:optionList){
                    if(qo.getQuestionId().longValue()==question.getId().longValue()){
                        oList.add(qo);
                    }
                }
                question.setOptionList(oList);
                for(QuestionFavorites qf:qfList){
                  if(qf.getQuestionId().longValue()==question.getId().longValue()){
                    question.setHasFavorites(1);
                    break;
                  }
                }
            }
        }
        model.addAttribute("questionList", new PageInfo<>(questionList));
        model.addAttribute("curUser", ShiroUtils.getSysUser());
        setFunc(model, funcId);
        return prefix + "/exam_favorites";
    }

    

    @Log(title = "添加收藏", businessType = BusinessType.INSERT)
    @PostMapping("/addFavorites")
    @ResponseBody
    public AjaxResult addFavorites(Long questionId) {
      QuestionFavorites obj = new QuestionFavorites();
      User user = ShiroUtils.getSysUser();
      if(user==null){
        return error(0, "还没登录");
      }
      obj.setUserId(user.getUserId());
      obj.setQuestionId(questionId);
      List<QuestionFavorites>  qfList = questionFavoritesService.selectByUidAndQid(user.getUserId(), questionId);
      AjaxResult result = null;
      if(CollectionUtils.isNotEmpty(qfList)){
        result = AjaxResult.error("已添加收藏");
        result.put("hasFavorites", 1);//是否收藏
        return result;
      }
      if(questionFavoritesService.insertObj(obj)>0){
        result = AjaxResult.success("已添加收藏");
        result.put("hasFavorites", 1);
      }else{
        result = AjaxResult.error();
        result.put("hasFavorites", 0);
      }
      return result;
    }
    
    
    @Log(title = "删除收藏", businessType = BusinessType.INSERT)
    @PostMapping("/delFavorites")
    @ResponseBody
    public AjaxResult delFavorites(Long questionId) {
      User user = ShiroUtils.getSysUser();
      if(user==null){
        return error(0, "还没登录");
      }
      questionFavoritesService.delObj(user.getUserId(), questionId);
      AjaxResult result = AjaxResult.success("已取消收藏");
      result.put("hasFavorites", 0);//是否收藏
      return result;
    }
    
    @PostMapping("/addAnswer")
    @ResponseBody
    public AjaxResult addAnswer(String questionOptionId) {
      User user = ShiroUtils.getSysUser();
      if(user==null){
        return error(0, "还没登录");
      }
      return questionAnswerService.addAnswer(ShiroUtils.getUserId(),questionOptionId);
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
