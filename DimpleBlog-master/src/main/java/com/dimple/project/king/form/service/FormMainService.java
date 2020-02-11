package com.dimple.project.king.form.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dimple.project.king.form.domain.FormDataEntity;
import com.dimple.project.king.form.domain.FormMainEntity;

/**
 * 表单
 * @author ls2008
 * @date 2020-02-03 11:02:16
 */
public interface FormMainService extends IService<FormMainEntity>{

	int addSave(FormMainEntity main, String jsonStr);

	int updateSave(FormMainEntity main, String jsonStr, String[] delSubId);

	int saveData(List<FormDataEntity> list);

}