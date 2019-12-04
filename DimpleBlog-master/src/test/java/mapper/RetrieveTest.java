package mapper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dimple.DimpleBlogApplication;
import com.dimple.project.king.userinfo.UserInfo;
import com.dimple.project.king.userinfo.mapper.UserInfoMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DimpleBlogApplication.class)
public class RetrieveTest {

  @Autowired
  private UserInfoMapper userMapper;

  /**
   * 根据Id查询
   */
  @Test
  public void selectById() {
    UserInfo user = userMapper.selectById(1202099648227586050L);
    System.out.println(user);
  }

  /**
   * 根据Id集合查询
   */
  @Test
  public void selectIds() {
    List<Long> idsList =
        Arrays.asList(1088248166370832385L, 1088250446457389058L, 1094590409767661570L);// 获取id集合
    List<UserInfo> userList = userMapper.selectBatchIds(idsList);
    userList.forEach(System.out::println);
  }

  @Test
  public void selectByMap() {
    Map<String, Object> columnMap = new HashMap<>();
    // columnMap.put("name","王天风"); //必须与数据库中的对应，如果没有会报错
    columnMap.put("age", 27); // 键是数据库中的列 where age= 27
    List<UserInfo> userList = userMapper.selectByMap(columnMap);
    userList.forEach(System.out::println);
  }

  /**
   * 条件构造器查询 需求： 名字中包含雨并且年龄小于40 name like '%雨%' and age<40
   */
  @Test
  public void selectByWrapper1() {
    QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<UserInfo>();
    queryWrapper.like("name", "雨").lt("age", 40);
    List<UserInfo> userList = userMapper.selectList(queryWrapper);
    userList.forEach(System.out::println);
  }

  /**
   * 条件构造器查询 需求2： 名字中包含雨并且年龄大于等于20且小于等于40并且email不为空 name like '%雨%' and age between 20 and 40 and
   * email is not null
   */
  @Test
  public void selectByWrapper2() {
    QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<UserInfo>();
    List<UserInfo> userList = userMapper.selectList(queryWrapper);
    userList.forEach(System.out::println);
  }

  /**
   * 条件构造器查询 需求3： 名字为王姓或者年龄大于等于25，按照年龄降序排列，年龄相同按照id升序排列； name like '王%' or age>= 25 order by age
   * desc,id asc
   */
  @Test
  public void selectByWrapper3() {
    QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<UserInfo>();
    queryWrapper.likeRight("name", "王").or().ge("age", 25).orderByDesc("age").orderByAsc("id");
    List<UserInfo> userList = userMapper.selectList(queryWrapper);
    userList.forEach(System.out::println);
  }

  /**
   * 条件构造器查询 需求4： 创建日期为2019年2月14日并且直属上级为名字为王姓 data_format(craete_time,'$Y-%m-%d')and manager_id in
   * (select id from user where name like '王%')
   */
  @Test
  public void selectByWrapper4() {
    QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<UserInfo>();
    queryWrapper.apply("date_format(create_time,'%Y-%m-%d')=2019-02-14").inSql("manager_id",
        "select id from user_info where name like '王%'");
    List<UserInfo> userList = userMapper.selectList(queryWrapper);
    userList.forEach(System.out::println);
  }

  /**
   * 条件构造器查询 需求5： 名字为王姓并且（年龄小于40或邮箱不为空） name like '王%' and (age<40 or email is not null)
   */
  @Test
  public void selectByWrapper5() {
    QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<UserInfo>();
    queryWrapper.likeRight("name", "王").and(wq -> wq.lt("age", 40).or().isNotNull("email"));
    List<UserInfo> userList = userMapper.selectList(queryWrapper);
    userList.forEach(System.out::println);
  }

  /**
   * 条件构造器查询 需求6： 名字为王姓或者（年龄小于40并且年龄大于20并且邮箱不为空） name like '王%' or (age<40 and age>20 and email is
   * not null)
   */
  @Test
  public void selectByWrapper5b() {
    QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<UserInfo>();
    queryWrapper.likeRight("name", "王").or(wq -> wq.lt("age", 40).gt("age", 20).isNotNull("email"));
    List<UserInfo> userList = userMapper.selectList(queryWrapper);
    userList.forEach(System.out::println);
  }


  /**
   * 条件构造器查询 需求7： (年龄小于40或邮箱不为空)并且名字为王姓 (age<40 or email is not null) and name like '王%'
   */
  @Test
  public void selectByWrapper7() {
    QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<UserInfo>();
    queryWrapper.nested(wq -> wq.lt("age", 40).or().isNotNull("email")).likeRight("name", "王");
    List<UserInfo> userList = userMapper.selectList(queryWrapper);
    userList.forEach(System.out::println);
  }

  /**
   * 条件构造器查询 需求8： 年龄为30、31、34、35 age in (30、31、34、35)
   */
  @Test
  public void selectByWrapper8() {
    QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<UserInfo>();
    queryWrapper.in("age", Arrays.asList(30, 31, 34, 35));
    List<UserInfo> userList = userMapper.selectList(queryWrapper);
    userList.forEach(System.out::println);
  }

  /**
   * 条件构造器查询 需求9：只返回满足条件的其中一条语句即可 limit 1
   */
  @Test
  public void selectByWrapper9() {
    QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<UserInfo>();
    queryWrapper.in("age", Arrays.asList(30, 31, 34, 35)).last("limit 1");
    List<UserInfo> userList = userMapper.selectList(queryWrapper);
    userList.forEach(System.out::println);
  }

  /**
   * 条件构造器查询 需求10： 名字中包含雨并且年龄小于40 只查询id,name两个字段 name like '%雨%' and age<40
   */
  @Test
  public void selectByWrapper10() {
    QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<UserInfo>();
    queryWrapper.select("id", "name").like("name", "雨").lt("age", 40);
    List<UserInfo> userList = userMapper.selectList(queryWrapper);
    userList.forEach(System.out::println);
  }


  /**
   * 条件构造器查询 需求10： 名字中包含雨并且年龄小于40 只查询部分字段使用排除法 select id,name,age,email from user where like '%雨%'
   * and age<40
   */
  @Test
  public void selectByWrapper10b() {
    QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<UserInfo>();
    queryWrapper.like("name", "雨").lt("age", 40).select(UserInfo.class,
        info -> !info.getColumn().equals("create_time") && !info.getColumn().equals("manager_id"));
    List<UserInfo> userList = userMapper.selectList(queryWrapper);
    userList.forEach(System.out::println);
  }


  /**
   * condition作用：当它的值为true的时候，这个方法才会执行
   */
  @Test
  public void testCondition() {
    String name = "王";
    String email = "";
    QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<UserInfo>();
    // if(StringUtils.isNotEmpty(name)){
    // queryWrapper.like("name",name);
    // }
    // if(StringUtils.isNotEmpty(email)){
    // queryWrapper.like("email",email);
    // }
    // 下面的写法可以替代上面的这两个
    queryWrapper.like(StringUtils.isNotEmpty(name), "name", name)
        .like(StringUtils.isNotEmpty(email), "email", email);
    List<UserInfo> userList = userMapper.selectList(queryWrapper);
    userList.forEach(System.out::println);
  }

  /**
   * 实体作为条件构造器构造方法的参数
   */
  @Test
  public void selectByWrapperEntity() {
    UserInfo whereUser = new UserInfo();
    whereUser.setName("刘红雨");
    whereUser.setAge(32);
    QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<UserInfo>(whereUser);
    queryWrapper.like("name", "雨").lt("age", 40); // 这条语句写上，会与whereUser这条同时生效；
    List<UserInfo> userList = userMapper.selectList(queryWrapper);
    userList.forEach(System.out::println);
  }

  
  @Test
  public void selectByWrapperAllEq(){
      QueryWrapper<UserInfo> queryWrapper=new QueryWrapper<UserInfo>();
      Map<String,Object> params=new HashMap<String,Object>();
      params.put("name","王天风");
      params.put("age",25);
      //queryWrapper.allEq(params);
      //过滤查询
      queryWrapper.allEq((k,v)->!k.equals("name"),params);
      List<UserInfo> userList=userMapper.selectList(queryWrapper);
      userList.forEach(System.out::println);
  }

  /**
   * 其他使用条件构造器的方法
   */
  @Test
  public void selectOthereConstruct(){
    QueryWrapper<UserInfo> queryWrapper=new QueryWrapper<UserInfo>();
    queryWrapper.select("id","name").like("name","雨").lt("age",40);
    //使用selectMaps，返回的结果是键值对，即Key为字段名，value为值名；
    //使用了select指定了id,name,则返回的只有这两个数据；如果非selectMaps方式，则返回的结果，非id,name的字段会为null，而现在的没有此字段，更好看一点；
    List<Map<String,Object>> userList=userMapper.selectMaps(queryWrapper);
    userList.forEach(System.out::println);
  }
  
}
