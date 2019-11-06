package com.hy.iom.reporting.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reporting")
public class TestController {
    @GetMapping("/test")
    public String hello(){
        return "hello";
    }
}
