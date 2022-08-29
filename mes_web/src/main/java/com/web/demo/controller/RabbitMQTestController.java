package com.web.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2020/5/29.
 */
@RestController
@RequestMapping(value = "/demo/rabbitmqtest")
public class RabbitMQTestController {

   /* @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping(value = "/test")
    public void test() {
        rabbitTemplate.convertAndSend("helloQueue", "helloQueue");
    }*/
}
