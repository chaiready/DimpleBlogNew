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
	

	
	
	private String mapperName;
	private String mapperPath;
	
	private String entityPath;
	private String entityShortName;
	
	private String serviceName;
	private String servicePath;
	
	private String serviceImplName;
	private String serviceImplPath;
	
	private String controllerName;
	private String controllerPath;
	
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

  public String getMapperName() {
    return mapperName;
  }

  public void setMapperName(String mapperName) {
    this.mapperName = mapperName;
  }

  public String getMapperPath() {
    return mapperPath;
  }

  public void setMapperPath(String mapperPath) {
    this.mapperPath = mapperPath;
  }

  public String getEntityPath() {
    return entityPath;
  }

  public void setEntityPath(String entityPath) {
    this.entityPath = entityPath;
  }

  public String getServiceName() {
    return serviceName;
  }

  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }

  public String getServicePath() {
    return servicePath;
  }

  public void setServicePath(String servicePath) {
    this.servicePath = servicePath;
  }

  public String getServiceImplName() {
    return serviceImplName;
  }

  public void setServiceImplName(String serviceImplName) {
    this.serviceImplName = serviceImplName;
  }

  public String getServiceImplPath() {
    return serviceImplPath;
  }

  public void setServiceImplPath(String serviceImplPath) {
    this.serviceImplPath = serviceImplPath;
  }

  public String getEntityShortName() {
    return entityShortName;
  }

  public void setEntityShortName(String entityShortName) {
    this.entityShortName = entityShortName;
  }

  public String getControllerName() {
    return controllerName;
  }

  public void setControllerName(String controllerName) {
    this.controllerName = controllerName;
  }

  public String getControllerPath() {
    return controllerPath;
  }

  public void setControllerPath(String controllerPath) {
    this.controllerPath = controllerPath;
  }

  
}
