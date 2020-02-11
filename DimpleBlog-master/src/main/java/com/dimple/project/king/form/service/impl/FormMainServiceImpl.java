package com.dimple.project.king.form.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dimple.project.king.form.domain.FormDataEntity;
import com.dimple.project.king.form.domain.FormMainEntity;
import com.dimple.project.king.form.domain.FormSubEntity;
import com.dimple.project.king.form.mapper.FormMainMapper;
import com.dimple.project.king.form.service.FormDataService;
import com.dimple.project.king.form.service.FormMainService;
import com.dimple.project.king.form.service.FormSubService;
import com.dimple.project.king.vo.FormSubVo;


/**
 * 表单
 * @author ls2008
 * @date 2020-02-03 11:02:16
 */
@Service
public class FormMainServiceImpl extends ServiceImpl<FormMainMapper, FormMainEntity> implements FormMainService{

	@Autowired
	private FormMainMapper mapper;
	@Autowired
	private FormSubService subService;
	@Autowired
	private FormDataService dataService;
	
	@Transactional
	@Override
	public int addSave(FormMainEntity main, String jsonStr) {
		save(main);
		
		List<FormSubVo> subList = JSON.parseArray(jsonStr, FormSubVo.class);
		List<FormSubEntity> subs = new ArrayList<>();
		for(FormSubVo vo : subList){
			FormSubEntity sub = new FormSubEntity();
			sub.setMainid(main.getId());
			sub.setColType(vo.getColType());
			sub.setColName(vo.getColName());
			sub.setColVal(JSON.toJSONString(vo.getColVals()));
			subs.add(sub);
		}
		if(CollectionUtils.isNotEmpty(subs)){
			subService.saveBatch(subs);
		}
		return 1;
	}

	@Transactional
	@Override
	public int updateSave(FormMainEntity main, String jsonStr, String[] delSubId) {
		updateById(main);
		if(delSubId!=null){
			for(String sid:delSubId){
				subService.removeById(Long.parseLong(sid));
			}
		}
		List<FormSubVo> subList = JSON.parseArray(jsonStr, FormSubVo.class);
		List<FormSubEntity> addList = new ArrayList<>();
		List<FormSubEntity> updateList = new ArrayList<>();
		for(FormSubVo vo : subList){
			FormSubEntity sub = new FormSubEntity();
			sub.setId(vo.getSubId());
			sub.setMainid(main.getId());
			sub.setColType(vo.getColType());
			sub.setColName(vo.getColName());
			sub.setColVal(JSON.toJSONString(vo.getColVals()));
			if(sub.getId()!=null&&sub.getId()>0){
				updateList.add(sub);
			}else{
				addList.add(sub);
			}
		}
		if(CollectionUtils.isNotEmpty(updateList)){
			subService.updateBatchById(updateList);
		}
		if(CollectionUtils.isNotEmpty(addList)){
			subService.saveBatch(addList);
		}
		return 1;
	}

	@Override
	public int saveData(List<FormDataEntity> list) {
		dataService.saveBatch(list);
		return 1;
	}
}