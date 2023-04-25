package com.zql.springboot.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author：zql
 * @date: 2023/4/25
 */
@Configuration
@EnableOpenApi
public class SwaggerConfig {
        @Bean
        public Docket docket() {
            Docket docket = new Docket(DocumentationType.OAS_30)
                    .apiInfo(apiInfo()).enable(true)
                    .select()
                    //apis： 添加swagger接口提取范围
                    .apis(RequestHandlerSelectors.basePackage("com.zql.springboot.demo.controller"))
                    .paths(PathSelectors.any())
                    .build();
            return docket;
        }

        private ApiInfo apiInfo() {
            return new ApiInfoBuilder()
                    .title("SpringBoot-Demo")
                    .description("记录一些有用的SpringBoot案例")
                    .contact(new Contact("Echo", "https://github.com/Echoidf/JavaCode","zql46@outlook.com"))
                    .version("v1.0")
                    .build();
        }
}
