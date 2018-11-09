ALTER TABLE app_customer ADD (checkStates INT(20) DEFAULT NULL);

ALTER TABLE app_customer ADD (thirdUserName VARCHAR(255) DEFAULT NULL,platform VARCHAR(255) DEFAULT NULL,thirdUserPw VARCHAR(255) DEFAULT NULL);

ALTER TABLE app_customer ADD (otcFrozenDate DATETIME NULL COMMENT '冻结日期',otcFrozenFlag VARCHAR(255) NULL COMMENT '冻结标识');

ALTER TABLE app_customer ADD (otcFrozenCout DECIMAL(20,10) NULL COMMENT 'otc取消次数');

ALTER TABLE app_customer ADD (otcCancelDate DATETIME NULL COMMENT 'OTC最新取消日期');