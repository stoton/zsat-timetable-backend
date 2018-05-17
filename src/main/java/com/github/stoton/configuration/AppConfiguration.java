package com.github.stoton.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@ComponentScan("com.github.stoton")
@EnableAspectJAutoProxy
@EnableSwagger2
public class AppConfiguration {

}
