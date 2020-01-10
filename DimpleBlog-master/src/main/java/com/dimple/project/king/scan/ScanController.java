package com.dimple.project.king.scan;

import com.dimple.common.constant.Constants;
import com.dimple.common.utils.QrCodeUtils;
import com.dimple.project.front.controller.BaseBlogController;
import com.dimple.project.king.userlog.service.UserLogService;
import com.dimple.project.system.carouselMap.entity.CarouselMap;
import com.dimple.project.system.carouselMap.service.CarouselMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
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



	@RequestMapping(value = "/image", produces = MediaType.IMAGE_PNG_VALUE)
	public ResponseEntity<byte[]> getImage() throws Exception{
		ByteArrayOutputStream os = null;
		os = new ByteArrayOutputStream();
		QrCodeUtils.encode("http://5180it:8080", null, os, true);

		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);
		return new ResponseEntity<>(os.toByteArray(), headers, HttpStatus.OK);
	}

	public static byte[] fileToByte(File img) throws Exception {
		byte[] bytes = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			BufferedImage bi;
			bi = ImageIO.read(img);
			ImageIO.write(bi, "png", baos);
			bytes = baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			baos.close();
		}
		return bytes;
	}

	@RequestMapping(value = "/get",produces = MediaType.IMAGE_JPEG_VALUE)
	@ResponseBody
	public BufferedImage get(HttpServletRequest request, HttpServletResponse response) throws Exception {
		StringBuffer url = request.getRequestURL();
		// 域名
		String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).append("/").toString();

		// 再加上请求链接
		String requestUrl = tempContextUrl;
		System.out.println(requestUrl);
		return  QrCodeUtils.createImage(requestUrl, null, true);
	}
}