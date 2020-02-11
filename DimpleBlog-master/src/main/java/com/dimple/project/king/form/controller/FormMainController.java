package com.dimple.project.king.form.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dimple.common.utils.DateUtils;
import com.dimple.common.utils.security.ShiroUtils;
import com.dimple.common.utils.text.Convert;
import com.dimple.framework.web.controller.BaseController;
import com.dimple.framework.web.domain.AjaxResult;
import com.dimple.framework.web.page.TableDataInfo;
import com.dimple.project.king.form.domain.FormDataEntity;
import com.dimple.project.king.form.domain.FormMainEntity;
import com.dimple.project.king.form.domain.FormSubEntity;
import com.dimple.project.king.form.service.FormDataService;
import com.dimple.project.king.form.service.FormMainService;
import com.dimple.project.king.form.service.FormSubService;
import com.dimple.project.log.operlog.domain.OperLog;

/**
 * 表单
 * 
 * @author ls2008
 * @date 2020-02-03 11:02:16
 */
@Controller
@RequestMapping(value = "/form")
public class FormMainController extends BaseController {

	private String TABLE_COLUMN_PREX = "subid_";

	@Autowired
	private FormMainService service;
	@Autowired
	private FormSubService subService;
	@Autowired
	private FormDataService dataService;

	@GetMapping()
	public String operlog() {
		return "king/form/form_main_list";
	}

	@GetMapping("/list")
	@ResponseBody
	public TableDataInfo list(OperLog operLog) {
		startPage();
		List<FormMainEntity> list = service.list();
		return getDataTable(list);
	}

	@RequestMapping("/add")
	public String addView(Model model, ServletRequest request) {
		return "king/form/form_add";
	}

	private List<FormSubEntity> getSubList(Long entityId) {
		QueryWrapper<FormSubEntity> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("mainid", entityId);
		return subService.list(queryWrapper);
	}

	private void setModel(Model model, Long entityId) {
		model.addAttribute("entity", service.getById(entityId));
		List<FormSubEntity> subList = getSubList(entityId);
		for (FormSubEntity sub : subList) {
			String colVal = sub.getColVal();
			if (StringUtils.isEmpty(colVal)) {
				continue;
			}
			List<String> colVals = JSON.parseArray(colVal, String.class);
			sub.setColVals(colVals);
		}
		model.addAttribute("subList", subList);
	}

	@RequestMapping("/edit/{entityId}")
	public String editView(Model model, @PathVariable("entityId") Long entityId) {
		setModel(model, entityId);
		return "king/form/form_edit";
	}

	@RequestMapping("/detail/{entityId}")
	public String detailView(Model model, @PathVariable("entityId") Long entityId) {
		setModel(model, entityId);
		return "king/form/form_detail";
	}

	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(FormMainEntity main, String jsonStr) {
		main.setCreateTime(new Date());
		main.setCreateBy(ShiroUtils.getSysUser() == null ? "" : ShiroUtils.getLoginName());
		return toAjax(service.addSave(main, jsonStr));
	}

	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(FormMainEntity main, String jsonStr, String[] delSubId) {
		return toAjax(service.updateSave(main, jsonStr, delSubId));
	}

	/**
	 * 保存用户填写信息
	 * 
	 * @param main
	 * @param jsonStr
	 * @param delSubId
	 * @return
	 */
	@PostMapping("/save")
	@ResponseBody
	public AjaxResult saveData(HttpServletRequest request) {
		Long mainId = Long.parseLong(request.getParameter("mainid"));
		String rowuuid = String.valueOf(System.currentTimeMillis());
		Map<String, String[]> map = request.getParameterMap();
		List<FormDataEntity> list = new ArrayList<>();
		String[] subIds = request.getParameterValues("subId");
		for (int i = 0; i < subIds.length; i++) {
			FormDataEntity entity = new FormDataEntity();
			entity.setMainid(mainId);
		}
		String prefix = "colData_";
		String loginName = ShiroUtils.getSysUser() == null ? "" : ShiroUtils.getLoginName();
		for (String key : map.keySet()) {
			System.out.println(key + ":" + map.get(key)[0]);
			if (key.startsWith(prefix)) {
				FormDataEntity entity = new FormDataEntity();
				entity.setMainid(mainId);
				entity.setSubid(Long.parseLong(key.replace(prefix, "")));
				entity.setColData(map.get(key)[0]);
				entity.setCreateTime(new Date());
				entity.setCreateBy(loginName);
				entity.setRowuuid(rowuuid);
				list.add(entity);
			}

		}
		return toAjax(service.saveData(list));
	}
	
	@DeleteMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        try {
            return toAjax(service.removeByIds(Convert.toStrList(ids)));
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }


	@GetMapping("/dataList")
	public String dataList(Long mainid, Model model) {
		model.addAttribute("mainid", mainid);

		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		obj.put("field", "id");
		obj.put("checkbox", true);
		array.add(obj);

		List<FormSubEntity> subList = getSubList(mainid);
		for (FormSubEntity sub : subList) {
			obj = new JSONObject();
			obj.put("field", TABLE_COLUMN_PREX + sub.getId());
			obj.put("title", sub.getColName());
			obj.put("align", "center");
			array.add(obj);
		}
		model.addAttribute("columns", array);
		return "king/form/form_data_list";
	}

	@RequestMapping("/queryData")
	@ResponseBody
	public TableDataInfo queryData(Long mainid) {
		// startPage();
		QueryWrapper<FormDataEntity> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("mainid", mainid);
		queryWrapper.orderByDesc("rowuuid");
		List<FormDataEntity> list = dataService.list(queryWrapper);
		JSONArray array = new JSONArray();
		if(CollectionUtils.isNotEmpty(list)){
			String rowuuid = list.get(0).getRowuuid();
			JSONObject obj = new JSONObject();
			for (FormDataEntity entity : list) {
				if (!rowuuid.equals(entity.getRowuuid())) {
					array.add(obj);
					obj = new JSONObject();
				}
				obj.put(TABLE_COLUMN_PREX + entity.getSubid(), entity.getColData());
				rowuuid = entity.getRowuuid();
			}
			array.add(obj);
		}
		return getDataTable(array);
	}

	@RequestMapping(value = "/exportQuery")
	public void exportQuery(HttpServletRequest request, HttpServletResponse response, Long mainid) {
		OutputStream os = null;
		Workbook wb = null;
		String filename = "表单记录";
		try {
			os = response.getOutputStream();// 取得输出流
			setExcelResponse(filename + DateUtils.dateTimeNow(), request, response);

			wb = new SXSSFWorkbook(100);
			changeToStatisCells(wb, filename, mainid);
			wb.write(os);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (wb != null) {
					wb.close();
				}
				if (os != null) {
					os.close();// 关闭流
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 转为导出excel @Title: changeToStatisCells @author liusheng @date 2017年8月26日
	 * 下午10:08:37 @param @param resultList @param @param wb 设定文件 @return void
	 * 返回类型 @throws
	 */
	public void changeToStatisCells(Workbook wb, String sheetName, Long mainid) {
		List<JSONObject> resultList = (List<JSONObject>) queryData(mainid).getRows();
		Row row = null;
		Cell cell = null;
		Sheet sh = null;
		sh = wb.createSheet(sheetName);
		CellStyle style = wb.createCellStyle();// 样式对象
		// 生成一个字体
		Font font = wb.createFont();
		font.setColor(IndexedColors.BLACK.index);// 字体颜色HSSFColor.BLACK.index
		font.setFontHeightInPoints((short) 12);
		font.setBold(true);// 字体增粗setBoldweight(HSSFFont.BOLDWEIGHT_BOLD)
		// 把字体应用到当前的样式
		style.setFont(font);
		style.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直
																// HSSFCellStyle.VERTICAL_CENTER
		style.setAlignment(HorizontalAlignment.CENTER);// 水平HSSFCellStyle.ALIGN_CENTER

		// sh.setColumnWidth(1, 15 * 256);//设置宽度

		row = sh.createRow(0);// 创建表头
		List<FormSubEntity> subList = getSubList(mainid);
		for (int i = 0; i < subList.size(); i++) {
			cell = row.createCell(i);
			cell.setCellStyle(style);
			cell.setCellValue(subList.get(i).getColName());
		}

		int rownum = 0;
		String studentSex = "";
		for (JSONObject obj : resultList) {
			rownum++;
			row = sh.createRow(rownum);
			for (int i = 0; i < subList.size(); i++) {
				cell = row.createCell(i);// 序号
				cell.setCellValue(obj.getString(TABLE_COLUMN_PREX + subList.get(i).getId()));
			}
		}
	}
}