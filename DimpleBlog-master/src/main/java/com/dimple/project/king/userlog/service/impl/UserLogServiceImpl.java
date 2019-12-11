package com.dimple.project.king.userlog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dimple.project.king.userlog.domain.UserLogEntity;
import com.dimple.project.king.userlog.service.UserLogService;
import com.dimple.project.king.userlog.mapper.UserLogMapper;


/**
 * 建议
 * @author ls2008
 * @date 2019-12-11 14:08:40
 */
@Service
public class UserLogServiceImpl extends ServiceImpl<UserLogMapper, UserLogEntity> implements UserLogService{

	@Autowired
	private UserLogMapper mapper;
}