package com.jws.common.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * 修改spring配置，使得RequestMappingHandlerMapping能够加载
 * 
 */
@Configuration
public class SpringMvcConfig extends WebMvcConfigurationSupport {

	/**
	 * Return a {@link RequestMappingHandlerMapping} ordered at 0 for mapping
	 * requests to annotated controllers.
	 */
	@Override
	@Bean
	public RequestMappingHandlerMapping requestMappingHandlerMapping() {
		
		RequestMappingHandlerMapping handlerMapping = new RequestMappingHandlerMapping();
		handlerMapping.setOrder(0);
		handlerMapping.setInterceptors(getInterceptors());
		
		// 扫描父容器中的bean
		handlerMapping.setDetectHandlerMethodsInAncestorContexts(true);
		return handlerMapping;
	}

}
