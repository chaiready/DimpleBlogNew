package com.dimple.project.king.func.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dimple.common.constant.UserConstants;
import com.dimple.common.utils.StringUtils;
import com.dimple.common.utils.security.ShiroUtils;
import com.dimple.framework.web.domain.Ztree;
import com.dimple.project.king.func.domain.Func;
import com.dimple.project.king.func.mapper.FuncMapper;
import com.dimple.project.king.util.TreeModelUtils;
import com.dimple.project.system.role.domain.Role;
import com.dimple.project.system.user.domain.User;

/**
 * @className: FuncServiceImpl
 * @description: 菜单 业务层处理
 * @auther: Dimple
 * @Date: 2019/3/13
 * @Version: 1.1
 */
@Service
public class FuncServiceImpl implements IFuncService {
	
    public static final String PREMISSION_STRING = "perms[\"{0}\"]";

    @Autowired
    private FuncMapper funcMapper;


    /**
     * 根据用户查询菜单
     *
     * @param user 用户信息
     * @return 菜单列表
     */
    @Override
    public List<Func> selectFuncsByUser(User user) {
        List<Func> funcs = new LinkedList<Func>();
        // 管理员显示所有菜单信息
        if (user.isAdmin()) {
            funcs = funcMapper.selectFuncNormalAll();
        } else {
            funcs = funcMapper.selectFuncsByUserId(user.getUserId());
        }
        return TreeModelUtils.getChildPerms(funcs, 0);
    }

    /**
     * 查询菜单集合
     *
     * @return 所有菜单信息
     */
    @Override
    public List<Func> selectFuncList(Func func) {
        User user = ShiroUtils.getSysUser();
        if (!user.isAdmin()) {
            func.setCreator(ShiroUtils.getUserId());
        }
        return funcMapper.selectFuncList(func);
    }

    /**
     * 查询菜单集合
     *
     * @return 所有菜单信息
     */
    @Override
    public List<Func> selectFuncAll() {
        return funcMapper.selectFuncAll();
    }

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectPermsByUserId(Long userId) {
        List<String> perms = funcMapper.selectPermsByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms) {
            if (StringUtils.isNotEmpty(perm)) {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 根据角色ID查询菜单
     *
     * @param role 角色对象
     * @return 菜单列表
     */
    @Override
    public List<Ztree> roleFuncTreeData(Role role) {
        Long roleId = role.getRoleId();
        List<Ztree> ztrees = new ArrayList<Ztree>();
        List<Func> funcList = funcMapper.selectFuncAll();
        if (StringUtils.isNotNull(roleId)) {
            List<String> roleFuncList = funcMapper.selectFuncTree(roleId);
            ztrees = initZtree(funcList, roleFuncList, true);
        } else {
            ztrees = initZtree(funcList, null, true);
        }
        return ztrees;
    }

    /**
     * 查询所有菜单
     *
     * @return 菜单列表
     */
    @Override
    public List<Ztree> funcTreeData() {
        List<Func> funcList = funcMapper.selectFuncAll();
        List<Ztree> ztrees = initZtree(funcList);
        return ztrees;
    }

    /**
     * 查询系统所有权限
     *
     * @return 权限列表
     */
    @Override
    public LinkedHashMap<String, String> selectPermsAll() {
        LinkedHashMap<String, String> section = new LinkedHashMap<>();
        List<Func> permissions = funcMapper.selectFuncAll();
        if (StringUtils.isNotEmpty(permissions)) {
            for (Func func : permissions) {
                section.put(func.getUrl(), MessageFormat.format(PREMISSION_STRING, func.getPerms()));
            }
        }
        return section;
    }

    /**
     * 对象转菜单树
     *
     * @param funcList 菜单列表
     * @return 树结构列表
     */
    public List<Ztree> initZtree(List<Func> funcList) {
        return initZtree(funcList, null, false);
    }

    /**
     * 对象转菜单树
     *
     * @param funcList     菜单列表
     * @param roleFuncList 角色已存在菜单列表
     * @param permsFlag    是否需要显示权限标识
     * @return 树结构列表
     */
    public List<Ztree> initZtree(List<Func> funcList, List<String> roleFuncList, boolean permsFlag) {
        List<Ztree> ztrees = new ArrayList<Ztree>();
        boolean isCheck = StringUtils.isNotNull(roleFuncList);
        for (Func func : funcList) {
            Ztree ztree = new Ztree();
            ztree.setId(func.getFuncId());
            ztree.setpId(func.getParentId());
            ztree.setName(transFuncName(func, roleFuncList, permsFlag));
            ztree.setTitle(func.getFuncName());
            if (isCheck) {
                ztree.setChecked(roleFuncList.contains(func.getFuncId() + func.getPerms()));
            }
            ztrees.add(ztree);
        }
        return ztrees;
    }

    public String transFuncName(Func func, List<String> roleFuncList, boolean permsFlag) {
        StringBuffer sb = new StringBuffer();
        sb.append(func.getFuncName());
        if (permsFlag) {
            sb.append("<font color=\"#888\">&nbsp;&nbsp;&nbsp;" + func.getPerms() + "</font>");
        }
        return sb.toString();
    }

    /**
     * 删除菜单管理信息
     *
     * @param funcId 菜单ID
     * @return 结果
     */
    @Override
    public int deleteFuncById(Long funcId) {
        ShiroUtils.clearCachedAuthorizationInfo();
        return funcMapper.deleteFuncById(funcId);
    }

    /**
     * 根据菜单ID查询信息
     *
     * @param funcId 菜单ID
     * @return 菜单信息
     */
    @Override
    public Func selectFuncById(Long funcId) {
        return funcMapper.selectFuncById(funcId);
    }

    /**
     * 查询子菜单数量
     *
     * @param parentId 菜单ID
     * @return 结果
     */
    @Override
    public int selectCountFuncByParentId(Long parentId) {
        return funcMapper.selectCountFuncByParentId(parentId);
    }


    /**
     * 新增保存菜单信息
     *
     * @param func 菜单信息
     * @return 结果
     */
    @Override
    public int insertFunc(Func func) {
        User user = ShiroUtils.getSysUser();
        func.setCreator(ShiroUtils.getUserId());
        ShiroUtils.clearCachedAuthorizationInfo();
        return funcMapper.insertFunc(func);
    }

    /**
     * 修改保存菜单信息
     *
     * @param func 菜单信息
     * @return 结果
     */
    @Override
    public int updateFunc(Func func) {
        func.setUpdateBy(ShiroUtils.getLoginName());
        ShiroUtils.clearCachedAuthorizationInfo();
        return funcMapper.updateFunc(func);
    }

    /**
     * 校验菜单名称是否唯一
     *
     * @param func 菜单信息
     * @return 结果
     */
    @Override
    public String checkFuncNameUnique(Func func) {
        Long funcId = StringUtils.isNull(func.getFuncId()) ? -1L : func.getFuncId();
        Func info = funcMapper.checkFuncNameUnique(func.getFuncName(), func.getParentId());
        if (StringUtils.isNotNull(info) && info.getFuncId().longValue() != funcId.longValue()) {
            return UserConstants.MENU_NAME_NOT_UNIQUE;
        }
        return UserConstants.MENU_NAME_UNIQUE;
    }

	@Override
	public List<Func> findByCreator(String loginName) {
		 return funcMapper.findByCreator(loginName);
	}

    @Override
    public List<Func> findBbsByCreator(String loginName) {
        List<Func> funcList = findByCreator(loginName);
        List<Func> defaultFucs = new ArrayList<Func>();
        for (Func func : funcList) {
            func.setUrl("/bbs/" + loginName + func.getUrl());
            defaultFucs.add(func);
        }
        return defaultFucs;
    }

}
