package com.github.pdgs.MyPengAPI.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    @GetMapping(value = "/")
    public String get() {
        return "Ping";
    }

}
