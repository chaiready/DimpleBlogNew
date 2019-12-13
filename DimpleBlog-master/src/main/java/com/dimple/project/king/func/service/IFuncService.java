package com.dimple.project.king.func.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dimple.project.king.func.domain.Func;

/**
 * @className: IFuncService
 * @description: 菜单 业务层
 * @auther: Dimple
 * @Date: 2019/3/13
 * @Version: 1.1
 */
public interface IFuncService extends IService<Func>{
    
	public List<Func> findByCreator(String loginName);

    public List<Func> findBbsByCreator(String loginName);

    int insertFunc(Func func);

    public int updateFunc(Func func);

    public int selectCountFuncByParentId(Long funcId);

    public List<Func> selectFuncList(Func func);

    public int toFirst(Long funcId);
}
