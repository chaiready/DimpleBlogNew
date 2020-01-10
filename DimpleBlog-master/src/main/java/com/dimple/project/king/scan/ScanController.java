package com.dimple.project.king.scan;

import com.dimple.common.constant.Constants;
import com.dimple.project.front.controller.BaseBlogController;
import com.dimple.project.king.userlog.service.UserLogService;
import com.dimple.project.system.carouselMap.entity.CarouselMap;
import com.dimple.project.system.carouselMap.service.CarouselMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * 建议
 * @author ls2008
 * @date 2019-12-11 14:08:40
 */
@Controller
@RequestMapping(value = "/scan")
public class ScanController extends BaseBlogController {

	@Autowired
	private UserLogService service;
	@Autowired
	CarouselMapService carouselMapService;
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
	public String index(Model model) {
		setBLogHead(Constants.ADMIN_LOGINNAME,model);
		return "king/scan/scan_index";
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
		return "king/scan/scan_goods";
	}

	@RequestMapping("/about.html")
	public String about(Model model) {
		setBLogHead(Constants.ADMIN_LOGINNAME,model);
		return "king/scan/scan_about";
	}
}