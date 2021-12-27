package com.github.pdgs.MyPengAPI.controller;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
    public SingleResult<User> findUserByAutoId(@PathVariable Long autoID) {
        return responseService.getSingleResult(userJpaRepo.findById(autoID).orElse(null));
    }

    @Operation(summary = "회원 입력", description = "회원을 입력한다.")
    @PostMapping(value = "/user")
    public SingleResult<User> save(@RequestParam @Valid String name,
                                   @RequestParam @Valid String id,
                                   @RequestParam @Valid String password,
                                   @RequestParam @Valid boolean isTeacher,
                                   @RequestParam @Valid String school) {

        String schoolSearch = schoolService.findByName(school);
        if (schoolSearch == null) {
            return responseService.getFailSingleResult("존재하지 않는 학교 입니다.");
        }

        boolean hasSchool = schoolSearch.contains(school);

        if (!hasSchool) {
            return responseService.getFailSingleResult("존재하지 않는 학교 입니다.");
        }

        JSONArray jsonArray;
        try {
            JSONObject jsonObject = new JSONObject(schoolSearch);
            jsonArray = (JSONArray) jsonObject.get("schools");
        } catch (JSONException e) {
            e.printStackTrace();
            return responseService.getFailSingleResult("존재하지 않는 학교 입니다.");
        }

        String result = jsonArray.getJSONObject(0).get("name").toString();
        System.out.println("결과: " + result);

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
        User user = User.builder()
                .autoID(autoID)
                .name(name)
                .id(id)
                .password(password)
                .isTeacher(isTeacher)
                .school(school)
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
