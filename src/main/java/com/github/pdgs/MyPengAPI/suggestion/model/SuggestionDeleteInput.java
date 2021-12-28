package com.github.pdgs.MyPengAPI.suggestion.model;

import lombok.Data;

import java.util.List;

@Data
public class SuggestionDeleteInput {

    private List<Long> idList;
}
