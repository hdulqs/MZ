CREATE TABLE app_member_point_config(
   `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `isopen` INT(3) DEFAULT 1 COMMENT '1 启用,0 未启用',
  `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
   recomm_point DECIMAL(20,10) NOT NULL DEFAULT 0 COMMENT "推荐积分",
   `recomm_isopen` INT(3) DEFAULT 1 COMMENT '1 启用,0 未启用',
   customerId BIGINT(20) NOT NULL COMMENT "用户id",
   regist_point DECIMAL(20,10) NOT NULL DEFAULT 0 COMMENT "注册积分",
   `regist_isopen` INT(3) DEFAULT 1 COMMENT '1 启用,0 未启用',
   `created` DATETIME DEFAULT NULL,
  `modified` DATETIME DEFAULT NULL,
  `saasId` VARCHAR(255) DEFAULT NULL,
  `content` TEXT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT "推荐积分配置表"
 