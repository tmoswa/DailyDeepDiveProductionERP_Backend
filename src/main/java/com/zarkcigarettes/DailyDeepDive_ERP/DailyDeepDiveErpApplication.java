package com.zarkcigarettes.DailyDeepDive_ERP;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Map;
@Slf4j
@SpringBootApplication
public class DailyDeepDiveErpApplication  {
	public static void main(String[] args) {
		SpringApplication.run(DailyDeepDiveErpApplication.class, args);
	}
	@Bean
	public RequestContextListener requestContextListener() {
		return new RequestContextListener();
	}
}
