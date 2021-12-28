package com.github.pdgs.MyPengAPI.suggestion.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.FieldError;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseError {

    private String field;
    private String errorMsg;

    public static ResponseError of(FieldError e) {
        return ResponseError.builder()
                .field((e).getField())
                .errorMsg(e.getDefaultMessage())
                .build();
    }

}
