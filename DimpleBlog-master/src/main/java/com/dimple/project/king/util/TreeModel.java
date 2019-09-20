package com.dimple.project.king.util;

import java.util.List;

/**
 * 树实体
 * @author liusheng
 *
 */
public interface TreeModel {

	Long getId();
	
	String getName();
	
	void setName(String name);
	
	Long getParentId();
	
	void setChildList(List children);
}
