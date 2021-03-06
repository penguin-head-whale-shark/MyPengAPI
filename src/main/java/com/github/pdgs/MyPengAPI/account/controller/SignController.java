package com.github.pdgs.MyPengAPI.account.controller;

import com.github.pdgs.MyPengAPI.account.entity.User;
import com.github.pdgs.MyPengAPI.account.repository.UserJpaRepo;
import com.github.pdgs.MyPengAPI.account.response.CommonResult;
import com.github.pdgs.MyPengAPI.account.service.posts.ResponseService;
import com.github.pdgs.MyPengAPI.account.service.posts.SchoolService;
import com.github.pdgs.MyPengAPI.account.response.SingleResult;
import com.github.pdgs.MyPengAPI.advice.exception.CEmailSignInFailedException;
import com.github.pdgs.MyPengAPI.config.security.JwtTokenProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Api(tags = {"1. Sign"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/")
public class SignController {

    private final UserJpaRepo userJpaRepo;
    private final JwtTokenProvider jwtTokenProvider;
    private final ResponseService responseService;
    private final PasswordEncoder passwordEncoder;
    private final SchoolService schoolService;

    @ApiOperation(value = "로그인", notes = "이메일 회원 로그인을 한다.")
    @PostMapping(value = "user/sign-in")
    public SingleResult<String> signIn(@ApiParam(value = "회원ID: 이메일", required = true) @RequestParam String id,
                                       @ApiParam(value = "비밀번호", required = true) @RequestParam String password) {

        User user = userJpaRepo.findById(id).orElseThrow(CEmailSignInFailedException::new);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CEmailSignInFailedException();
        }
        return responseService.getSingleResult(jwtTokenProvider.createToken(String.valueOf(user.getAutoID()), user.getRoles()));
    }


    @ApiOperation(value = "가입", notes = "회원가입을 한다.")
    @PostMapping(value = "user/sign-up")
    public CommonResult signUp(@ApiParam(value = "회원ID: 이메일", required = true) @RequestParam String id,
                               @ApiParam(value = "비밀번호", required = true) @RequestParam String password,
                               @ApiParam(value = "이름", required = true) @RequestParam String name,
                               @ApiParam(value = "계정의 선생님 여부: true / false", required = true) @RequestParam boolean isTeacher,
                               @ApiParam(value = "학교", required = true) @RequestParam(required = false, defaultValue = "대구소프트웨어고등학교") String school) {

        System.out.println(userJpaRepo.findById(id));

        if (userJpaRepo.findById(id).isPresent()) {
            return responseService.getFailResult(-1005, "이미 존재하는 이메일입니다.");
        }

        String result = schoolService.checkSchool(school);

        if (result == null) {
            return responseService.getFailResult(-1004, "없는 학교가 전달되었습니다.");
        }

        userJpaRepo.save(User.builder()
                .name(name)
                .id(id)
                .password(passwordEncoder.encode(password)) // 반드시 인코딩할 것
                .isTeacher(isTeacher)
                .school(result)
                .roles(Collections.singletonList("ROLE_USER"))
                .build());
        return responseService.getSuccessResult();
    }

}
