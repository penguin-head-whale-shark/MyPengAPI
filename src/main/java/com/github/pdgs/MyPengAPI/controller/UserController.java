package com.github.pdgs.MyPengAPI.controller;

import com.github.pdgs.MyPengAPI.domain.posts.User;
import com.github.pdgs.MyPengAPI.repository.UserJpaRepo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "1. User")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1.0.0")
public class UserController {

    private final UserJpaRepo userJpaRepo;

    @Operation(summary = "회원 조회", description = "모든 회원을 조회합니다.")
    @GetMapping(value = "/user")
    public List<User> findAllUsers() {
        return userJpaRepo.findAll();
    }

    @Operation(summary = "회원 입력", description = "회원을 입력합니다.")
    @PostMapping(value = "/user")
    public User save(@Parameter(name = "회원 아이디", required = true) @RequestParam String id,
                     @Parameter(name = "회원 비밀번호", required = true) @RequestParam String password,
                     @Parameter(name = "회원 계정의 선생님 여부", required = true) @RequestParam boolean isTeacher,
                     @Parameter(name = "회원 학교", required = true) @RequestParam String school) {
        User user = User.builder()
                .id(id)
                .password(password)
                .isTeacher(isTeacher)
                .school(school)
                .build();
        return userJpaRepo.save(user);
    }

}
