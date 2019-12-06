package com.dimple.project.king.func.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dimple.project.king.func.domain.Func;

/**
 * @className: FuncMapper
 * @description: 菜单表 数据层
 * @auther: Dimple
 * @Date: 2019/3/13
 * @Version: 1.1
 */
public interface FuncMapper extends BaseMapper<Func>{

	public List<Func> findByCreator(String loginName);

  public int selectCountFuncByParentId(Long parentId);

  public List<Func> selectFuncList(Func func);
}
