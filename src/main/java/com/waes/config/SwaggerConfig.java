package com.waes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by aandra1 on 30/09/16.
 */
@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket companyAdminApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.waes.controllers.api"))
                .paths(PathSelectors.ant("/api/**"))
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Json Util - Web API")
                .description("Json Util - WEB API based on REST to compare two json files")
                .contact("Antonio Reuter")
                .license("Apache License Version 2.0")
                .version("1.0")
                .build();
    }
}