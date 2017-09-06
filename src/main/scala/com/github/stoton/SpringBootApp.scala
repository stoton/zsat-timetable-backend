package com.github.stoton

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@EnableAutoConfiguration
@ComponentScan
class SampleConfig

object SpringBootApp extends App {
  SpringApplication.run(classOf[SampleConfig])
}
