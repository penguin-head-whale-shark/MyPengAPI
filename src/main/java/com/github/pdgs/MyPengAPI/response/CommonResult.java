package com.github.pdgs.MyPengAPI.response;

import io.swagger.v3.oas.annotations.Operation;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonResult {

    private boolean success;

    private int code;

    private String msg;

}
