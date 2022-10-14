package com.atmmachine.configuration;

import com.atmmachine.entity.Account;
import com.fasterxml.classmate.TypeResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfiguration {

    @Autowired
    private TypeResolver typeResolver;

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .build();
    }

}
