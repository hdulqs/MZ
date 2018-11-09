CREATE TABLE app_member_point(
   `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `created` DATETIME DEFAULT NULL,
  `modified` DATETIME DEFAULT NULL,
  `saasId` VARCHAR(255) DEFAULT NULL,
  `isDelete` VARCHAR(255) DEFAULT NULL,
  `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
   recomm_customerId BIGINT(20) DEFAULT NULL COMMENT "推荐人",
   customerId BIGINT(20) DEFAULT NULL COMMENT "用户id",
   member_point DECIMAL(20,10) NOT NULL DEFAULT 0,
   recomm_point DECIMAL(20,10) NOT NULL DEFAULT 0,
  `content` TEXT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT "推荐积分流水表"
 