package org.example.repo2cloud.controller;

import org.example.repo2cloud.wrapper.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/repo2cloud/v1/test")
public class TestController {

    @GetMapping
    public ApiResponse<String> testServer(){
        return new ApiResponse<>("Server health stauts",
                "Server running....");
    }

}
