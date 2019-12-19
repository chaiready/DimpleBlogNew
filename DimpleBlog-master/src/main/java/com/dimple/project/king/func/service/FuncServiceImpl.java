package com.dimple.project.king.func.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dimple.common.utils.StringUtils;
import com.dimple.common.utils.security.ShiroUtils;
import com.dimple.project.king.func.domain.Func;
import com.dimple.project.king.func.mapper.FuncMapper;
import com.dimple.project.system.user.domain.User;

/**
 * @className: FuncServiceImpl
 * @description: 菜单 业务层处理
 * @auther: Dimple
 * @Date: 2019/3/13
 * @Version: 1.1
 */
@Service
public class FuncServiceImpl extends ServiceImpl<FuncMapper, Func> implements IFuncService {

  public static final String PREMISSION_STRING = "perms[\"{0}\"]";

  @Autowired
  private FuncMapper funcMapper;

  /**
   * 新增保存菜单信息
   *
   * @param func 菜单信息
   * @return 结果
   */
  @Override
  public int insertFunc(Func func) {
    if(StringUtils.isEmpty(func.getCreateBy())){
      User user = ShiroUtils.getSysUser();
      func.setCreator(user.getUserId());
      func.setCreateBy(user.getLoginName());
    }
    func.setOrderNum(System.currentTimeMillis());
    funcMapper.insert(func);
    func.setUrl("/func/" + func.getId() + ".html");
    return funcMapper.updateById(func);
  }

  /**
   * 修改保存菜单信息
   *
   * @param func 菜单信息
   * @return 结果
   */
  @Override
  public int updateFunc(Func func) {
    Func entity = new Func();
    entity.setId(func.getId());
    entity.setFuncName(func.getFuncName());
    entity.setUpdateBy(ShiroUtils.getLoginName());
    entity.setUpdateTime(new Date());
    return funcMapper.updateById(entity);
  }

  @Override
  public int toFirst(Long funcId) {
    Func entity = new Func();
    entity.setId(funcId);
    entity.setOrderNum(System.currentTimeMillis());
    entity.setUpdateBy(ShiroUtils.getLoginName());
    entity.setUpdateTime(new Date());
    return funcMapper.updateById(entity);
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
   * 查询子菜单数量
   *
   * @param parentId 菜单ID
   * @return 结果
   */
  @Override
  public int selectCountFuncByParentId(Long parentId) {
    return funcMapper.selectCountFuncByParentId(parentId);
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
