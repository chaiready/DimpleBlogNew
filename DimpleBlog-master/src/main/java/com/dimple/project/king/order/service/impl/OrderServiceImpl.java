package com.dimple.project.king.order.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dimple.project.king.order.domain.OrderEntity;
import com.dimple.project.king.order.service.OrderService;
import com.dimple.project.king.order.mapper.OrderMapper;


/**
 * 建议
 * @author ls2008
 * @date 2020-02-02 10:59:20
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OrderEntity> implements OrderService{

	@Autowired
	private OrderMapper mapper;
}