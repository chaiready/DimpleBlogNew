package com.dimple.project.king.form.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dimple.project.king.form.domain.FormSubEntity;
import com.dimple.project.king.form.service.FormSubService;
import com.dimple.project.king.form.mapper.FormSubMapper;


/**
 * 表单
 * @author ls2008
 * @date 2020-02-03 11:00:35
 */
@Service
public class FormSubServiceImpl extends ServiceImpl<FormSubMapper, FormSubEntity> implements FormSubService{

	@Autowired
	private FormSubMapper mapper;
}