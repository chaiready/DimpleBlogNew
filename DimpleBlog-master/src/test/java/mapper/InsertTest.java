package mapper;

import java.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.dimple.DimpleBlogApplication;
import com.dimple.project.king.userinfo.UserInfo;
import com.dimple.project.king.userinfo.mapper.UserInfoMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DimpleBlogApplication.class)
public class InsertTest {

  @Autowired
  private UserInfoMapper userMapper;

  @Test
  public void insert() {
    UserInfo user = new UserInfo();
    user.setName("刘明强");
    user.setAge(31);
    user.setManagerId(1088248166370832385L);
    user.setCreateTime(LocalDateTime.now());
    int rows = userMapper.insert(user);
    System.out.println("影响记录数" + rows);
  }
  
}
