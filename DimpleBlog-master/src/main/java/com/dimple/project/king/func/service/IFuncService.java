package com.dimple.project.king.func.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dimple.framework.web.domain.Ztree;
import com.dimple.project.king.func.domain.Func;
import com.dimple.project.system.role.domain.Role;
import com.dimple.project.system.user.domain.User;

/**
 * @className: IFuncService
 * @description: 菜单 业务层
 * @auther: Dimple
 * @Date: 2019/3/13
 * @Version: 1.1
 */
public interface IFuncService {
    /**
     * 根据用户ID查询菜单
     *
     * @param user 用户信息
     * @return 菜单列表
     */
    public List<Func> selectFuncsByUser(User user);

    /**
     * 查询系统菜单列表
     *
     * @param func 菜单信息
     * @return 菜单列表
     */
    public List<Func> selectFuncList(Func func);

    /**
     * 查询菜单集合
     *
     * @return 所有菜单信息
     */
    public List<Func> selectFuncAll();

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    public Set<String> selectPermsByUserId(Long userId);

    /**
     * 根据角色ID查询菜单
     *
     * @param role 角色对象
     * @return 菜单列表
     */
    public List<Ztree> roleFuncTreeData(Role role);

    /**
     * 查询所有菜单信息
     *
     * @return 菜单列表
     */
    public List<Ztree> funcTreeData();

    /**
     * 查询系统所有权限
     *
     * @return 权限列表
     */
    public Map<String, String> selectPermsAll();

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
     * 新增保存菜单信息
     *
     * @param func 菜单信息
     * @return 结果
     */
    public int insertFunc(Func func);

    /**
     * 修改保存菜单信息
     *
     * @param func 菜单信息
     * @return 结果
     */
    public int updateFunc(Func func);

    /**
     * 校验菜单名称是否唯一
     *
     * @param func 菜单信息
     * @return 结果
     */
    public String checkFuncNameUnique(Func func);
}
