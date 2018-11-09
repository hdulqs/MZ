ALTER TABLE c2c_transaction ADD (handleId  BIGINT(20) DEFAULT NULL  COMMENT '管理人ID',handleName  VARCHAR(255) NULL COMMENT '管理人用户名' ,handleIp  VARCHAR(255) NULL COMMENT '管理人IP');
