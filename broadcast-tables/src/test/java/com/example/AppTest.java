package com.example;

import com.example.entity.Dict;
import com.example.mapper.DictMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Unit test for simple App.
 */
@SpringBootTest
public class AppTest
{
    @Autowired
    private DictMapper dictMapper;

    @Test
    public void insertTest() {
        Dict dict = new Dict();
        dict.setDictType("广播测试");
        dictMapper.insert(dict);
    }
}
