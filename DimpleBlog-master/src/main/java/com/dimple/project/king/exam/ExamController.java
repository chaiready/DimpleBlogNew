package com.dimple.project.king.exam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.dimple.common.utils.DateUtils;
import com.dimple.common.utils.security.ShiroUtils;
import com.dimple.framework.aspectj.lang.annotation.Log;
import com.dimple.framework.aspectj.lang.enums.BusinessType;
import com.dimple.framework.web.controller.BaseController;
import com.dimple.framework.web.domain.AjaxResult;
import com.dimple.project.front.service.HomeService;
import com.dimple.project.king.exam.domain.Question;
import com.dimple.project.king.exam.domain.QuestionAnswer;
import com.dimple.project.king.exam.domain.QuestionExamEntity;
import com.dimple.project.king.exam.domain.QuestionFavorites;
import com.dimple.project.king.exam.domain.QuestionFolderEntity;
import com.dimple.project.king.exam.domain.QuestionOption;
import com.dimple.project.king.exam.service.QuestionAnswerService;
import com.dimple.project.king.exam.service.QuestionExamService;
import com.dimple.project.king.exam.service.QuestionFavoritesService;
import com.dimple.project.king.exam.service.QuestionFolderService;
import com.dimple.project.king.exam.service.QuestionItemService;
import com.dimple.project.king.exam.service.QuestionOptionService;
import com.dimple.project.king.exam.service.QuestionService;
import com.dimple.project.king.func.domain.Func;
import com.dimple.project.king.func.service.IFuncService;
import com.dimple.project.system.notice.domain.Notice;
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
  @Autowired
  private QuestionExamService questionExamService;
  @Autowired
  private QuestionFolderService questionFolderService;
  @Autowired
  private QuestionItemService questionItemService;

  private void setFunc(Model model, Long funcId, User user) {
    List<Func> funcList = new ArrayList<Func>();
    model.addAttribute("funcList", funcList);
    String funcName = "";
    
    Func funcVo = new Func();
    funcVo.setUrl("/kaoshi/questionFolder/1.html");
    funcVo.setFuncName("医药学");
    funcVo.setFuncId(1l);
    funcList.add(funcVo);
    if (CollectionUtils.isNotEmpty(funcList)) {
      funcId = funcId == null ? funcList.get(0).getFuncId() : funcId;
      for (Func func : funcList) {
        if (func.getFuncId().longValue() == funcId.longValue()) {
          funcName = func.getFuncName();
          break;
        }
      }
    }
    model.addAttribute("funcId", funcId);
    model.addAttribute("funcName", funcName);

    model.addAttribute("curUser", ShiroUtils.getSysUser());

    List<Notice> noticeList = new ArrayList<>();
    Notice notice = new Notice();
    notice.setNoticeTitle("欢迎进入htt://5180it.com:8080");
    noticeList.add(notice);
    model.addAttribute("notices", noticeList);
  }

  private Integer getDirectPageNum(Integer pageNum, String directPage) {
    if (!StringUtils.isEmpty(directPage)) {
      pageNum = NumberUtils.toInt(directPage, 1);// 转换失败返回默认值1
    }
    pageNum = pageNum == null ? 1 : pageNum;
    return pageNum;
  }

  private void setQuestionList(List<Question> questionList, User user) {
    if (CollectionUtils.isNotEmpty(questionList)) {
      Long[] questionIds = questionList.stream().map(Question::getId).toArray(Long[]::new);
      Long userId = user == null ? 0l : user.getUserId();
      List<QuestionOption> optionList = questionOptionService.selectByQuestionIds(questionIds);
      List<QuestionFavorites> qfList =
          questionFavoritesService.selectByQuestionIds(userId, questionIds);
      for (Question question : questionList) {
        List<QuestionOption> oList = optionList.stream()
            .filter(qo -> qo.getQuestionId().longValue() == question.getId().longValue())
            .collect(Collectors.toList());
        question.setOptionList(oList);
        for (QuestionFavorites qf : qfList) {
          if (qf.getQuestionId().longValue() == question.getId().longValue()) {
            question.setHasFavorites(1);
            break;
          }
        }
      }
    }
  }


  @GetMapping()
  public String index(Model model, @PathVariable(required = false) Long funcId, Integer pageNum,
      String directPage,Long folderId) {
    User user = ShiroUtils.getSysUser();
    PageHelper.startPage(getDirectPageNum(pageNum, directPage), 10, "id asc");
    List<QuestionFolderEntity> folderList = questionFolderService.selectList();
    model.addAttribute("folderList", new PageInfo<>(folderList));
    setFunc(model, funcId, user);
    return prefix + "/exam_index";
  }

  @GetMapping("/questionFolder/{folderId}.html")
  public String questionFolder(Model model, @PathVariable(required = false) Long funcId, Integer pageNum,
      String directPage,@PathVariable Long folderId) {
    User user = ShiroUtils.getSysUser();
    PageHelper.startPage(getDirectPageNum(pageNum, directPage), 10, "id asc");
    List<Question> questionList = questionService.selectQuestionByFolderId(folderId);
    setQuestionList(questionList, user);
    model.addAttribute("questionList", new PageInfo<>(questionList));
    setFunc(model, funcId, user);
    model.addAttribute("folderId", folderId);
    return prefix + "/exam_folder";
  }

  @GetMapping("/listFavorites/{folderId}.html")
  public String listFavorites(Model model, @PathVariable(required = false) Long funcId,@PathVariable Long folderId,
      Integer pageNum, String directPage) {
    User user = ShiroUtils.getSysUser();
    Long userId = user == null ? 0l : user.getUserId();
    PageHelper.startPage(getDirectPageNum(pageNum, directPage), 10, "id asc");
    List<Question> questionList = questionService.selectQuestionFavorites(userId,folderId);
    setQuestionList(questionList, user);
    model.addAttribute("questionList", new PageInfo<>(questionList));
    model.addAttribute("folderId", folderId);
    setFunc(model, funcId, user);
    return prefix + "/exam_favorites";
  }

  @GetMapping("/listWrong/{folderId}.html")
  public String listWrong(Model model, @PathVariable(required = false) Long funcId,@PathVariable Long folderId, Integer pageNum,
      String directPage) {
    User user = ShiroUtils.getSysUser();
    Long userId = user == null ? 0l : user.getUserId();
    PageHelper.startPage(getDirectPageNum(pageNum, directPage), 10, "id asc");
    List<Question> questionList = questionService.selectQuestionWrong(userId,folderId);
    setQuestionList(questionList, user);
    model.addAttribute("questionList", new PageInfo<>(questionList));
    model.addAttribute("folderId", folderId);
    setFunc(model, funcId, user);
    return prefix + "/exam_wrong";
  }


  @GetMapping("/listExam/{folderId}.html")
  public String listExam(Model model, @PathVariable(required = false) Long funcId,@PathVariable Long folderId,
      Integer pageNum) {
    User user = ShiroUtils.getSysUser();
    Long userId = user == null ? 0l : user.getUserId();
    PageHelper.startPage(pageNum == null ? 1 : pageNum, 10, "create_time desc");
    List<QuestionExamEntity> examList = questionExamService.pageList(userId,folderId);
    model.addAttribute("examList", new PageInfo<>(examList));
    model.addAttribute("folderId", folderId);
    setFunc(model, funcId, user);
    return prefix + "/exam_list";
  }

  @ResponseBody
  @GetMapping("/createExam")
  public AjaxResult createExam(Model model, @PathVariable(required = false) Long funcId,Long folderId,
      Integer pageNum, String directPage) {
    User user = ShiroUtils.getSysUser();
    if (user == null) {
      return AjaxResult.error("未登录");
    }
    QuestionExamEntity questionExam = new QuestionExamEntity();
    questionExam.setUserId(user.getUserId());
    questionExam.setFolderId(folderId);
    questionExam.setCreateBy(user.getLoginName());
    questionExam.setExamName("试卷" + DateUtils.getTime());
    questionExam.setCreateTime(new Date());
    Long examId = questionExamService.add(questionExam);

    AjaxResult result = AjaxResult.success("创建成功");
    result.put("examId", examId);
    result.put("folderId", folderId);
    return result;
  }


  @GetMapping("/examDetail/{folderId}/{examId}.html")
  public String examDetail(Model model, @PathVariable(required = false) Long funcId, @PathVariable Long folderId,
      Integer pageNum, String directPage, @PathVariable Long examId) {
    User user = ShiroUtils.getSysUser();
    PageHelper.startPage(getDirectPageNum(pageNum, directPage), 10, "id asc");
    List<Question> questionList = questionService.selectQuestionByFolderId(folderId);
    if (CollectionUtils.isNotEmpty(questionList)) {
      Long[] questionIds = questionList.stream().map(Question::getId).toArray(Long[]::new);
      Long userId = user == null ? 0l : user.getUserId();
      List<QuestionOption> optionList = questionOptionService.selectByQuestionIds(questionIds);
      List<QuestionFavorites> qfList =
          questionFavoritesService.selectByQuestionIds(userId, questionIds);
      List<QuestionAnswer> answerList =
          questionAnswerService.selectByQuestionIds(userId, examId, questionIds);
      for (Question question : questionList) {
        long yourOptionId = 0;
        int optionCorrect = 0;
        // 回答
        for (QuestionAnswer qa : answerList) {
          if (qa.getQuestionId().longValue() == question.getId().longValue()) {
            question.setHasAnswer(qa.getCorrect());
            question.setYouAnswer(qa.getOptionOrder());
            yourOptionId = qa.getOptionId();
            optionCorrect = qa.getCorrect();
            break;
          }
        }
        List<QuestionOption> oList = new ArrayList<>();
        for (QuestionOption qo : optionList) {
          if (qo.getQuestionId().longValue() == question.getId().longValue()) {
            if (yourOptionId == qo.getId().longValue()) {
              qo.setCorrect(optionCorrect);
            }
            oList.add(qo);
          }
        }
        question.setOptionList(oList);
        for (QuestionFavorites qf : qfList) {
          if (qf.getQuestionId().longValue() == question.getId().longValue()) {
            question.setHasFavorites(1);
            break;
          }
        }
      }
    }
    model.addAttribute("questionList", new PageInfo<>(questionList));
    setFunc(model, funcId, user);
    model.addAttribute("examId", examId);
    return prefix + "/exam_edit";
  }




  @Log(title = "添加收藏", businessType = BusinessType.INSERT)
  @PostMapping("/addFavorites")
  @ResponseBody
  public AjaxResult addFavorites(Long questionId) {
    QuestionFavorites obj = new QuestionFavorites();
    User user = ShiroUtils.getSysUser();
    if (user == null) {
      return error(0, "还没登录");
    }
    obj.setUserId(user.getUserId());
    obj.setQuestionId(questionId);
    List<QuestionFavorites> qfList =
        questionFavoritesService.selectByUidAndQid(user.getUserId(), questionId);
    AjaxResult result = null;
    if (CollectionUtils.isNotEmpty(qfList)) {
      result = AjaxResult.error("已添加收藏");
      result.put("hasFavorites", 1);// 是否收藏
      return result;
    }
    if (questionFavoritesService.insertObj(obj) > 0) {
      result = AjaxResult.success("已添加收藏");
      result.put("hasFavorites", 1);
    } else {
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
    if (user == null) {
      return error(0, "还没登录");
    }
    questionFavoritesService.delObj(user.getUserId(), questionId);
    AjaxResult result = AjaxResult.success("已取消收藏");
    result.put("hasFavorites", 0);// 是否收藏
    return result;
  }


  @PostMapping("/addAnswer")
  @ResponseBody
  public AjaxResult addAnswer(String questionOptionId, Long examId) {
    User user = ShiroUtils.getSysUser();
    if (user == null) {
      return error(0, "还没登录");
    }
    return questionAnswerService.addAnswer(ShiroUtils.getUserId(), examId, questionOptionId);
  }

}
