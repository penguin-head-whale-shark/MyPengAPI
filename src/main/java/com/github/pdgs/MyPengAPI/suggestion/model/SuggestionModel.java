package com.github.pdgs.MyPengAPI.suggestion.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SuggestionModel {

    private int suggestionId;

    private String title;

    private String content;

    private LocalDateTime regDate;

}
