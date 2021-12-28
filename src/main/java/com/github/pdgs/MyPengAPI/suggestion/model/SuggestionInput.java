package com.github.pdgs.MyPengAPI.suggestion.model;

import com.github.pdgs.MyPengAPI.account.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SuggestionInput {

    private String title;
    private String content;
}
