package com.dimple.project.coding;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.ibatis.type.JdbcType;
import com.dimple.common.utils.StringUtils;


/**
 * 
 * @author liusheng
 *
 */
public class CodeGeneratorUtils {

  // 公共部分
  public static final String RT_1 = "\r\n";
  public static final String RT_2 = RT_1 + RT_1;
  public static final String RT_1_SEMICOLON = ";\r\n";
  public static final String RT_2_SEMICOLON = ";"+RT_1 + RT_1;
  public static final String TAB_1 = "\t";
  public static final String TAB_2 = "\t\t";
  public static final String BLANK_1 = " ";
  public static final String BLANK_4 = "    ";
  public static final String BLANK_8 = BLANK_4 + BLANK_4;

  // 文件 地址
  private static final String savePath = "D:\\codes\\";
  
  private static String domainFolder = "domain";
  private static String mapperFolder = "mapper";
  private static String serviceFolder = "service";
  private static String controllerFolder = "controller";
  
  private static String javaFile = ".java";


  public static void main(String[] args) throws Exception {
    EntityInfo info = new EntityInfo();
    info.setEntityName("QuestionItemEntity");
    info.setExtendEntityName("BaseEntity");
    info.setPackagePath("com.dimple.project.king.exam");
    info.setTitle("问题集");
    info.setAuthor("ls2008");
    info.setReqMappingPath("/ver/auditCols");
    info.setJspPath("modules/ver/auditcols");// jsp路径

    // 生成实体==================================================================================
    List<ColInfo> list = new ArrayList<ColInfo>();
    ColInfo col = new ColInfo();
    col.setColName("folderId");
    col.setColType("Long");
    col.setColLenth(50);
    col.setColAnno("问题夹id");
    col.setEleType(ColInfo.EleType.TEXT);
    list.add(col);
    col = new ColInfo();
    col.setColName("questionId");
    col.setColType("Long");
    col.setColLenth(11);
    col.setColAnno("用户id");
    col.setEleType(ColInfo.EleType.TEXT);
    list.add(col);
    // 生成实体==================================================================================

    String entityName = getUppercaseChar(info.getEntityName());
    entityName = entityName.contains("Entity") ? entityName : (entityName += "Entity");
    info.setEntityName(entityName);
    info.setEntityShortName(info.getEntityName().replace("Entity", ""));
    
    boolean hasId = false;
    for (ColInfo c : list) {
      if (c.getColName().equals("id")) {
        hasId = true;
      }
      if(StringUtils.isEmpty(c.getColDbName())){
        c.setColDbName(underline(new StringBuffer(c.getColName())).toString());
      }
    }
    if (!hasId) {
      List<ColInfo> newList = new ArrayList<>();
      ColInfo c = new ColInfo();
      c.setColName("id");
      c.setColType("Long");
      c.setColAnno("表id");
      newList.add(c);
      newList.addAll(list);
      list = newList;
    }

    createEntity(info, list);
    createMapper(info);
    createMapperXml(info, list);
    createService(info);
    createServiceImpl(info);
    createController(info);
     
    
    // createJspList(info,list);
    // createJspEdit(info,list);
    // createJspDetail(info,list);
  }


  public static void createEntity(EntityInfo info, List<ColInfo> list) throws IOException {
    String fileName = savePath + info.getEntityName() + javaFile;
    File file = createFile(fileName);
    FileWriter fw = new FileWriter(file);
    StringBuilder sb = new StringBuilder();
    String entityPath = info.getPackagePath()+"."+domainFolder;
    info.setEntityPath(entityPath);
    sb.append("package ").append(entityPath).append(RT_2_SEMICOLON);

    // 判断需要import的类 start
    boolean hasDate = false;
    for (ColInfo col : list) {
      if (col.getColType().equals("Date")) {
        hasDate = true;
      }
    }

    if (hasDate) {
      sb.append("import java.util.Date;").append(RT_1);
      sb.append("import com.fasterxml.jackson.annotation.JsonFormat;").append(RT_1);
    }
    sb.append("import lombok.ToString;").append(RT_1);
    sb.append("import com.dimple.framework.web.domain.SuperEntity;").append(RT_1);
    sb.append("import org.springframework.data.annotation.Id;").append(RT_1);
    sb.append("import com.baomidou.mybatisplus.annotation.IdType;").append(RT_1);
    sb.append("import com.baomidou.mybatisplus.annotation.TableId;").append(RT_1);
    sb.append("import com.baomidou.mybatisplus.annotation.TableName;").append(RT_1);
    // sb.append("import com.baomidou.mybatisplus.annotation.TableField;").append(RT_1);


    // 判断需要import的类 end

    appendFileAnno(info, sb);// 类备注

    sb.append("@ToString").append(RT_1);
    sb.append("@TableName(value = \"bg" + underline(new StringBuffer(info.getEntityShortName())).toString()).append("\")").append(RT_1);
    sb.append("public class " + info.getEntityName() + " extends SuperEntity {" + RT_2);
    sb.append("	private static final long serialVersionUID = 1L;" + RT_2);

    // 生成变量
    for (ColInfo col : list) {
      if (col.getColType().equals("Date")) {
        sb.append(TAB_1)
            .append("@JsonFormat(pattern = \"yyyy-MM-dd HH:mm:ss\", timezone = \"GMT+08:00\")")
            .append(RT_1);
      }
      if (col.getColName().equals("id")) {
        sb.append(TAB_1).append("@Id").append(RT_1);
        sb.append(TAB_1).append("@TableId(value = \"id\", type = IdType.AUTO)").append(RT_1);
      }
      sb.append(TAB_1).append("private ").append(col.getColType()).append(BLANK_1)
          .append(col.getColName()).append(";//").append(col.getColAnno()).append(RT_2);
    }

    // 生成getter和setter方法
    for (ColInfo col : list) {
      sb.append("	public ").append(col.getColType()).append(" get")
          .append(getUppercaseChar(col.getColName())).append("() {").append(RT_1);
      sb.append(TAB_2).append("return ").append(col.getColName()).append(";").append(RT_1)
          .append("	}").append(RT_2);
      sb.append("	public void set").append(getUppercaseChar(col.getColName())).append("(")
          .append(col.getColType()).append(BLANK_1).append(col.getColName()).append(") {")
          .append(RT_1);
      sb.append(TAB_2).append("this.").append(col.getColName()).append(" = ")
          .append(col.getColName()).append(";").append(RT_1).append("	}").append(RT_2);
    }
    sb.append("}");
    fw.write(sb.toString());
    fw.flush();
    fw.close();
    showInfo(fileName);
  }


  /**
   * 创建bean的Dao<br>
   * 
   * @param c
   * @throws Exception
   */
  public static void createMapper(EntityInfo info) throws Exception {
    String daoName = info.getEntityShortName()+"Mapper";

    String fileName = savePath + daoName + javaFile;
    File file = createFile(fileName);
    FileWriter fw = new FileWriter(file);

    StringBuilder sb = new StringBuilder();
    String mapperPath = info.getPackagePath()+"."+ mapperFolder;
    info.setMapperName(daoName);
    info.setMapperPath(mapperPath);
    
    sb.append("package " + mapperPath + ";" + RT_2);

    sb.append("import com.baomidou.mybatisplus.core.mapper.BaseMapper;").append(RT_1);
    sb.append("import ").append(info.getEntityPath()).append(".").append(info.getEntityName()).append(RT_1_SEMICOLON);

    appendFileAnno(info, sb);// 类备注
    sb.append("public interface ").append(daoName).append(" extends BaseMapper<").append(info.getEntityName()).append("> {").append(RT_2);
    sb.append( "}");
    fw.write(sb.toString());
    fw.flush();
    fw.close();
    showInfo(fileName);
  }


  public static void createMapperXml(EntityInfo info, List<ColInfo> list) throws Exception {
    String fileName = savePath + info.getMapperName() + ".xml";
    File file = createFile(fileName);
    FileWriter fw = new FileWriter(file);

    StringBuilder sb = new StringBuilder();
    
    
    sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>").append(RT_1);
    sb.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >").append(RT_1);
    sb.append("<mapper namespace=\"").append(info.getMapperPath()).append(".").append(info.getMapperName()).append("\">").append(RT_1);
    
    sb.append(TAB_1).append("<resultMap id=\"BaseResultMap\" type=\"").append(info.getEntityPath()).append(".").append(info.getEntityName()).append("\">").append(RT_1);
    
    for (ColInfo col : list) {
      if(col.getColName().equals("id")){
        sb.append(TAB_2).append("<id column=\"id\" property=\"id\" jdbcType=\"BIGINT\"/>").append(RT_1);
      }else{
        sb.append(TAB_2).append("<result column=\"").append(col.getColDbName()).append("\" property=\"");
        sb.append(col.getColName()).append("\" jdbcType=\"");
        if (col.getColType().equals("Date")) {
          sb.append(JdbcType.TIMESTAMP.name());
        }else if(col.getColType().equals("Long")){
          sb.append(JdbcType.BIGINT.name());
        }else if(col.getColType().equals("Integer")){
          sb.append(JdbcType.INTEGER.name());
        }else if(col.getColType().equals("String")){
          sb.append(JdbcType.VARCHAR.name());
        }
        sb.append("\"/>").append(RT_1);
      }
    }  
    sb.append(TAB_2).append("<result column=\"create_by\" property=\"optionOrder\" jdbcType=\"VARCHAR\"/>").append(RT_1);
    sb.append(TAB_2).append("<result column=\"create_time\" property=\"createTime\" jdbcType=\"TIMESTAMP\"/>").append(RT_1);
    sb.append(TAB_2).append("<result column=\"update_by\" property=\"updateBy\" jdbcType=\"VARCHAR\"/>").append(RT_1);
    sb.append(TAB_2).append("<result column=\"update_time\" property=\"updateTime\" jdbcType=\"TIMESTAMP\"/>").append(RT_1);
    
    sb.append(TAB_1).append("</resultMap>").append(RT_1);
    sb.append("</mapper>");
    fw.write(sb.toString());
    fw.flush();
    fw.close();
    showInfo(fileName);
  }


  /**
   * 创建bean的service
   * 
   * @param c
   * @throws Exception
   */
  public static void createService(EntityInfo info) throws Exception {
    String serviceName = info.getEntityShortName()+"Service";
    info.setServiceName(serviceName);
    String fileName = savePath + serviceName + javaFile;
    File file = createFile(fileName);
    FileWriter fw = new FileWriter(file);

    StringBuilder sb = new StringBuilder();
    String servicePath = info.getPackagePath()+"."+serviceFolder;
    info.setServicePath(servicePath);
    sb.append("package ").append(servicePath).append(RT_2_SEMICOLON);
    sb.append("import com.baomidou.mybatisplus.extension.service.IService").append(RT_1_SEMICOLON);
    sb.append("import ").append(info.getEntityPath()).append(".").append(info.getEntityName()).append(RT_1_SEMICOLON);

    appendFileAnno(info, sb);// 类备注
    sb.append("public interface ").append(serviceName).append(" extends IService<").append(info.getEntityName()).append(">");
    sb.append("{").append(RT_2);
    sb.append("}");

    fw.write(sb.toString());
    fw.flush();
    fw.close();
    showInfo(fileName);
  }
  
  public static void createServiceImpl(EntityInfo info) throws Exception {
    String serviceImplName = info.getServiceName()+"Impl";

    info.setServiceImplName(serviceImplName);
    String fileName = savePath + serviceImplName + javaFile;
    File file = createFile(fileName);
    FileWriter fw = new FileWriter(file);

    StringBuilder sb = new StringBuilder();
    String serviceImplPath = info.getServicePath()+".impl" ;
    info.setServiceImplPath(serviceImplPath);
    sb.append("package ").append(serviceImplPath).append(RT_2_SEMICOLON);
    sb.append("import org.springframework.beans.factory.annotation.Autowired;").append(RT_1);
    sb.append("import org.springframework.stereotype.Service;").append(RT_1);
    sb.append("import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl").append(RT_1_SEMICOLON);
    sb.append("import ").append(info.getEntityPath()).append(".").append(info.getEntityName()).append(RT_1_SEMICOLON);
    sb.append("import ").append(info.getServicePath()).append(".").append(info.getServiceName()).append(RT_1_SEMICOLON);
    sb.append("import ").append(info.getMapperPath()).append(".").append(info.getMapperName()).append(RT_2_SEMICOLON);
    
    appendFileAnno(info, sb);// 类备注
    sb.append("@Service").append(RT_1);
    sb.append("public class ").append(serviceImplName).append(" extends ServiceImpl<").append(info.getMapperName());
    sb.append(", ").append(info.getEntityName()).append("> implements ").append(info.getServiceName());
    sb.append("{").append(RT_2);
    sb.append(TAB_1).append("@Autowired").append(RT_1);
    sb.append(TAB_1).append("private ").append(info.getMapperName()).append(" ").append("mapper").append(RT_1_SEMICOLON);
    sb.append("}");

    fw.write(sb.toString());
    fw.flush();
    fw.close();
    showInfo(fileName);
  }



  public static void createController(EntityInfo info) throws IOException {
    String controllerName = info.getEntityShortName()+"Controller";
    info.setControllerName(controllerName);
    String fileName = savePath + controllerName + javaFile;
    File file = createFile(fileName);
    FileWriter fw = new FileWriter(file);

    StringBuilder sb = new StringBuilder();
    String controllerPath = info.getPackagePath()+"."+controllerFolder;
    info.setControllerPath(controllerPath);
    sb.append("package ").append(controllerPath).append(RT_2_SEMICOLON);

    sb.append("import org.springframework.ui.Model;").append(RT_1);
    sb.append("import javax.servlet.ServletRequest;").append(RT_1);
    sb.append("import com.dimple.framework.web.controller.BaseController;").append(RT_1);
    sb.append("import org.springframework.stereotype.Controller;").append(RT_1);;
    sb.append("import org.springframework.web.bind.annotation.RequestMapping;").append(RT_1);
    sb.append("import org.springframework.beans.factory.annotation.Autowired;").append(RT_1);
    sb.append("import ").append(info.getEntityPath()).append(".").append(info.getEntityName()).append(RT_1_SEMICOLON);
    sb.append("import ").append(info.getServicePath()).append(".").append(info.getServiceName()).append(RT_1_SEMICOLON);


    appendFileAnno(info, sb);// 类备注
    sb.append("@Controller").append(RT_1).append("@RequestMapping(value = \"")
        .append(info.getReqMappingPath()).append("\")").append(RT_1);
    sb.append("public class ").append(controllerName).append(" extends BaseController").append("{").append(RT_2);

    sb.append("	@Autowired").append(RT_1);
    sb.append("	private ").append(info.getServiceName()).append(" service;").append(RT_2);


    sb.append("	/**").append(RT_1);
    sb.append("	 * ").append(RT_1);
    sb.append("	 * @Title: listView").append(RT_1);
    sb.append("	 * @author ").append(info.getAuthor()).append(RT_1);
    sb.append("	 * @date ").append(getDate()).append(RT_1);
    sb.append("	 * @param @param model").append(RT_1);
    sb.append("	 * @param @return 设定文件 ").append(RT_1);
    sb.append("	 * @return String 返回类型 ").append(RT_1);
    sb.append("	 * @throws").append(RT_1);
    sb.append("	 */").append(RT_1);
    sb.append(TAB_1).append("@RequestMapping(\"/list\")").append(RT_1);
    sb.append(TAB_1).append("public String listView(Model model) {").append(RT_1);
    sb.append(TAB_2).append("return \"").append(info.getJspPath()).append("/").append(info.getEntityShortName()).append("_list\";").append(RT_1);
    sb.append("	}").append(RT_2);

    sb.append(TAB_1).append("@RequestMapping(\"/add\")").append(RT_1);
    sb.append(TAB_1).append("public String addView(Model model, ServletRequest request) {").append(RT_1);
    sb.append(TAB_2).append("return \"").append(info.getJspPath()).append("/").append(info.getEntityShortName()).append("_edit\";").append(RT_1);
    sb.append("	}").append(RT_2);

    sb.append(TAB_1).append("@RequestMapping(\"/edit\")").append(RT_1);
    sb.append(TAB_1).append("public String editView(Model model, ServletRequest request, ").append(info.getEntityName()).append(" entity) {").append(RT_1);
    sb.append(TAB_2).append("return \"").append(info.getJspPath()).append("/").append(info.getEntityShortName()).append("_edit\";").append(RT_1);
    sb.append("	}").append(RT_2);

    sb.append(TAB_1).append("@RequestMapping(\"/detail\")").append(RT_1);
    sb.append(TAB_1).append("public String detailView(Model model, ServletRequest request, ").append(info.getEntityName()).append(" entity) {").append(RT_1);
    sb.append(TAB_2).append("return \"").append(info.getJspPath()).append("/").append(info.getEntityShortName()).append("_detail\";").append(RT_1);
    sb.append("	}").append(RT_2);

    sb.append("}");

    fw.write(sb.toString());
    fw.flush();
    fw.close();
    showInfo(fileName);
  }



  public static void createJspList(EntityInfo info, List<ColInfo> list) throws IOException {
    String jspPreName = info.getEntityName().toLowerCase().replace("entity", "");

    String fileName = savePath + jspPreName + "_list.jsp";
    File file = createFile(fileName);
    FileWriter fw = new FileWriter(file);

    StringBuilder sb = new StringBuilder();
    sb.append("<%@ page contentType=\"text/html;charset=UTF-8\"%>" + RT_1);
    sb.append("<%@ taglib prefix=\"c\" uri=\"http://java.sun.com/jsp/jstl/core\"%>").append(RT_1);
    sb.append("<%@ taglib prefix=\"shiro\" uri=\"http://shiro.apache.org/tags\"%>").append(RT_1);
    sb.append("<c:set var=\"ctx\" value=\"${pageContext.request.contextPath}\"/>").append(RT_1);
    sb.append("<c:set var=\"serviceurl\" value=\"${ctx}" + info.getReqMappingPath() + "\"/>")
        .append(RT_1);
    sb.append("<html>").append(RT_1);
    sb.append("<head>").append(RT_1);
    sb.append("<title>" + info.getTitle() + "</title>").append(RT_1);
    sb.append("</head>").append(RT_1);
    sb.append("<body>").append(RT_1);
    sb.append("	<div id=\"yy-page\" class=\"container-fluid page-container\">").append(RT_1);
    sb.append("		<div class=\"page-content\" id=\"yy-page-list\">").append(RT_1);
    sb.append("			<div class=\"row yy-toolbar\">").append(RT_1);
    sb.append("				<button id=\"yy-btn-add\" class=\"btn blue btn-sm\">").append(RT_1);
    sb.append("					<i class=\"fa fa-plus\"></i> 新增").append(RT_1);
    sb.append("				</button>").append(RT_1);
    sb.append("				<button id=\"yy-btn-remove\" class=\"btn red btn-sm\">").append(RT_1);
    sb.append("					<i class=\"fa fa-trash-o\"></i> 删除").append(RT_1);
    sb.append("				</button>").append(RT_1);
    sb.append("				<button id=\"yy-btn-refresh\" class=\"btn blue btn-sm\">").append(RT_1);
    sb.append("					<i class=\"fa fa-refresh\"></i> 刷新").append(RT_1);
    sb.append("				</button>").append(RT_1);
    sb.append("			</div>").append(RT_1);
    sb.append("			<div class=\"row yy-searchbar form-inline\">").append(RT_1);
    sb.append("				<form id=\"yy-form-query\">").append(RT_1);

    boolean hasSearch = false;
    for (ColInfo col : list) {
      if (col.isSearch()) {
        hasSearch = true;
        if (col.getEleType().equals(ColInfo.EleType.SELECT)) {
          sb.append("					<label for=\"search_EQ_" + col.getColAnno()
              + "\" class=\"control-label\">" + col.getColAnno() + "</label>").append(RT_1);
          sb.append(
              "					<select class=\"yy-input-enumdata form-control\" id=\"search_EQ_"
                  + col.getColName() + "\" ")
              .append(RT_1);
          sb.append("						name=\"search_EQ_" + col.getColAnno()
              + "\" data-enum-group=\"" + col.getEnumGroup() + "\"></select>").append(RT_2);
        } else {
          sb.append("					<label for=\"search_LIKE_" + col.getColAnno()
              + "\" class=\"control-label\">" + col.getColAnno() + "</label>").append(RT_1);
          sb.append(
              "					<input type=\"text\" autocomplete=\"on\" name=\"search_LIKE_"
                  + col.getColAnno() + "\"")
              .append(RT_1);
          sb.append("						id=\"search_LIKE_" + col.getColAnno()
              + "\" class=\"form-control input-sm\">").append(RT_2);
        }
      }
    }
    if (!hasSearch) {// 没有就默认一个给查询
      sb.append(
          "					<label for=\"search_EQ_billstatus\" class=\"control-label\">xxx</label>")
          .append(RT_1);
      sb.append(
          "					<select class=\"yy-input-enumdata form-control\" id=\"search_EQ_billstatus\" ")
          .append(RT_1);
      sb.append(
          "						name=\"search_EQ_billstatus\" data-enum-group=\"BillApplyStatus\"></select>")
          .append(RT_2);
      sb.append(
          "					<label for=\"search_LIKE_name\" class=\"control-label\">xxxxx</label>")
          .append(RT_1);
      sb.append(
          "					<input type=\"text\" autocomplete=\"on\" name=\"search_LIKE_name\"")
          .append(RT_1);
      sb.append("						id=\"search_LIKE_name\" class=\"form-control input-sm\">")
          .append(RT_2);
    }

    sb.append(
        "					<button id=\"yy-btn-search\" type=\"button\" class=\"btn btn-sm btn-info\">")
        .append(RT_1);
    sb.append("						<i class=\"fa fa-search\"></i>查询").append(RT_1);
    sb.append("					</button>").append(RT_1);
    sb.append("					<button id=\"rap-searchbar-reset\" type=\"reset\" class=\"red\">")
        .append(RT_1);
    sb.append("						<i class=\"fa fa-undo\"></i> 清空").append(RT_1);
    sb.append("					</button>").append(RT_1);
    sb.append("				</form>").append(RT_1);
    sb.append("			</div>").append(RT_1);
    sb.append("			<div class=\"row\">").append(RT_1);
    sb.append("				<table id=\"yy-table-list\" class=\"yy-table\">").append(RT_1);
    sb.append("					<thead>").append(RT_1);
    sb.append("						<tr>").append(RT_1);
    sb.append("							<th style=\"width: 30px;\">序号</th>").append(RT_1);
    sb.append("							<th class=\"table-checkbox\">").append(RT_1);
    sb.append(
        "								<input type=\"checkbox\" class=\"group-checkable\" data-set=\"#yy-table-list .checkboxes\"/>")
        .append(RT_1);
    sb.append("							</th>").append(RT_1);
    sb.append("							<th>操作</th>").append(RT_1);
    for (ColInfo col : list) {
      if (col.isListVisiable()) {
        sb.append("							<th>" + col.getColAnno() + "</th>").append(RT_1);
      }
    }
    sb.append("						</tr>").append(RT_1);
    sb.append("					</thead>").append(RT_1);
    sb.append("					<tbody></tbody>").append(RT_1);
    sb.append("				</table>").append(RT_1);
    sb.append("			</div>").append(RT_1);
    sb.append("		</div>").append(RT_1);
    sb.append("	</div>").append(RT_2);
    sb.append("	<!-- 公用脚本 -->").append(RT_1);
    sb.append("	<%@include file=\"/WEB-INF/views/common/listscript.jsp\"%>").append(RT_2);
    sb.append("	<script type=\"text/javascript\">").append(RT_1);
    sb.append("		_isNumber = true;").append(RT_1);
    sb.append("		var _tableCols = [ {").append(RT_1);
    sb.append("							data : null,").append(RT_1);
    sb.append("							orderable : false,").append(RT_1);
    sb.append("							className : \"center\",").append(RT_1);
    sb.append("							width : \"50\"").append(RT_1);
    sb.append("						},{").append(RT_1);
    sb.append("							data : \"uuid\",").append(RT_1);
    sb.append("							orderable : false,").append(RT_1);
    sb.append("							className : \"center\",").append(RT_1);
    sb.append("							/* visible : false, */").append(RT_1);
    sb.append("							width : \"40\",").append(RT_1);
    sb.append("							render : YYDataTableUtils.renderCheckCol").append(RT_1);
    sb.append("						},{").append(RT_1);
    sb.append("							data : \"uuid\",").append(RT_1);
    sb.append("							className : \"center\",").append(RT_1);
    sb.append("							orderable : false,").append(RT_1);
    sb.append("							render : YYDataTableUtils.renderActionCol,").append(RT_1);
    sb.append("							width : \"50\"").append(RT_1);

    for (int i = 0; i < list.size(); i++) {
      if (list.get(i).isListVisiable()) {
        sb.append("						},{").append(RT_1);
        sb.append("							data : \"" + list.get(i).getColName() + "\",")
            .append(RT_1);
        sb.append("							width : \"10%\",").append(RT_1);
        sb.append("							className : \"center\",").append(RT_1);
        sb.append("							orderable : true").append(RT_1);
      }
    }
    sb.append("						}];").append(RT_1);

    sb.append(RT_2);
    sb.append("		//var _setOrder = [[5,'desc']];").append(RT_1);
    sb.append("		$(document).ready(function() {").append(RT_1);
    sb.append("			_queryData = $(\"#yy-form-query\").serializeArray();").append(RT_1);
    sb.append("			bindListActions();").append(RT_1);
    sb.append("			serverPage(null);").append(RT_1);
    sb.append("		});").append(RT_1);
    sb.append("	</script>").append(RT_1);
    sb.append("</body>").append(RT_1);
    sb.append("</html>	").append(RT_1);
    sb.append("").append(RT_1);

    fw.write(sb.toString());
    fw.flush();
    fw.close();
    showInfo(fileName);

  }


  public static void createJspEdit(EntityInfo info, List<ColInfo> list) throws IOException {
    String jspPreName = info.getEntityName().toLowerCase().replace("entity", "");

    String fileName = savePath + jspPreName + "_edit.jsp";
    File file = createFile(fileName);
    FileWriter fw = new FileWriter(file);

    StringBuilder sb = new StringBuilder();
    sb.append("<%@ page contentType=\"text/html;charset=UTF-8\"%>" + RT_1);
    sb.append("<%@ taglib prefix=\"c\" uri=\"http://java.sun.com/jsp/jstl/core\"%>" + RT_1);
    sb.append("<c:set var=\"ctx\" value=\"${pageContext.request.contextPath}\" />" + RT_1);
    sb.append(
        "<c:set var=\"serviceurl\" value=\"${ctx}" + info.getReqMappingPath() + "\"/>" + RT_1);
    sb.append("<html>" + RT_1);
    sb.append("<head>" + RT_1);
    sb.append("<title>" + info.getTitle() + "</title>" + RT_1);
    sb.append("</head>" + RT_1);
    sb.append("<body>" + RT_1);
    sb.append("	<div id=\"yy-page-edit\" class=\"container-fluid page-container page-content\" >"
        + RT_1);
    sb.append("		<div class=\"row yy-toolbar\">" + RT_1);
    sb.append("			<button id=\"yy-btn-save\" class=\"btn blue btn-sm\">" + RT_1);
    sb.append("				<i class=\"fa fa-save\"></i> 保存" + RT_1);
    sb.append("			</button>" + RT_1);
    sb.append("			<button id=\"yy-btn-cancel\" class=\"btn blue btn-sm\">" + RT_1);
    sb.append("				<i class=\"fa fa-rotate-left\"></i> 取消" + RT_1);
    sb.append("			</button>" + RT_1);
    sb.append("		</div>" + RT_1);
    // sb.append(" <div>"+RT_1);
    sb.append("		<form id=\"yy-form-edit\" class=\"form-horizontal yy-form-edit\">" + RT_1);
    sb.append("			<input name=\"uuid\" type=\"text\" class=\"hide\" value=\"${entity.uuid}\">"
        + RT_1);

    StringBuilder sbCol = new StringBuilder();
    int colIndex = 0;// 用于计算一个字段占多列的情况

    List<ColInfo> detailList = new ArrayList<ColInfo>();// 明细页面显示的字段列
    for (ColInfo col : list) {
      if (col.isDetailVisiable()) {
        detailList.add(col);
      }
    }
    for (int i = 0; i < detailList.size(); i++) {
      // 设置列==========================
      int colMd = 0;
      int colMdSub1 = 0;
      int colMdSub2 = 0;
      String colSubStyle1 = "";
      String colSubStyle2 = "";
      if (detailList.get(i).getColCount() == 1) {
        colMd = 4;
        colMdSub1 = 4;
        colMdSub2 = 8;
      } else if (detailList.get(i).getColCount() == 2) {
        colMd = 8;
        colMdSub1 = 2;
        colMdSub2 = 10;
      } else if (detailList.get(i).getColCount() == 3) {
        colMd = 12;
        colMdSub1 = 1;
        colMdSub2 = 11;
        colSubStyle1 = "style=\"width: 11.11%;\"";
        colSubStyle2 = "style=\"width: 88.89%;\"";
      }
      sbCol = new StringBuilder();
      sbCol.append("				<div class=\"col-md-" + colMd + "\">" + RT_1);
      sbCol.append("					<div class=\"form-group\">" + RT_1);
      String requiredCls = "";
      if (detailList.get(i).isRequired()) {
        requiredCls = " required";
      }
      sbCol.append(
          "						<label class=\"control-label col-md-" + colMdSub1 + requiredCls
              + "\" " + colSubStyle1 + ">" + detailList.get(i).getColAnno() + "</label>" + RT_1);
      sbCol.append("						<div class=\"col-md-" + colMdSub2 + "\" " + colSubStyle2
          + ">" + RT_1);
      sbCol.append("							").append(createHtmlEle(detailList.get(i)))
          .append(RT_1);
      sbCol.append("						</div>" + RT_1);
      sbCol.append("					</div>" + RT_1);
      sbCol.append("				</div>" + RT_1);
      // 设置列==========================

      if (detailList.get(i).isRow()) {// 是否单独一行
        if (i != 0) {
          sb.append("			</div>" + RT_1);
        }
        sb.append("			<div class=\"row\">" + RT_1);
        sb.append(sbCol.toString());
        sb.append("			</div>" + RT_1);
        if (i != (detailList.size() - 1)) {
          sb.append("			<div class=\"row\">" + RT_1);
        }
      } else {
        if (i == (detailList.size() - 1)) {
          if (colIndex % 3 == 0) {
            sb.append("			<div class=\"row\">" + RT_1);
            sb.append(sbCol.toString());
            sb.append("			</div>" + RT_1);
          } else {
            sb.append(sbCol.toString());
            sb.append("			</div>" + RT_1);
          }
        } else {
          if (colIndex % 3 == 0) {
            sb.append("			<div class=\"row\">" + RT_1);
            sb.append(sbCol.toString());
          } else if (colIndex % 3 == 1) {
            sb.append(sbCol.toString());
          } else {
            sb.append(sbCol.toString());
            sb.append("			</div>" + RT_1);
          }
        }
      }
      colIndex += detailList.get(i).getColCount();
    }
    sb.append("		</form>" + RT_1);
    sb.append("	</div>" + RT_1);
    sb.append("	<!-- 公用脚本 -->" + RT_1);
    sb.append("	<%@include file=\"/WEB-INF/views/common/editscript.jsp\"%>" + RT_2);
    sb.append("	<script type=\"text/javascript\">" + RT_2);
    sb.append("		$(document).ready(function() {" + RT_1);
    sb.append("			//按钮绑定事件" + RT_1);
    sb.append("			bindEditActions();" + RT_1);
    sb.append("			//bindButtonAction();" + RT_1);
    sb.append("			validateForms();" + RT_1);
    sb.append("			setValue();" + RT_1);
    sb.append("		});" + RT_2);
    sb.append("		//设置默认值" + RT_1);
    sb.append("		function setValue() {" + RT_1);
    sb.append("			if ('${openstate}' == 'add') {" + RT_1);
    sb.append("				//$(\"select[name='is_use']\").val('1');" + RT_1);
    sb.append("			} else if ('${openstate}' == 'edit') {" + RT_1);
    for (int i = 0; i < detailList.size(); i++) {
      if (detailList.get(i).getEleType().equals(ColInfo.EleType.SELECT)) {
        sb.append("				$(\"select[name='" + detailList.get(i).getColName()
            + "']\").val('${entity." + detailList.get(i).getColName() + "}');" + RT_1);
      }
    }
    sb.append("			}" + RT_1);
    sb.append("		}" + RT_2);
    sb.append("		//表单校验" + RT_1);
    sb.append("		function validateForms(){" + RT_1);
    sb.append("			validata = $('#yy-form-edit').validate({" + RT_1);
    sb.append("				onsubmit : true," + RT_1);
    sb.append("				rules : {" + RT_1);

    boolean hasRule = false;
    for (int i = 0; i < detailList.size(); i++) {
      String requiredStr = "";
      if (detailList.get(i).isRequired()) {
        requiredStr = "required : true,";
      }
      if (!hasRule) {
        sb.append("					'" + detailList.get(i).getColName() + "' : {" + requiredStr
            + "maxlength : 100}");
      } else {
        sb.append(",").append(RT_1).append("					'" + detailList.get(i).getColName()
            + "' : {" + requiredStr + "maxlength : 100}");
      }
      hasRule = true;
    }
    sb.append(RT_1).append("				}" + RT_1);
    sb.append("			});" + RT_1);
    sb.append("		}" + RT_1);
    sb.append("	</script>" + RT_1);
    sb.append("</body>" + RT_1);
    sb.append("</html>" + RT_1);
    fw.write(sb.toString());
    fw.flush();
    fw.close();
    showInfo(fileName);

  }

  public static void createJspDetail(EntityInfo info, List<ColInfo> list) throws IOException {
    String jspPreName = info.getEntityName().toLowerCase().replace("entity", "");

    String fileName = savePath + jspPreName + "_detail.jsp";
    File file = createFile(fileName);
    FileWriter fw = new FileWriter(file);

    StringBuilder sb = new StringBuilder();
    sb.append("<%@ page contentType=\"text/html;charset=UTF-8\"%>" + RT_1);
    sb.append("<%@ taglib prefix=\"c\" uri=\"http://java.sun.com/jsp/jstl/core\"%>" + RT_1);
    sb.append("<c:set var=\"ctx\" value=\"${pageContext.request.contextPath}\" />" + RT_1);
    sb.append(
        "<c:set var=\"serviceurl\" value=\"${ctx}" + info.getReqMappingPath() + "\"/>" + RT_1);
    sb.append("<html>" + RT_1);
    sb.append("<head>" + RT_1);
    sb.append("<title>" + info.getTitle() + "</title>" + RT_1);
    sb.append("</head>" + RT_1);
    sb.append("<body>" + RT_1);
    sb.append("	<div id=\"yy-page-detail\" class=\"container-fluid page-container page-content\" >"
        + RT_1);
    sb.append("		<div class=\"row yy-toolbar\">" + RT_1);
    sb.append("			<button id=\"yy-btn-backtolist\" class=\"btn blue btn-sm\">" + RT_1);
    sb.append("				<i class=\"fa fa-rotate-left\"></i> 返回" + RT_1);
    sb.append("			</button>" + RT_1);
    sb.append("		</div>" + RT_1);
    // sb.append(" <div>"+RT_1);
    sb.append("		<form id=\"yy-form-detail\" class=\"form-horizontal yy-form-detail\">" + RT_1);
    sb.append("			<input name=\"uuid\" type=\"text\" class=\"hide\" value=\"${entity.uuid}\">"
        + RT_1);

    StringBuilder sbCol = new StringBuilder();
    int colIndex = 0;// 用于计算一个字段占多列的情况
    List<ColInfo> detailList = new ArrayList<ColInfo>();// 明细页面显示的字段列
    for (ColInfo col : list) {
      if (col.isDetailVisiable()) {
        detailList.add(col);
      }
    }
    for (int i = 0; i < detailList.size(); i++) {
      // 设置列==========================
      int colMd = 0;
      int colMdSub1 = 0;
      int colMdSub2 = 0;
      String colSubStyle1 = "";
      String colSubStyle2 = "";
      if (detailList.get(i).getColCount() == 1) {
        colMd = 4;
        colMdSub1 = 4;
        colMdSub2 = 8;
      } else if (detailList.get(i).getColCount() == 2) {
        colMd = 8;
        colMdSub1 = 2;
        colMdSub2 = 10;
      } else if (detailList.get(i).getColCount() == 3) {
        colMd = 12;
        colMdSub1 = 1;
        colMdSub2 = 11;
        colSubStyle1 = "style=\"width: 11.11%;\"";
        colSubStyle2 = "style=\"width: 88.89%;\"";
      }
      sbCol = new StringBuilder();
      sbCol.append("				<div class=\"col-md-" + colMd + "\">" + RT_1);
      sbCol.append("					<div class=\"form-group\">" + RT_1);
      String requiredCls = "";
      if (detailList.get(i).isRequired()) {
        requiredCls = "";
      }
      sbCol.append(
          "						<label class=\"control-label col-md-" + colMdSub1 + requiredCls
              + "\" " + colSubStyle1 + ">" + detailList.get(i).getColAnno() + "</label>" + RT_1);
      sbCol.append("						<div class=\"col-md-" + colMdSub2 + "\" " + colSubStyle2
          + ">" + RT_1);
      sbCol.append("							").append(createHtmlEle(detailList.get(i)))
          .append(RT_1);
      sbCol.append("						</div>" + RT_1);
      sbCol.append("					</div>" + RT_1);
      sbCol.append("				</div>" + RT_1);
      // 设置列==========================

      if (detailList.get(i).isRow()) {// 是否单独一行
        if (i != 0) {
          sb.append("			</div>" + RT_1);
        }
        sb.append("			<div class=\"row\">" + RT_1);
        sb.append(sbCol.toString());
        sb.append("			</div>" + RT_1);
        if (i != (detailList.size() - 1)) {
          sb.append("			<div class=\"row\">" + RT_1);
        }
      } else {
        if (i == (detailList.size() - 1)) {
          if (colIndex % 3 == 0) {
            sb.append("			<div class=\"row\">" + RT_1);
            sb.append(sbCol.toString());
            sb.append("			</div>" + RT_1);
          } else {
            sb.append(sbCol.toString());
            sb.append("			</div>" + RT_1);
          }
        } else {
          if (colIndex % 3 == 0) {
            sb.append("			<div class=\"row\">" + RT_1);
            sb.append(sbCol.toString());
          } else if (colIndex % 3 == 1) {
            sb.append(sbCol.toString());
          } else {
            sb.append(sbCol.toString());
            sb.append("			</div>" + RT_1);
          }
        }
      }
      colIndex += detailList.get(i).getColCount();
    }
    sb.append("		</form>" + RT_1);
    sb.append("	</div>" + RT_1);
    sb.append("	<!-- 公用脚本 -->" + RT_1);
    sb.append("	<%@include file=\"/WEB-INF/views/common/detailscript.jsp\"%>" + RT_2);
    sb.append("	<script type=\"text/javascript\">" + RT_2);
    sb.append("		$(document).ready(function() {" + RT_1);
    sb.append("			//按钮绑定事件" + RT_1);
    sb.append("			bindDetailActions();" + RT_1);
    sb.append("			//bindButtonAction();" + RT_1);
    sb.append("			setValue();" + RT_2);
    sb.append("			YYFormUtils.lockForm(\"yy-form-detail\");" + RT_1);
    sb.append("		});" + RT_2);
    sb.append("		//设置默认值" + RT_1);
    sb.append("		function setValue() {" + RT_1);
    sb.append("			if ('${openstate}' == 'add') {" + RT_1);
    sb.append("				//$(\"select[name='is_use']\").val('1');" + RT_1);
    sb.append("			} else if ('${openstate}' == 'detail') {" + RT_1);
    for (int i = 0; i < detailList.size(); i++) {
      if (detailList.get(i).getEleType().equals(ColInfo.EleType.SELECT)) {
        sb.append("				$(\"select[name='" + detailList.get(i).getColName()
            + "']\").val('${entity." + detailList.get(i).getColName() + "}');" + RT_1);
      }
    }
    sb.append("			}" + RT_1);
    sb.append("		}" + RT_2);
    sb.append("	</script>" + RT_1);
    sb.append("</body>" + RT_1);
    sb.append("</html>" + RT_1);
    fw.write(sb.toString());
    fw.flush();
    fw.close();
    showInfo(fileName);

  }

  /**
   * 创建文件 @Title: createFile @author liusheng @date 2017年6月28日 下午2:39:19 @param @param
   * fileName @param @return @param @throws IOException 设定文件 @return File 返回类型 @throws
   */
  public static File createFile(String fileName) throws IOException {
    File file = new File(fileName);
    // 判断目标文件所在的目录是否存在
    if (!file.getParentFile().exists()) {
      // 如果目标文件所在的目录不存在，则创建父目录
      System.out.println("目标文件所在目录不存在，准备创建它！");
      if (!file.getParentFile().mkdirs()) {
        System.out.println("创建目标文件所在目录失败！");
      }
    }
    if (!file.exists()) {
      file.createNewFile();
    }
    return file;
  }

  /**
   * 获取路径的最后面字符串<br>
   * 如：<br>
   * <code>str = "com.b510.base.bean.User"</code><br>
   * <code> return "User";<code>
   * 
   * @param str
   * @return
   */
  public static String getLastChar(String str) {
    if ((str != null) && (str.length() > 0)) {
      int dot = str.lastIndexOf('.');
      if ((dot > -1) && (dot < (str.length() - 1))) {
        return str.substring(dot + 1);
      }
    }
    return str;
  }

  /**
   * 把第一个字母变为小写<br>
   * 如：<br>
   * <code>str = "UserDao";</code><br>
   * <code>return "userDao";</code>
   * 
   * @param str
   * @return
   */
  public static String getLowercaseChar(String str) {
    return str.substring(0, 1).toLowerCase() + str.substring(1);
  }

  /**
   * 将第一个字母变为大写 @Title: getUppercaseChar @author liusheng @date 2017年6月28日 下午1:48:37 @param @param
   * str @param @return 设定文件 @return String 返回类型 @throws
   */
  public static String getUppercaseChar(String str) {
    return str.substring(0, 1).toUpperCase() + str.substring(1);
  }

  /**
   * 显示信息
   * 
   * @param info
   */
  public static void showInfo(String info) {
    System.out.println("创建文件：" + info + "成功！");
  }

  /**
   * 获取系统时间
   * 
   * @return
   */
  public static String getDate() {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return simpleDateFormat.format(new Date());
  }


  /**
   * 追加类备注 @Title: appendFileAnno @author liusheng @date 2017年6月28日 下午3:02:51 @param @param
   * enInfo @param @param sb 设定文件 @return void 返回类型 @throws
   */
  public static void appendFileAnno(EntityInfo enInfo, StringBuilder sb) {
    sb.append(RT_1).append("/**" + RT_1 + BLANK_1 + "*" + BLANK_1 + enInfo.getTitle() + RT_1);// 类注释
    sb.append(BLANK_1 + "*" + BLANK_1 + "@author " + enInfo.getAuthor() + RT_1);
    sb.append(BLANK_1 + "*" + BLANK_1 + "@date " + getDate() + RT_1);
    sb.append(BLANK_1 + "*/" + RT_1);
  }

  private static String createHtmlEle(ColInfo col) {
    String htmlStr = "";
    if (col.getEleType().equals(ColInfo.EleType.DATE)) {
      htmlStr = "<input name=\"" + col.getColName() + "\" id=\"" + col.getColName()
          + "\" type=\"text\" value=\"${entity." + col.getColName()
          + "}\" class=\"Wdate form-control\" onclick=\"WdatePicker();\">";
    } else if (col.getEleType().equals(ColInfo.EleType.DATETIME)) {
      htmlStr = "<input name=\"" + col.getColName() + "\" id=\"" + col.getColName()
          + "\" type=\"text\" value=\"${entity." + col.getColName()
          + "}\" class=\"Wdate form-control\" onfocus=\"WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});\">";
    } else if (col.getEleType().equals(ColInfo.EleType.SELECT)) {
      htmlStr = "<select name=\"" + col.getColName() + "\" id=\"" + col.getColName()
          + "\" data-enum-group=\"" + col.getEnumGroup()
          + "\" class=\"yy-input-enumdata form-control\"></select>";
    } else if (col.getEleType().equals(ColInfo.EleType.TEXTAREA)) {
      htmlStr = "<textarea name=\"" + col.getColName() + "\" id=\"" + col.getColName()
          + "\" class=\"form-control\">${entity." + col.getColName() + "}</textarea>";
    } else if (col.getEleType().equals(ColInfo.EleType.REF)) {
      StringBuilder refStr = new StringBuilder();
      refStr.append("<div class=\"input-group input-icon right\">").append(RT_1);
      refStr.append("								<input id=\"" + col.getColName()
          + "Uuid\" name=\"" + col.getColName() + ".uuid\" type=\"hidden\" value=\"${entity."
          + col.getColName() + ".uuid}\">").append(RT_1);
      refStr
          .append("								<i class=\"fa fa-remove\" onclick=\"cleanDef('"
              + col.getColName() + "Uuid','" + col.getColName() + "Name');\" title=\"清空\"></i>")
          .append(RT_1);
      refStr.append("								<input id=\"" + col.getColName()
          + "Name\" name=\"" + col.getColName()
          + "Name\" type=\"text\" class=\"form-control\" readonly=\"readonly\" value=\"${entity."
          + col.getColName() + ".name}\">").append(RT_1);
      refStr.append("								<span class=\"input-group-btn\">").append(RT_1);
      refStr.append("									<button id=\"" + col.getColName()
          + "-select-btn\" class=\"btn btn-default btn-ref\" type=\"button\">").append(RT_1);
      refStr
          .append(
              "										<span class=\"glyphicon glyphicon-search\"></span>")
          .append(RT_1);
      refStr.append("									</button>").append(RT_1);
      refStr.append("								</span>").append(RT_1);
      refStr.append("							</div>");
      htmlStr = refStr.toString();
    } else {
      htmlStr = "<input name=\"" + col.getColName() + "\" id=\"" + col.getColName()
          + "\" type=\"text\" value=\"${entity." + col.getColName() + "}\" class=\"form-control\">";
    }
    return htmlStr;
  }
  
  
  /**
   * 下划线转驼峰
   * @param str
   * @return
   */
  public static StringBuffer camel(StringBuffer str) {
      //利用正则删除下划线，把下划线后一位改成大写
      Pattern pattern = Pattern.compile("_(\\w)");
      Matcher matcher = pattern.matcher(str);
      StringBuffer sb = new StringBuffer(str);
      if(matcher.find()) {
          sb = new StringBuffer();
          //将当前匹配子串替换为指定字符串，并且将替换后的子串以及其之前到上次匹配子串之后的字符串段添加到一个StringBuffer对象里。
          //正则之前的字符和被替换的字符
          matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
          //把之后的也添加到StringBuffer对象里
          matcher.appendTail(sb);         
      }else {
          return sb;
      }   
      return camel(sb);
  }
  
  
  /**
   * 驼峰转下划线
   * @param str
   * @return
   */
  public static StringBuffer underline(StringBuffer str) {
      Pattern pattern = Pattern.compile("[A-Z]");
      Matcher matcher = pattern.matcher(str);
      StringBuffer sb = new StringBuffer(str);
      if(matcher.find()) {
          sb = new StringBuffer();
          //将当前匹配子串替换为指定字符串，并且将替换后的子串以及其之前到上次匹配子串之后的字符串段添加到一个StringBuffer对象里。
          //正则之前的字符和被替换的字符
          matcher.appendReplacement(sb,"_"+matcher.group(0).toLowerCase());
          //把之后的也添加到StringBuffer对象里
          matcher.appendTail(sb);         
      }else {
          return sb;
      }   
      return underline(sb);
  }
}
