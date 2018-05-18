package com.github.stoton.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import springfox.documentation.annotations.ApiIgnore;

@Controller
@ApiIgnore
public class SwaggerController {

    @RequestMapping("/documentation")
    public String swagger() {
        return "redirect:/documentation/swagger-ui.html";
    }
}

