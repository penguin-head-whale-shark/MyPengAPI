package com.github.pdgs.MyPengAPI.controller;

import com.github.pdgs.MyPengAPI.advice.exception.CEmailSignInFailedException;
import com.github.pdgs.MyPengAPI.config.security.JwtTokenProvider;
import com.github.pdgs.MyPengAPI.domain.posts.User;
import com.github.pdgs.MyPengAPI.repository.UserJpaRepo;
import com.github.pdgs.MyPengAPI.response.CommonResult;
import com.github.pdgs.MyPengAPI.response.SingleResult;
import com.github.pdgs.MyPengAPI.service.posts.ResponseService;
import com.github.pdgs.MyPengAPI.service.posts.SchoolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Tag(name = "1. Sign")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1")
public class SignController {

    private final UserJpaRepo userJpaRepo;
    private final JwtTokenProvider jwtTokenProvider;
    private final ResponseService responseService;
    private final PasswordEncoder passwordEncoder;
    private final SchoolService schoolService;

    @Operation(summary = "로그인", description = "이메일 회원 로그인")
    @PostMapping(value = "user/sign-in")
    public SingleResult<String> signIn(@RequestParam String id, @RequestParam String password) {
        User user = userJpaRepo.findById(id).orElseThrow(CEmailSignInFailedException::new);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CEmailSignInFailedException();
        }
        return responseService.getSingleResult(jwtTokenProvider.createToken(String.valueOf(user.getAutoID()), user.getRoles()));
    }


    @Operation(summary = "가입", description = "회원가입")
    @PostMapping(value = "user/sign-up")
    public CommonResult signUp(@RequestParam String id,
                               @RequestParam String password,
                               @RequestParam String name,
                               @RequestParam boolean isTeacher,
                               @RequestParam String school) {

        String result = schoolService.checkSchool(school);

        User user = User.builder()
                .name(name)
                .id(id)
                .password(password)
                .isTeacher(isTeacher)
                .school(result)
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
        return responseService.getSuccessResult();
    }












}
