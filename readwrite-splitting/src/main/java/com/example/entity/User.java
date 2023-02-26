package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@TableName(value = "t_user")
@Getter
@Setter
@ToString
public class User {
    private Long id;
    private String uname;
}
