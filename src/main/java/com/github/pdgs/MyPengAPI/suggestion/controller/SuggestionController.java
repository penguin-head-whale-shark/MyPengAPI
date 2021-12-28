package com.github.pdgs.MyPengAPI.suggestion.controller;

import com.github.pdgs.MyPengAPI.suggestion.entity.Suggestion;
import com.github.pdgs.MyPengAPI.suggestion.exception.SuggestionNotFoundException;
import com.github.pdgs.MyPengAPI.suggestion.model.SuggestionInput;
import com.github.pdgs.MyPengAPI.suggestion.repository.SuggestionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @ExceptionHandler(SuggestionNotFoundException.class)
    public ResponseEntity<String> handlerSuggestionNotFoundException(SuggestionNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @PutMapping("{suggestionId}")
    public void updateSuggestion(@PathVariable Long suggestionId, @RequestParam SuggestionInput suggestionInput) {
        Suggestion suggestion = suggestionRepo.findById(suggestionId)
                        .orElseThrow(() -> new SuggestionNotFoundException("건의사항에 글이 존재하지 않습니다."));

        suggestion.setTitle(suggestionInput.getTitle());
        suggestion.setContent(suggestionInput.getContent());
        suggestion.setUpdateDate(LocalDateTime.now());
        suggestionRepo.save(suggestion);
    }

    @PatchMapping("{suggestionId}/hits")
    public void suggestionHits(@PathVariable Long suggestionId) {
        Suggestion suggestion = suggestionRepo.findById(suggestionId)
                .orElseThrow(() -> new SuggestionNotFoundException("건의사항에 글이 존재하지 않습니다."));
        suggestion.setHits(suggestion.getHits() + 1);
        suggestionRepo.save(suggestion);
    }

}
