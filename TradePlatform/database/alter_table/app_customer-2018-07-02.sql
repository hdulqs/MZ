ALTER TABLE app_customer  ADD(mailStates INT(20) DEFAULT '0' COMMENT "绑定邮箱标识 0 未绑定 1 绑定");
ALTER TABLE app_customer  ADD(mail VARCHAR(100) COLLATE utf8_bin DEFAULT NULL COMMENT "绑定的邮箱");