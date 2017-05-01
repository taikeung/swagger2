#  Spring MVC中使用 Swagger2 构建Restful API
1. 添加swagger2相关依赖：
```
		<!--swagger2,springmvc中间包-->
		<dependency>
			<groupId>com.mangofactory</groupId>
			<artifactId>swagger-springmvc</artifactId>
			<version>1.0.2</version>
		</dependency>
		<!--swagger2包-->
		<dependency>
			<groupId>io.springfox</groupId>
			<artifactId>springfox-swagger2</artifactId>
			<version>2.4.0</version>
		</dependency>
		<!--swagger2生成web界面需要的包,swagger1需要导入页面相关的js,css,html,swagger2为了简化操作直接将这些打包这个包中-->
		<dependency>
			<groupId>io.springfox</groupId>
			<artifactId>springfox-swagger-ui</artifactId>
			<version>2.4.0</version>
		</dependency>
		<!--swagger2依赖的包-->
		<dependency>
			<groupId>com.fasterxml.jackson.core</groupId>
			<artifactId>jackson-annotations</artifactId>
			<version>2.8.3</version>
		</dependency>
		<dependency>
			<groupId>com.fasterxml.jackson.core</groupId>
			<artifactId>jackson-databind</artifactId>
			<version>2.8.3</version>
		</dependency>
		<dependency>
			<groupId>com.fasterxml.jackson.core</groupId>
			<artifactId>jackson-core</artifactId>
			<version>2.8.3</version>
		</dependency>
```

2.添加swagger2配置类
``` java
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
```

3.在springmvc配置文件中增加自动扫描swagger2的配置
```
<!-- 使用 Swagger Restful API文档时，添加此注解 -->
<mvc:default-servlet-handler />
<mvc:annotation-driven/>
```
或者
```
<mvc:resources location="classpath:/META-INF/resources/" mapping="swagger-ui.html"/>
<mvc:resources location="classpath:/META-INF/resources/webjars/" mapping="/webjars/**"/>
```
4.在springmvc的controller中添加swagger2相关的api注解：
``` java
package com.hw.control;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hw.entity.Result;
import com.hw.entity.User;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;

import io.swagger.annotations.ApiOperation;

@Api(value = "User控制器")
@Controller
@RequestMapping("/user")
public class UserController {
	@ApiOperation(value = "根据用户id查询用户信息", httpMethod = "GET", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = Result.class)
	@ResponseBody
	@RequestMapping(value = "/queryUserById", method = RequestMethod.GET, produces = "application/json")
	public Result queryUserById(
			@ApiParam(name = "userId", required = true, value = "用户Id")
			@RequestParam("userId") int userId, HttpServletRequest request) {
		User user = new User(userId, "andyhoo", 24);
		Result result = new Result();
		result.setCode(0);
		result.setData(user);
		result.setMessage("success");
		return result;
	}
}

```
5.通过web页面来查看apidoc:[查看样例](http://47.92.0.76:80/ptccservice/swagger-ui.html)

6.swagger2常用注解：
####springfox、swagger.annotations.*注解部分参数介绍
- @ApiIgnore 忽略注解标注的类或者方法，不添加到API文档中

- @ApiOperation 展示每个API基本信息
	value api名称
	notes 备注说明
- @ApiImplicitParam 用于规定接收参数类型、名称、是否必须等信息

    name 对应方法中接收参数名称
    value 备注说明
	required 是否必须 boolean
	paramType 参数类型 body、path、query、header、form中的一种
	body 使用@RequestBody接收数据 POST有效
	path 在url中配置{}的参数
	query 普通查询参数 例如 ?query=q ,jquery ajax中data设置的值也可以，例如 {query:”q”},springMVC中不需要添加注解接收
	header 使用@RequestHeader接收数据
	form 笔者未使用，请查看官方API文档
	dataType 数据类型，如果类型名称相同，请指定全路径，例如 dataType = “java.util.Date”，springfox会自动根据类型生成模型
- @ApiImplicitParams 包含多个@ApiImplicitParam

- @ApiModelProperty 对模型中属性添加说明，例如 上面的PageInfoBeen、BlogArticleBeen这两个类中使用，只能使用在类中。
	value 参数名称
	required 是否必须 boolean
	hidden 是否隐藏 boolean
	其他信息和上面同名属性作用相同，hidden属性对于集合不能隐藏，目前不知道原因
- @ApiParam 对单独某个参数进行说明，使用在类中或者controller方法中都可以。注解中的属性和上面列出的同名属性作用相同


附录：[官方文档http://www.baeldung.com/swagger-2-documentation-for-spring-rest-api](http://www.baeldung.com/swagger-2-documentation-for-spring-rest-api)
