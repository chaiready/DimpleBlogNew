package com.dimple.project.king.order.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dimple.common.constant.Constants;
import com.dimple.common.utils.DateUtils;
import com.dimple.common.utils.security.ShiroUtils;
import com.dimple.framework.web.domain.AjaxResult;
import com.dimple.project.blog.blog.service.BlogService;
import com.dimple.project.front.controller.BaseBlogController;
import com.dimple.project.king.order.domain.OrderEntity;
import com.dimple.project.king.order.service.OrderService;
import com.dimple.project.system.carouselMap.entity.CarouselMap;
import com.dimple.project.system.user.domain.User;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 订单
 * @author lenovo
 *
 */
@Controller
@RequestMapping(value = "/order")
public class OrderController extends BaseBlogController {
	
	@Autowired
	private BlogService blogService;
	@Autowired
	private OrderService service;
	
	/**
	 * 
	 * @Title: listView
	 * @author ls2008
	 * @date 2019-12-11 14:08:40
	 * @param @param model
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	@RequestMapping("/index.html")
	public String index(Model model, Integer pageNum, String directPage) {
		setBLogHead(Constants.ADMIN_LOGINNAME,model);
		
		User user = ShiroUtils.getSysUser();
		if(user==null){
			return blogLoginPage();
		}
		PageHelper.startPage(changePageNum(pageNum, "") , Constants.BLOG_PAGE_SIZE);
		model.addAttribute("blogs", new PageInfo<>(blogService.selectBookmarksList(user.getUserId())));
		
		return "king/order/order_index";
	}

	@RequestMapping("/goods.html")
	public String goods(Model model) {
		setBLogHead(Constants.ADMIN_LOGINNAME,model);
		// 放置轮播图
		List<CarouselMap> carouselMaps = new ArrayList<>();
		carouselMaps.add(new CarouselMap("/img/king/goods1.png","",""));
		carouselMaps.add(new CarouselMap("/img/king/goods2.png","",""));
		carouselMaps.add(new CarouselMap("/img/king/goods3.png","",""));
		model.addAttribute("carouselMaps", carouselMaps);
		return "king/order/order_goods";
	}

	@RequestMapping("/about.html")
	public String about(Model model) {
		setBLogHead(Constants.ADMIN_LOGINNAME,model);
		return "king/order/order_about";
	}

	/**
	 * 添加订单
	 * @param obj
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/add")
	public AjaxResult add(OrderEntity obj) {
		User user = ShiroUtils.getSysUser();
//		if(user==null){
//			return AjaxResult.error("请重新登录");
//		}
		obj.setBillcode(DateUtils.dateTimeNow());
		obj.setDescription("肠粉加肉加蛋 数量:"+obj.getNum());
		if(user!=null){
			obj.setCreateBy(user.getLoginName());
		}
		obj.setCreateTime(new Date());
		service.save(obj);
		return AjaxResult.success();
	}
	
	/**
	 * 计算
	 * @param obj
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/settle")
	public AjaxResult settle(OrderEntity obj) {
		AjaxResult result =  AjaxResult.success();
		result.put("billcode", DateUtils.dateTimeNow());
		return result;
	}
	
	
	
	/**
	 * 订单列表
	 * @param model
	 * @param pageNum
	 * @param directPage
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/query")
	public AjaxResult query(Model model, Integer pageNum, String directPage) {
		setBLogHead(Constants.ADMIN_LOGINNAME,model);
		PageHelper.startPage(changePageNum(pageNum, "") , 5);
		QueryWrapper<OrderEntity> queryWrapper = new QueryWrapper<>();
		queryWrapper.orderByDesc("create_time");
		AjaxResult result =  AjaxResult.successSearch();
		result.put("data", new PageInfo<>(service.list(queryWrapper)));
		return result;
	}
}