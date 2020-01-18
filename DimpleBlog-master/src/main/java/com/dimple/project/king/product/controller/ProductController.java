package com.dimple.project.king.product.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.dimple.framework.web.controller.BaseController;
import com.dimple.framework.web.domain.AjaxResult;
import com.dimple.framework.web.page.TableDataInfo;
import com.dimple.project.king.product.domain.ProductEntity;
import com.dimple.project.king.product.service.ProductService;

/**
 * 产品
 * 
 * @author ls2008
 * @date 2020-01-18 14:43:23
 */
@Controller
@RequestMapping(value = "/product")
public class ProductController extends BaseController {

	@Autowired
	private ProductService service;

	/**
	 * 
	 * @Title: listView @author ls2008 @date 2020-01-18 14:43:23 @param @param
	 * model @param @return 设定文件 @return String 返回类型 @throws
	 */
	@GetMapping()
	public String index(Model model) {
		return "king/product/product_list";
	}

	@ResponseBody
	@PostMapping("/list")
	public TableDataInfo list(ProductEntity entity) {
		startPage();
		List<ProductEntity> list = service.list();
		return getDataTable(list);
	}

	@GetMapping("/add")
	public String addView(Model model, ServletRequest request) {
		return "king/product/product_add";
	}

	@ResponseBody
	@PostMapping("/add")
	public AjaxResult addSave(MultipartHttpServletRequest request, ProductEntity entity) {
		List<MultipartFile> files = request.getFiles("attachment[]");
		return toAjax(service.insertWithFiles(entity, files));
	}

	@GetMapping("/edit/{id}")
	public String editView(Model model,@PathVariable Long id) {
		model.addAttribute("entity", service.getById(id));
		return "king/product/product_edit";
	}
	
	@ResponseBody
    @PutMapping("/edit")
    public AjaxResult editSave(MultipartHttpServletRequest request,ProductEntity entity) {
        List<MultipartFile> files = request.getFiles("attachment[]");
        return toAjax(service.updateWithFiles(entity,files));
    }

	@RequestMapping("/detail/{id}")
	public String detailView(Model model,@PathVariable Long id) {
		return "king/product/product_detail";
	}
	
    @DeleteMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(service.removeByIds(Arrays.asList(ids.split(","))));
    }

}