package com.github.pdgs.MyPengAPI.advice;

import com.github.pdgs.MyPengAPI.advice.exception.CEmailSignInFailedException;
import com.github.pdgs.MyPengAPI.advice.exception.CUserNotFoundException;
import com.github.pdgs.MyPengAPI.response.CommonResult;
import com.github.pdgs.MyPengAPI.service.posts.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {

    private final ResponseService responseService;
    private final MessageSource messageSource;

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult defaultException(HttpServletRequest request, Exception e) {
        return responseService.getFailResult(Integer.parseInt(getMessage("unknown.code")), getMessage("unknown.msg"));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult userNotFoundException(HttpServletRequest request, CUserNotFoundException e) {
        return responseService.getFailResult(Integer.parseInt(getMessage("userNotFound.code")), getMessage("userNotFound.msg"));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult emailSignInFailed(HttpServletRequest request, CEmailSignInFailedException e) {
        return responseService.getFailResult(Integer.parseInt(getMessage("emailSignInFailed.code")), getMessage("emailSignInFailed.msg"));
    }

    // code 정보에 해당하는 메시지를 조회한다.
    private String getMessage(String code) {
        return getMessage(code, null);
    }

    // code 정보, 추가 argument로 현재 locale에 맞는 메시지를 조회한다.
    private String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

}
