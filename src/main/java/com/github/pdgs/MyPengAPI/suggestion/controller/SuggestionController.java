package com.github.pdgs.MyPengAPI.suggestion.controller;

import com.github.pdgs.MyPengAPI.suggestion.entity.Suggestion;
import com.github.pdgs.MyPengAPI.suggestion.model.SuggestionInput;
import com.github.pdgs.MyPengAPI.suggestion.repository.SuggestionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/suggestion/")
public class SuggestionController {

    private final SuggestionRepo suggestionRepo;

    @PostMapping("addSuggest")
    public Suggestion addSuggestion(@RequestParam SuggestionInput suggestionInput) {
        Suggestion suggestion = Suggestion.builder()
                .title(suggestionInput.getTitle())
                .content(suggestionInput.getContent())
                .regDate(LocalDateTime.now())
                .hits(0)
                .likes(0)
                .build();
        suggestionRepo.save(suggestion);
        return suggestion;
    }

    @GetMapping("{suggestionId}")
    public Suggestion suggestion(@PathVariable Long suggestionId) {
        Optional<Suggestion> suggestion = suggestionRepo.findById(suggestionId);
        return suggestion.orElse(null);
    }

    @PutMapping("{suggestionId}")
    public void updateSuggestion(@PathVariable Long suggestionId, @RequestParam SuggestionInput suggestionInput) {
        Optional<Suggestion> suggestion = suggestionRepo.findById(suggestionId);
        if (suggestion.isPresent()) {
            suggestion.get().setTitle(suggestionInput.getTitle());
            suggestion.get().setContent(suggestionInput.getContent());
            suggestion.get().setUpdateDate(LocalDateTime.now());
            suggestionRepo.save(suggestion.get());
        }
    }

}
