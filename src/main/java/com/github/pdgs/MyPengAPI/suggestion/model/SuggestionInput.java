package com.github.pdgs.MyPengAPI.suggestion.model;

import com.github.pdgs.MyPengAPI.account.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SuggestionInput {

    @NotBlank(message = "제목은 필수 항목입니다.")
    @Size(min = 1, max = 100, message = "제목은 1자 이상 100자 이하로 입력해주세요.")
    private String title;

    @NotBlank(message = "내용은 필수 항목입니다.")
    @Size(min = 5, max = 10000, message = "내용은 5자 이상 10000자 이하로 입력해주세요.")
    private String content;

    @NotBlank(message = "작성자는 필수 항목입니다.")
    @Size(max = 20, message = "작성자 Id는 20자 이하입니다.")
    private Long writerId;
}
