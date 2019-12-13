package com.dimple.project.king.func.domain;

import org.springframework.data.annotation.Id;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dimple.framework.web.domain.SuperEntity;

/**
 * @className: Menu
 * @description: 菜单权限表 bg_func
 * @auther: Dimple
 * @Date: 2019/3/13
 * @Version: 1.1
 */
@TableName("bg_func")
public class Func extends SuperEntity {

  private static final long serialVersionUID = 1L;

  /**
   * 菜单ID
   */
  @TableId(value = "id", type = IdType.AUTO)
  @Id
  private Long id;

  /**
   * 菜单名称
   */
  private String funcName;

  /**
   * 父菜单名称
   */
  @TableField(exist=false)
  private String parentName;

  /**
   * 父菜单ID
   */
  private Long parentId;

  /**
   * 显示顺序
   */
  private long orderNum;

  /**
   * 菜单URL
   */
  private String url;

  /**
   * 类型:0目录,1菜单,2按钮
   */
  private String funcType;

  /**
   * 菜单状态:0显示,1隐藏
   */
  private String visible;

  /**
   * 权限字符串
   */
  private String perms;

  /**
   * 菜单图标
   */
  private String icon;


  private Long creator;

  public Func() {}

  public Func(String funcName, String url) {
    this.funcName = funcName;
    this.url = url;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFuncName() {
    return funcName;
  }

  public void setFuncName(String funcName) {
    this.funcName = funcName;
  }

  public String getParentName() {
    return parentName;
  }

  public void setParentName(String parentName) {
    this.parentName = parentName;
  }

  public Long getParentId() {
    return parentId;
  }

  public void setParentId(Long parentId) {
    this.parentId = parentId;
  }

  public long getOrderNum() {
    return orderNum;
  }

  public void setOrderNum(long orderNum) {
    this.orderNum = orderNum;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getFuncType() {
    return funcType;
  }

  public void setFuncType(String funcType) {
    this.funcType = funcType;
  }

  public String getVisible() {
    return visible;
  }

  public void setVisible(String visible) {
    this.visible = visible;
  }

  public String getPerms() {
    return perms;
  }

  public void setPerms(String perms) {
    this.perms = perms;
  }

  public String getIcon() {
    return icon;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }

  public Long getCreator() {
    return creator;
  }

  public void setCreator(Long creator) {
    this.creator = creator;
  }
}
