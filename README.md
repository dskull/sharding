# shardingsphere
shardingsphere学习

### 数据库相关

1. 使用docker创建数据库

   privileged=true作用：容器内的root跟宿主机的root权限一样，如果不设置，那么容器内的root只是宿主机的一个普通用户，进行磁盘挂载时会报错。

   ```shell
   docker run -d -p 3306:3306 --privileged=true -v /Users/username/docker/mysql/3306/data:/var/lib/mysql -v /Users/username/docker/mysql/3306/conf:/etc/mysql/conf.d -e MYSQL_ROOT_PASSWORD=123456 -e default-authentication-plugin=mysql_native_password --name mysql-master mysql:8.0;
   docker run -d -p 3307:3306 --privileged=true -v /Users/username/docker/mysql/3307/data:/var/lib/mysql -v /Users/username/docker/mysql/3307/conf:/etc/mysql/conf.d -e MYSQL_ROOT_PASSWORD=123456 -e default-authentication-plugin=mysql_native_password --name mysql-slave0 mysql:8.0;
   docker run -d -p 3308:3306 --privileged=true -v /Users/username/docker/mysql/3308/data:/var/lib/mysql -v /Users/username/docker/mysql/3308/conf:/etc/mysql/conf.d -e MYSQL_ROOT_PASSWORD=123456 --name mysql-slave1 mysql:8.0;
   docker run -d -p 3309:3306 --privileged=true -v /Users/username/docker/mysql/3309/data:/var/lib/mysql -v /Users/username/docker/mysql/3309/conf:/etc/mysql/conf.d -e MYSQL_ROOT_PASSWORD=123456 --name mysql-slave2 mysql:8.0;
   ```

2. 写入服务器mysql-master

   设置：

   1. 创建slave用户： 

      ```mysql
      CREATE USER 'mysql-slave'@'%';
      ```

   2. 设置密码：

      ```mysql
      alter user 'mysql-slave'@'%' identified with mysql_native_password by '123456';
      ```

   3. 授予复制权限：

      ```mysql
      grant replication slave on *.* to 'mysql-slave'@'%';
      ```

   4. 刷新权限：

      ```mysql
      FLUSH PRIVILEGES;
      ```

   5. 查询主服务器状态(从库使用)：

      ```mysql
      show master status;
      ```

      | File          | Position | Binlog_Do_DB | Binlog_Ignore_DB | Executed_Gtid_Set |
      | ------------- | -------- | ------------ | ---------------- | ----------------- |
      | binlog.000002 | 157      |              |                  |                   |

   6. 创建表

      ```mysql
      -- 主从配置
      CREATE TABLE `t_user` (
        `id` bigint NOT NULL,
        `uname` varchar(50) DEFAULT NULL,
        PRIMARY KEY (`id`)
      ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
      ```

      

3. 读取服务器mysql-slave0-2

   设置：

   1. 配置主从关系

      ```mysql
      change master to master_host = 'masterIp',master_user='mysql-slave', master_password='123456',master_port=3306,master_log_file='binlog.000002', master_log_pos=157;
      ```

   2. 启动主从：

      ```mysql
       start slave;
      ```

   3. 查看状态：

      ```mysql
      show slave status\G
      *************************** 1. row ***************************
                     Slave_IO_State: Waiting for source to send event
                        Master_Host: masterIp
                        Master_User: mysql-slave
                        Master_Port: 3306
                      Connect_Retry: 60
                    Master_Log_File: binlog.000002
                Read_Master_Log_Pos: 1069
                     Relay_Log_File: b162cc13c020-relay-bin.000002
                      Relay_Log_Pos: 1235
              Relay_Master_Log_File: binlog.000002
                   Slave_IO_Running: Yes
                  Slave_SQL_Running: Yes
                    Replicate_Do_DB:
                Replicate_Ignore_DB:
                 Replicate_Do_Table:
             Replicate_Ignore_Table:
            Replicate_Wild_Do_Table:
        Replicate_Wild_Ignore_Table:
                         Last_Errno: 0
                         Last_Error:
                       Skip_Counter: 0
                Exec_Master_Log_Pos: 1069
                    Relay_Log_Space: 1452
                    Until_Condition: None
                     Until_Log_File:
                      Until_Log_Pos: 0
                 Master_SSL_Allowed: No
                 Master_SSL_CA_File:
                 Master_SSL_CA_Path:
                    Master_SSL_Cert:
                  Master_SSL_Cipher:
                     Master_SSL_Key:
              Seconds_Behind_Master: 0
      Master_SSL_Verify_Server_Cert: No
                      Last_IO_Errno: 0
                      Last_IO_Error:
                     Last_SQL_Errno: 0
                     Last_SQL_Error:
        Replicate_Ignore_Server_Ids:
                   Master_Server_Id: 1
                        Master_UUID: 1da7e007-ada4-11ed-9e76-0242ac110002
                   Master_Info_File: mysql.slave_master_info
                          SQL_Delay: 0
                SQL_Remaining_Delay: NULL
            Slave_SQL_Running_State: Replica has read all relay log; waiting for more updates
                 Master_Retry_Count: 86400
                        Master_Bind:
            Last_IO_Error_Timestamp:
           Last_SQL_Error_Timestamp:
                     Master_SSL_Crl:
                 Master_SSL_Crlpath:
                 Retrieved_Gtid_Set:
                  Executed_Gtid_Set:
                      Auto_Position: 0
               Replicate_Rewrite_DB:
                       Channel_Name:
                 Master_TLS_Version:
             Master_public_key_path:
              Get_master_public_key: 0
                  Network_Namespace:
      1 row in set, 1 warning (0.00 sec)
      ```

   4. 创建数据表

      ```mysql
      -- 读写分离 垂直分库 水平分库order$->{0..1}
      CREATE TABLE `t_order` (
        `id` bigint NOT NULL AUTO_INCREMENT,
        `order_no` varchar(30) DEFAULT NULL,
        `user_id` bigint DEFAULT NULL,
        `amount` decimal(10,2) DEFAULT NULL,
        PRIMARY KEY (`id`)
      ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
      ```

      



- 读写分离

  主库负责写入，多个从库负责查询。优点：提高系统的查询性能，提供系统的吞吐量以及可用性。

- 垂直分片

  将具体的业务表拆分到不同的数据库中，达到专库专用的效果。

  如果某个库中某几个表的数据量过大，仍然无法解决单节点性能瓶颈。

  创建两个业务数据库

  ```mysql
  docker run -d -p 3310:3306 --privileged=true -v /Users/username/docker/mysql/3310/data:/var/lib/mysql -v /Users/username/docker/mysql/3310/conf:/etc/mysql/conf.d -e MYSQL_ROOT_PASSWORD=123456 --name mysql-order mysql:8.0;
  docker run -d -p 3311:3306 --privileged=true -v /Users/username/docker/mysql/3311/data:/var/lib/mysql -v /Users/username/docker/mysql/3311/conf:/etc/mysql/conf.d -e MYSQL_ROOT_PASSWORD=123456 --name mysql-user mysql:8.0;
  ```

创建数据表

```mysql
-- 水平分表 水平分库分表
CREATE TABLE `t_order0` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_no` varchar(30) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `amount` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE `t_order1` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_no` varchar(30) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `amount` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
```

- 水平分片

  通过一个或多个字段将数据分散到多个库或是表中

  其优点，理论上解决了单机数据量处理的瓶颈，扩展自由，是数据分片的标准方案。

  其缺点，数据变得散乱，不容易查找。某些sql无法在分片数据表中运行，引入了分布式事务的问题。

  ```shell
  docker run -d -p 3312:3306 --privileged=true -v /Users/username/docker/mysql/3312/data:/var/lib/mysql -v /Users/username/docker/mysql/3312/conf:/etc/mysql/conf.d -e MYSQL_ROOT_PASSWORD=123456 --name mysql-order0 mysql:8.0;
  docker run -d -p 3313:3306 --privileged=true -v /Users/username/docker/mysql/3313/data:/var/lib/mysql -v /Users/username/docker/mysql/3313/conf:/etc/mysql/conf.d -e MYSQL_ROOT_PASSWORD=123456 --name mysql-order1 mysql:8.0;
  ```

- 数据脱敏。仅在数据库中脱敏，查询后的信息依然为明文。

  创建数据库

  ```shell
  docker run -d -p 3314:3306 --privileged=true -v /Users/username/docker/mysql/3314/data:/var/lib/mysql -v /Users/username/docker/mysql/3314/conf:/etc/mysql/conf.d -e MYSQL_ROOT_PASSWORD=123456 --name mysql-encrypt mysql:8.0;
  ```
  
- 影子库。压测

  创建影子库

  ```shell
  docker run -d -p 3315:3306 --privileged=true -v /Users/username/docker/mysql/3315/data:/var/lib/mysql -v /Users/username/docker/mysql/3315/conf:/etc/mysql/conf.d -e MYSQL_ROOT_PASSWORD=123456 --name mysql-shadow mysql:8.0;
  ```

- proxy测试

```shell
docker run -d -v /Users/username/docker/shardingsphere-proxy-a/conf:/opt/shardingsphere-proxy/conf -v /Users/username/docker/shardingsphere-proxy-a/ext-lib:/opt/shardingsphere-proxy/ext-lib -e ES_JAVA_OPTS="-Xmx256m -Xms256m -Xmn128m" -p 3321:3307 --name server-proxy-a apache/shardingsphere-proxy:5.0.0
```

