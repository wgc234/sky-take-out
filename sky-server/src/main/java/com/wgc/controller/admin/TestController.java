package com.wgc.controller.admin;

import com.wgc.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/test")
public class TestController {

    @GetMapping
    public Result<String> test() {
        return Result.success("Test passed");
    }
} 