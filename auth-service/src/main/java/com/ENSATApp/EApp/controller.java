package com.ENSATApp.EApp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class controller {

    @GetMapping(path = "/hello")
    public String helloWorld(){
        int age = 23;
        String name = "Achraf khabar";
        return "You are " + name + " and you have " + age ;
    }
}
