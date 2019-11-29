package com.yy.modules.ver.auditCols;

import lombok.ToString;
import com.dimple.framework.web.domain.SuperEntity;
import org.springframework.data.annotation.Id;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * 对比项表
 * @author ls2008
 * @date 2019-11-29 15:25:41
 */
@ToString
@TableName(value = "yy_adddddsub")
public class AdddddSubEntity extends SuperEntity {

	private static final long serialVersionUID = 1L;

	@Id	@TableId(value = "id", type = IdType.AUTO)
	private Long id;//表id

	private String columnAnno;//列名

	private String colaaaaaa;//栏位aaaaaaa

	private String columnName;//栏位

	private String isDisplay;//是否显示

	private String type;//类别

	private String type111;//类别1111

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getColumnAnno() {
		return columnAnno;
	}

	public void setColumnAnno(String columnAnno) {
		this.columnAnno = columnAnno;
	}

	public String getColaaaaaa() {
		return colaaaaaa;
	}

	public void setColaaaaaa(String colaaaaaa) {
		this.colaaaaaa = colaaaaaa;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getIsDisplay() {
		return isDisplay;
	}

	public void setIsDisplay(String isDisplay) {
		this.isDisplay = isDisplay;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType111() {
		return type111;
	}

	public void setType111(String type111) {
		this.type111 = type111;
	}

}