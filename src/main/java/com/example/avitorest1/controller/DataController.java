package com.example.avitorest1.controller;

import com.example.avitorest1.service.DataInitService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DataController {
    @Autowired
    private DataInitService dataInitService;
    @PostMapping("/init-data")
    public void preInitData(){
        dataInitService.initData();
    }
}
