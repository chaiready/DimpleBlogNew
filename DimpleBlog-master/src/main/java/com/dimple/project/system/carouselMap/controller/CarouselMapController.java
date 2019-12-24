package com.dimple.project.system.carouselMap.controller;

import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dimple.common.constant.Constants;
import com.dimple.common.utils.security.ShiroUtils;
import com.dimple.framework.aspectj.lang.annotation.Log;
import com.dimple.framework.aspectj.lang.enums.BusinessType;
import com.dimple.framework.web.controller.BaseController;
import com.dimple.framework.web.domain.AjaxResult;
import com.dimple.framework.web.page.TableDataInfo;
import com.dimple.project.system.carouselMap.entity.CarouselMap;
import com.dimple.project.system.carouselMap.service.CarouselMapService;
import com.dimple.project.system.user.domain.User;

/**
 * @className: BgCarouselMapController
 * @description: 轮播图设置(CarouselMap)表控制层
 * @auther: Dimple
 * @Date: 2019/4/8
 * @Version: 1.1
 */
@Controller
@RequestMapping("/system/carouselMap")
public class CarouselMapController extends BaseController {

    @Resource
    private CarouselMapService carouselMapService;

    @RequiresPermissions("system:carouselMap:view")
    @GetMapping
    public String carouselMap() {
        return "system/carouselMap/carouselMap";
    }

    @GetMapping("/list")
    @RequiresPermissions("system:carouselMap:list")
    @ResponseBody
    public TableDataInfo list(CarouselMap carouselMap) {
        startPage();
        List<CarouselMap> list = carouselMapService.selectCarouselMapList(carouselMap);
        return getDataTable(list);
    }

    @GetMapping("/add")
    public String add(Model model) {
        return "system/carouselMap/add";
    }

    @Log(title = "系统轮播图", businessType = BusinessType.INSERT)
    @RequiresPermissions("system:carouselMap:add")
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(CarouselMap carouselMap) {
        return toAjax(carouselMapService.insertCarouselMap(carouselMap));
    }

    @GetMapping("/edit/{carouselMapId}")
    public String edit(@PathVariable Integer carouselMapId, Model model) {
        model.addAttribute("carouselMap", carouselMapService.selectCarouselMapById(carouselMapId));
        return "system/carouselMap/edit";
    }

    @PutMapping("/edit")
    @RequiresPermissions("system:carouselMap:edit")
    @Log(title = "系统轮播图", businessType = BusinessType.UPDATE)
    @ResponseBody
    public AjaxResult editSave(CarouselMap carouselMap) {
        return toAjax(carouselMapService.updateCarouselMap(carouselMap));
    }

    @PutMapping("/changeDisplay/{display}")
    @RequiresPermissions("system:carouselMap:edit")
    @Log(title = "系统轮播图", businessType = BusinessType.UPDATE)
    @ResponseBody
    public AjaxResult editSave(@PathVariable String display, String carouselId) {
        return toAjax(carouselMapService.changeCarouselDisplay(carouselId,display));
    }


    @DeleteMapping("/remove")
    @Log(title = "系统轮播图", businessType = BusinessType.DELETE)
    @RequiresPermissions("system:carouselMap:remove")
    @ResponseBody
    public AjaxResult remove(Integer[] ids) {
        return toAjax(carouselMapService.deleteCarouselMapByIds(ids));
    }
    
    
    /**
     * 博客修改轮播图片
     * @param model
     * @return
     */
    @GetMapping("/blogEdit")
    public String blogEdit(Model model) {
    	User user = ShiroUtils.getSysUser();
    	// 放置轮播图
        List<CarouselMap> carouselMaps = carouselMapService.selectByCreateBy(user.getLoginName());
        int carouselMapsSize = carouselMaps.size();
        for(int i=0;i<Constants.BLOG_INDEX_PIC_COUNT-carouselMapsSize;i++){
        	 CarouselMap cm = new CarouselMap();
        	 cm.setCarouselId(0);
             cm.setTitle("");
             cm.setSubTitle("");
             cm.setImgUrl(Constants.BLOG_INDEX_PIC_URL);
             carouselMaps.add(cm);
        }
        model.addAttribute("carouselMaps", carouselMaps);
        model.addAttribute("defaultPic", Constants.BLOG_INDEX_PIC_URL);
    	return "system/carouselMap/editBlog";
    }
    
    
    @DeleteMapping("/blogEditRemove")
    @ResponseBody
    public AjaxResult blogEditRemove(Integer carouselId) {
    	CarouselMap cm = carouselMapService.selectCarouselMapById(carouselId);
    	if(cm==null){
    		return AjaxResult.error("图片不存在");
    	}
    	if(cm.getCreateBy()==null||!cm.getCreateBy().equals(ShiroUtils.getLoginName())){
    		return AjaxResult.error("不是当前博主，不能删除");
    	}
    	Integer []ids = {carouselId};
        return toAjax(carouselMapService.deleteCarouselMapByIds(ids));
    }
    
    
    @Log(title = "系统轮播图", businessType = BusinessType.INSERT)
    @PostMapping("/blogSave")
    @ResponseBody
    public AjaxResult blogSave(Integer[] carouselId,String []imgUrl) {
        return toAjax(carouselMapService.blogSave(carouselId,imgUrl));
    }
}