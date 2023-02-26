package com.example;

import com.example.entity.User;
import com.example.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * Unit test for simple App.
 */
@SpringBootTest
public class AppTest
{
    @Autowired
    private UserMapper userMapper;
    @Test
    public void loadTest() {
        User user = new User();
        user.setUname("读写分离");
        // 主库写
        userMapper.insert(user);

        // 从库读
        // @Transactional 主库写
        userMapper.selectList(null);
    }
}
