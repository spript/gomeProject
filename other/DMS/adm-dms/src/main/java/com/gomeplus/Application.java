package com.gomeplus;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ComponentScan
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
//@ImportResource("classpath:spring-client.xml")
public class Application {

	public static void main(String[] args) {

		String environment = System.getenv().get("ENVIRONMENT");
		if (environment == null) {
			environment = "development";
		}

		new SpringApplicationBuilder()
				.profiles(environment)
				.sources(Application.class)
				.run(args);
	}

	/**
	 * Enabling CORS
	 */
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**");
			}
		};
	}
}
