package com.github.pdgs.MyPengAPI.controller.exception;

import com.github.pdgs.MyPengAPI.advice.exception.CAuthenticationEntryPointException;
import com.github.pdgs.MyPengAPI.response.CommonResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/exception")
public class ExceptionController {

    @GetMapping(value = "/entrypoint")
    public CommonResult entrypointException() {
        throw new CAuthenticationEntryPointException();
    }

    @GetMapping(value = "/access-denied")
    public CommonResult accessDeniedException() {
        throw new AccessDeniedException("");
    }

}
