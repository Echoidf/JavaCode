package com.zql.springboot.demo.controller;

import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author：zql
 * @date: 2023/4/25
 */
@Controller
@RequestMapping("/index")
@Api(tags = "测试")
public class IndexController {


}
