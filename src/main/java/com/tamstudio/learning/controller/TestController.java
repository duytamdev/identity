package com.tamstudio.learning.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequestMapping("/test")
    public ResponseEntity<?> test() {
        return new ResponseEntity<>("Hello World", HttpStatus.OK);
    }
}
