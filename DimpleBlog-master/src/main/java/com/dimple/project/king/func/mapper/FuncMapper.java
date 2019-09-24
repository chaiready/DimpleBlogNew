package com.dimple.project.king.func.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dimple.project.king.func.domain.Func;

/**
 * @className: FuncMapper
 * @description: 菜单表 数据层
 * @auther: Dimple
 * @Date: 2019/3/13
 * @Version: 1.1
 */
public interface FuncMapper {
    /**
     * 查询系统所有菜单（含按钮）
     *
     * @return 菜单列表
     */
    public List<Func> selectFuncAll();

    /**
     * 查询系统正常显示菜单（不含按钮）
     *
     * @return 菜单列表
     */
    public List<Func> selectFuncNormalAll();

    /**
     * 根据用户ID查询菜单
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    public List<Func> selectFuncsByUserId(Long userId);

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    public List<String> selectPermsByUserId(Long userId);

    /**
     * 根据角色ID查询菜单
     *
     * @param roleId 角色ID
     * @return 菜单列表
     */
    public List<String> selectFuncTree(Long roleId);

    /**
     * 查询系统菜单列表
     *
     * @param func 菜单信息
     * @return 菜单列表
     */
    public List<Func> selectFuncList(Func func);

    /**
     * 删除菜单管理信息
     *
     * @param funcId 菜单ID
     * @return 结果
     */
    public int deleteFuncById(Long funcId);

    /**
     * 根据菜单ID查询信息
     *
     * @param funcId 菜单ID
     * @return 菜单信息
     */
    public Func selectFuncById(Long funcId);

    /**
     * 查询菜单数量
     *
     * @param parentId 菜单父ID
     * @return 结果
     */
    public int selectCountFuncByParentId(Long parentId);

    /**
     * 新增菜单信息
     *
     * @param func 菜单信息
     * @return 结果
     */
    public int insertFunc(Func func);

    /**
     * 修改菜单信息
     *
     * @param func 菜单信息
     * @return 结果
     */
    public int updateFunc(Func func);

    /**
     * 校验菜单名称是否唯一
     *
     * @param funcName 菜单名称
     * @param parentId 父菜单ID
     * @return 结果
     */
    public Func checkFuncNameUnique(@Param("funcName") String funcName, @Param("parentId") Long parentId);
    

	public List<Func> findByCreator(String loginName);
}
