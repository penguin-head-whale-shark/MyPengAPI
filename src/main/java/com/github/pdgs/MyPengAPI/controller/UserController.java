package com.github.pdgs.MyPengAPI.controller;

import com.github.pdgs.MyPengAPI.advice.exception.CUserNotFoundException;
import com.github.pdgs.MyPengAPI.domain.posts.User;
import com.github.pdgs.MyPengAPI.repository.UserJpaRepo;
import com.github.pdgs.MyPengAPI.response.CommonResult;
import com.github.pdgs.MyPengAPI.response.ListResult;
import com.github.pdgs.MyPengAPI.response.SingleResult;
import com.github.pdgs.MyPengAPI.service.posts.ResponseService;
import com.github.pdgs.MyPengAPI.service.posts.SchoolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "User", description = "사용자 API")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/")
public class UserController {

    private final UserJpaRepo userJpaRepo;
    private final ResponseService responseService;
    private final SchoolService schoolService;

    @Operation(summary = "회원 조회", description = "모든 회원을 조회합니다.")
    @GetMapping(value = "/users")
    public ListResult<User> findAllUser() {
        return responseService.getListResult(userJpaRepo.findAll());
    }

    @Operation(summary = "회원 단건 조회", description = "userId로 회원 조회")
    @GetMapping(value = "/user/{autoID}")
    public SingleResult<User> findUserByAutoId(@PathVariable Long autoID,
                                               @RequestParam String lang) {
        return responseService.getSingleResult(userJpaRepo.findById(autoID).orElseThrow(CUserNotFoundException::new));
    }

    @Operation(summary = "회원 입력", description = "회원을 입력한다.")
    @PostMapping(value = "/user")
    public SingleResult<User> save(@RequestParam @Valid String name,
                                   @RequestParam @Valid String id,
                                   @RequestParam @Valid String password,
                                   @RequestParam @Valid boolean isTeacher,
                                   @RequestParam @Valid String school) {

        String result = schoolService.checkSchool(school);

        User user = User.builder()
                .name(name)
                .id(id)
                .password(password)
                .isTeacher(isTeacher)
                .school(result)
                .build();
        return responseService.getSingleResult(userJpaRepo.save(user));
    }

    @Operation(summary = "회원 수정", description = "회원 정보를 수정한다.")
    @PutMapping(value = "/user")
    public SingleResult<User> modify(@RequestParam long autoID,
                                     @RequestParam String name,
                                     @RequestParam String id,
                                     @RequestParam String password,
                                     @RequestParam boolean isTeacher,
                                     @RequestParam String school) {

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

    @Operation(summary = "회원 삭제", description = "UserId로 회원 정보 삭제")
    @DeleteMapping(value = "user/{autoID}")
    public CommonResult delete(@PathVariable long autoID) {
        userJpaRepo.deleteById(autoID);
        return responseService.getSuccessResult();
    }

}
