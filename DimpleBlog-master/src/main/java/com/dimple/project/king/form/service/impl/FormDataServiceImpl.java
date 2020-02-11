package com.dimple.project.king.form.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dimple.project.king.form.domain.FormDataEntity;
import com.dimple.project.king.form.service.FormDataService;
import com.dimple.project.king.form.mapper.FormDataMapper;


/**
 * 表单
 * @author ls2008
 * @date 2020-02-06 21:50:11
 */
@Service
public class FormDataServiceImpl extends ServiceImpl<FormDataMapper, FormDataEntity> implements FormDataService{

	@Autowired
	private FormDataMapper mapper;
}