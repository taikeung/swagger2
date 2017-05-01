package com.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableWebMvc
@Configuration
@EnableSwagger2
public class SwaggerConfig {
	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
				.select()
				// 扫描特定的包下面的注解
				.apis(RequestHandlerSelectors.basePackage("com.hw.control"))
				.paths(PathSelectors.any()).build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("Swagger2 RESTful APIs")
				.description("swagger2,springmvc构建restful风格api")
				.termsOfServiceUrl("https://github.com/AndyHooo")
				.contact(
						new Contact("AndyHoo",
								"https://github.com/AndyHooo",
								"396877565@qq.com")).version("1.0").build();
	}
}