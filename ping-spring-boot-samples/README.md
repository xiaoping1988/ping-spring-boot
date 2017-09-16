例子里用到的表如下:

    CREATE TABLE `sys_user` (
      `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID，主键，自增',
      `user_name` varchar(45) NOT NULL COMMENT '用户姓名',
      `user_account` varchar(45) NOT NULL COMMENT '用户账号',
      `user_age` int(11) NOT NULL COMMENT '用户年龄',
      `user_city` varchar(45) DEFAULT NULL COMMENT '用户所在城市',
      `create_time` datetime NOT NULL COMMENT '创建时间',
      `update_time` datetime DEFAULT NULL COMMENT '修改时间',
      PRIMARY KEY (`user_id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=102 DEFAULT CHARSET=utf8 COMMENT='用户表'