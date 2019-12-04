package mapper;

/**
 * https://my.oschina.net/u/2427561/blog/3118537/print
 * @author beth
 * @data 2019-10-16 23:31
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest
public class UpdateTest {
  
  
//    @Autowired
//    private UserInfoMapper userInfoMapper;
//
//    /**
//     * 根据id跟新
//     */
//    @Test
//    public void updateById() {
//        UserInfo userInfo = new UserInfo();
//        userInfo.setId(1234);
//        userInfo.setAge(26);
//        userInfo.setEmail("788878@qq.com");
//        int rows = userInfoMapper.updateById(userInfo);
//        System.out.println("影响记录数："+rows);
//    }
//
//    /**
//     * 根据条件跟新
//     */
//    @Test
//    public void updateByWrapper() {
//        UpdateWrapper<UserInfo> updateWrapper = new UpdateWrapper<UserInfo>();
//        updateWrapper.eq("username","肖娟").eq("age",20);
//        UserInfo userInfo = new UserInfo();
//        userInfo.setAge(26);
//        userInfo.setEmail("788878@qq.com");
//        int rows = userInfoMapper.update(userInfo,updateWrapper);
//        System.out.println("影响记录数："+rows);
//    }
//
//    /**
//     * 根据条件跟新
//     */
//    @Test
//    public void updateByWrapper2() {
//        UserInfo whereUser = new UserInfo();
//        whereUser.setUsername("肖娟");
//
//        UpdateWrapper<UserInfo> updateWrapper = new UpdateWrapper<UserInfo>(whereUser);
//        updateWrapper.eq("username","肖娟").eq("age",20);
//        UserInfo userInfo = new UserInfo();
//        userInfo.setAge(26);
//        userInfo.setEmail("788878@qq.com");
//        int rows = userInfoMapper.update(userInfo,updateWrapper);
//        System.out.println("影响记录数："+rows);
//    }
//
//    /**
//     * 根据条件跟新
//     */
//    @Test
//    public void updateByWrapper3() {
//        UpdateWrapper<UserInfo> updateWrapper = new UpdateWrapper<UserInfo>();
//        updateWrapper.eq("username","肖娟").eq("age",20).set("age",11);
//        int rows = userInfoMapper.update(null,updateWrapper);
//        System.out.println("影响记录数："+rows);
//    }
//
//    /**
//     * 根据条件跟新,使用lambda
//     */
//    @Test
//    public void updateByWrapperLambda() {
//        LambdaUpdateWrapper<UserInfo> lambdaUpdateWrapper = Wrappers.<UserInfo> lambdaUpdate();
//        lambdaUpdateWrapper.eq(UserInfo::getUsername,"肖姐").eq(UserInfo::getAge,30).set(UserInfo::getAge,11);
//        int rows = userInfoMapper.update(null,lambdaUpdateWrapper);
//        System.out.println("影响记录数："+rows);
//    }
//
//    /**
//     * 根据条件跟新,使用lambda链式修改
//     */
//    @Test
//    public void updateByWrapperLambdaChain() {
//       boolean update = new LambdaUpdateChainWrapper<UserInfo>(userInfoMapper).eq(UserInfo::getUsername,"肖姐").eq(UserInfo::getAge,30).set(UserInfo::getAge,11).update();
//       System.out.println(update);
//    }

}