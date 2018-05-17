package com.github.stoton.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class SwaggerController {

    @RequestMapping(value = "/documentation", produces = MediaType.TEXT_HTML_VALUE)
    public String swagger() {
        return "redirect:swagger-ui.html";
    }
}

