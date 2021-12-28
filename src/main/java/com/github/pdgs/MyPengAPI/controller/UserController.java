package com.github.pdgs.MyPengAPI.controller;

import com.github.pdgs.MyPengAPI.advice.exception.CUserNotFoundException;
import com.github.pdgs.MyPengAPI.domain.posts.User;
import com.github.pdgs.MyPengAPI.repository.UserJpaRepo;
import com.github.pdgs.MyPengAPI.response.CommonResult;
import com.github.pdgs.MyPengAPI.response.ListResult;
import com.github.pdgs.MyPengAPI.response.SingleResult;
import com.github.pdgs.MyPengAPI.service.posts.ResponseService;
import com.github.pdgs.MyPengAPI.service.posts.SchoolService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"2. User"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/")
public class UserController {

    private final UserJpaRepo userJpaRepo;
    private final ResponseService responseService;
    private final SchoolService schoolService;

    @Secured("ROLE_USER")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token",
            required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 리스트 조회", notes = "모든 회원을 조회한다.")
    @GetMapping(value = "/users")
    public ListResult<User> findAllUser() {
        return responseService.getListResult(userJpaRepo.findAll());
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token",
            required = false, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 단건 조회", notes = "회원번호(autoID)로 회원을 조회한다.")
    @GetMapping(value = "/user")
    public SingleResult<User> findUserByAutoId(@ApiParam(value = "언어", defaultValue = "ko") @RequestParam String lang) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();
        return responseService.getSingleResult(userJpaRepo.findById(id).orElseThrow(CUserNotFoundException::new));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token",
            required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 수정", notes = "회원 정보를 수정한다.")
    @PostMapping(value = "/user")
    public SingleResult<User> modify(@ApiParam(value = "회원번호", required = true) @RequestParam long autoID,
                                   @ApiParam(value = "회원ID: 이메일", required = true) @RequestParam String id,
                                   @ApiParam(value = "회원 비밀번호", required = true) @RequestParam String password,
                                   @ApiParam(value = "회원 이름", required = true) @RequestParam String name,
                                   @ApiParam(value = "계정의 선생님 여부", required = true) @RequestParam boolean isTeacher,
                                   @ApiParam(value = "회원 학교", required = true) @RequestParam String school) {

        String result = schoolService.checkSchool(school);

        User user = User.builder()
                .autoID(autoID)
                .name(name)
                .id(id)
                .password(password)
                .isTeacher(isTeacher)
                .school(result)
                .build();
        return responseService.getSingleResult(userJpaRepo.save(user));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 삭제", notes = "회원번호로 회원정보를 삭제한다.")
    @DeleteMapping(value = "user/{autoID}")
    public CommonResult delete(@PathVariable long autoID) {
        userJpaRepo.deleteById(autoID);
        return responseService.getSuccessResult();
    }

}
