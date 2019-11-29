package com.dimple.project.coding;

/**
 * 实体信息
 * @ClassName: EntityInfo
 * @author liusheng
 * @date 2017年6月28日 下午2:59:14
 */
public class EntityInfo {

	private String entityName;//实体名
	
	private String packagePath;//包路径
	
	private String extendEntityName;//继承
	
	private String reqMappingPath;
	
	private String title;//类注释
	
	private String author;//作者
	
	private String jspPath;//jsp页面的路径
	

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getPackagePath() {
		return packagePath;
	}

	public void setPackagePath(String packagePath) {
		this.packagePath = packagePath;
	}

	public String getExtendEntityName() {
		return extendEntityName;
	}

	public void setExtendEntityName(String extendEntityName) {
		this.extendEntityName = extendEntityName;
	}
	
	public String getReqMappingPath() {
		return reqMappingPath;
	}

	public void setReqMappingPath(String reqMappingPath) {
		this.reqMappingPath = reqMappingPath;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getJspPath() {
		return jspPath;
	}

	public void setJspPath(String jspPath) {
		this.jspPath = jspPath;
	}

}
