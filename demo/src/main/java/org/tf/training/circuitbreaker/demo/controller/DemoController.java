package org.tf.training.circuitbreaker.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping ("/service")
public class DemoController {

    @GetMapping
    public String service(){
        return "Demo Service callled";
    }

}
