package com.gomeplus.dms.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainController {
	
	private static Logger logger = LogManager.getLogger(MainController.class);
	@Value("${spring.application.serverName}")
	private String serverName;

	@Value("${spring.profiles.active}")
	private String profile;

	/**
	 * DMS首页
	 * @return
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(HttpServletRequest servletRequest) {

		String forwardedHost = servletRequest.getHeader("x-forwarded-host");
		logger.info("serverName :"+serverName);
		logger.info("forwardedHost :"+forwardedHost);
		logger.info("profile :"+profile);
		// @todo
		// 判断请求是否来自ngnix代理
		// 兼容adportal跳转错误问题
		if (!serverName.equals(forwardedHost)) {
			return "redirect:http://" + serverName;
		} 
		
		return profile + "/index";
	}
}