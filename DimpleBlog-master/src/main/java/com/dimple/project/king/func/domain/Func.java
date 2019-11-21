package com.dimple.project.king.func.domain;

import java.util.ArrayList;
import java.util.List;

import com.dimple.framework.web.domain.BaseEntity;
import com.dimple.project.king.util.TreeModel;

/**
 * @className: Menu
 * @description: 菜单权限表 bg_func
 * @auther: Dimple
 * @Date: 2019/3/13
 * @Version: 1.1
 */
public class Func extends BaseEntity implements TreeModel{
	
    private static final long serialVersionUID = 1L;

    /**
     * 菜单ID
     */
    private Long funcId;

    /**
     * 菜单名称
     */
    private String funcName;

    /**
     * 父菜单名称
     */
    private String parentName;

    /**
     * 父菜单ID
     */
    private Long parentId;

    /**
     * 显示顺序
     */
    private String orderNum;

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

    /**
     * 子菜单
     */
    private List<Func> children = new ArrayList<Func>();

    private Long creator;

    public Func() {
	}
	public Func(String funcName, String url) {
		this.funcName = funcName;
		this.url = url;
	}

	public Long getFuncId() {
		return funcId;
	}

	public void setFuncId(Long funcId) {
		this.funcId = funcId;
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

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
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

	public List<Func> getChildren() {
		return children;
	}

	public void setChildren(List<Func> children) {
		this.children = children;
	}

	@Override
	public String toString() {
		return "Func [funcId=" + funcId + ", funcName=" + funcName + ", parentName=" + parentName + ", parentId="
				+ parentId + ", orderNum=" + orderNum + ", url=" + url + ", funcType=" + funcType + ", visible="
				+ visible + ", perms=" + perms + ", icon=" + icon + ", children=" + children + "]";
	}

	@Override
	public Long getId() {
		return funcId;
	}

	@Override
	public void setChildList(List children) {
		setChildren(children);
	}

	@Override
	public String getName() {
		return getFuncName();
	}

	@Override
	public void setName(String name) {
		setFuncName(name);
	}

	public Long getCreator() {
		return creator;
	}

	public void setCreator(Long creator) {
		this.creator = creator;
	}
}
