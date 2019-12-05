package com.dimple.project.king.userinfo.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dimple.project.king.userinfo.UserInfo;

public interface UserInfoMapper extends BaseMapper<UserInfo>{
  
//使用SQL语句自定义条件    的方法
  @Select("select * from user ${ew.customSqlSegment}")
  List<UserInfo> selectAll(@Param("ew")Wrapper<UserInfo> wrapper);
//  List<UserInfo> selectAll(@Param(Constants.WRAPPER)Wrapper<UserInfo> wrapper);
}