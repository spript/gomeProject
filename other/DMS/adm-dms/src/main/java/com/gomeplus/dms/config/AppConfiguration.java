// package com.gomeplus.dms.config;

// import org.springframework.boot.web.servlet.FilterRegistrationBean;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.filter.DelegatingFilterProxy;

// @Configuration
// public class AppConfiguration {

// 	/**
// 	 * 添加shiroFilter过滤器
// 	 * @return
// 	 */
// 	@Bean
// 	public FilterRegistrationBean delegatingFilterProxy() {
// 		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
// 		DelegatingFilterProxy delegatingFilterProxy = new DelegatingFilterProxy();
// 		delegatingFilterProxy.setTargetFilterLifecycle(true);
// 		delegatingFilterProxy.setTargetBeanName("shiroFilter");
// 		registrationBean.setFilter(delegatingFilterProxy);
// 		registrationBean.addUrlPatterns("/*");
// 		registrationBean.setName("shiroFilter");
// 		return registrationBean;
// 	}
// }
