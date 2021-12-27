package com.github.pdgs.MyPengAPI.controller;

import com.github.pdgs.MyPengAPI.service.posts.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SchoolController {

    private final SchoolService schoolService;

    @GetMapping(value = "/api/v1/schools/{name}")
    public String get(@PathVariable String name) {
        return schoolService.findByName(name);
    }

}
