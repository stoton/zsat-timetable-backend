package com.github.stoton.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan("com.github.stoton")
@EnableAspectJAutoProxy
public class AppConfiguration {

}
