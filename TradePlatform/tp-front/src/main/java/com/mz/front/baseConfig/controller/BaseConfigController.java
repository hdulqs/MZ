package com.mz.front.baseConfig.controller;

import com.mz.redis.common.utils.RedisService;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/baseConfig")
public class BaseConfigController {
	private final static Logger log = Logger.getLogger(BaseConfigController.class);
	@Autowired
	private RedisService redisService;
	
	
}
