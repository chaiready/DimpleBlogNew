package com.dimple.project.king.userinfo;

import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@TableName("user_info")
public class UserInfo {
  // CREATE TABLE user_info (
  // id BIGINT(20) PRIMARY KEY NOT NULL COMMENT '主键',
  // name VARCHAR(30) DEFAULT NULL COMMENT '姓名',
  // age INT(11) DEFAULT NULL COMMENT '年龄',
  // email VARCHAR(50) DEFAULT NULL COMMENT '邮箱',
  // manager_id BIGINT(20) DEFAULT NULL COMMENT '直属上级id',
  // create_time DATETIME DEFAULT NULL COMMENT '创建时间'
  // ) ENGINE=INNODB CHARSET=UTF8;

  // 主键
  @TableId
  private Long id;
  // 姓名
  @TableField(value="name")//,condition=SqlCondition.LIKE
  private String name;
  // 年龄
  private Integer age;
  // 邮箱
  private String email;
  // 直属上级
  private Long managerId;
  // 创建时间
  private LocalDateTime createTime;
  
//  方式一：加上transient关键词：（为null）如：
//  private transient String 属性名；(不参与序列化)
//     方式二：使用静态变量static:（全类唯一一份，不符合要求）如：
//  private static String 属性名 并设置静态的get，
//  set方法
//  方式三：使用 @TableField(exist=false):（每个对象有自己单独的一份）
  @TableField(exist=false)
  private String remark;
  
  
}
