package com.github.pdgs.MyPengAPI.suggestion.controller;

import com.github.pdgs.MyPengAPI.suggestion.entity.Suggestion;
import com.github.pdgs.MyPengAPI.suggestion.exception.AlreadyDeletedException;
import com.github.pdgs.MyPengAPI.suggestion.exception.DuplicateSuggestionException;
import com.github.pdgs.MyPengAPI.suggestion.exception.SuggestionNotFoundException;
import com.github.pdgs.MyPengAPI.suggestion.model.ResponseError;
import com.github.pdgs.MyPengAPI.suggestion.model.SuggestionInput;
import com.github.pdgs.MyPengAPI.suggestion.repository.SuggestionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/suggestion/")
public class SuggestionController {

    private final SuggestionRepo suggestionRepo;

    @ExceptionHandler(DuplicateSuggestionException.class)
    public ResponseEntity<?> handlerDuplicateSuggestionException(DuplicateSuggestionException e) {
        return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("add-suggestion")
    public ResponseEntity<Object> addSuggestion(@RequestParam SuggestionInput suggestionInput,
                                                Errors errors) {
        if (errors.hasErrors()) {

            List<ResponseError> responseErrors = new ArrayList<>();

            errors.getAllErrors().forEach(e -> {
                responseErrors.add(ResponseError.of((FieldError) e));
            });

            return new ResponseEntity<>(responseErrors, HttpStatus.BAD_REQUEST);
        }

        LocalDateTime checkDate = LocalDateTime.now().minusMinutes(1);
        int suggestionCount = suggestionRepo.countByTitleAndContentAndRegDateIsGreaterThanEqual(
                suggestionInput.getTitle(),
                suggestionInput.getContent(),
                checkDate
        );

        if (suggestionCount > 0) {
            throw new DuplicateSuggestionException("1분 이내에 동일한 내용의 건의사항이 등록되었습니다.");
        }
        

        suggestionRepo.save(Suggestion.builder()
                .title(suggestionInput.getTitle())
                .content(suggestionInput.getContent())
                .hits(0)
                .likes(0)
                .regDate(LocalDateTime.now())
                .build());

        return ResponseEntity.ok().build();
    }

    @GetMapping("{suggestionId}")
    public Suggestion suggestion(@PathVariable Long suggestionId) {
        Optional<Suggestion> suggestion = suggestionRepo.findById(suggestionId);
        return suggestion.orElse(null);
    }

    @GetMapping("latest/{size}")
    public Page<Suggestion> suggestionsLatest(@PathVariable int size) {
        return suggestionRepo.findAll(
                PageRequest.of(0, size, Sort.Direction.DESC, "regDate")
        );
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

    @ExceptionHandler(AlreadyDeletedException.class)
    public ResponseEntity<String> handlerAlreadyDeletedException(AlreadyDeletedException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
    }

    @DeleteMapping("{suggestionId}")
    public void deleteSuggestion(@PathVariable @Valid Long suggestionId) {
        Suggestion suggestion = suggestionRepo.findById(suggestionId)
                .orElseThrow(() -> new SuggestionNotFoundException("건의사항에 글이 존재하지 않습니다."));

        if (suggestion.isDeleted()) {
            throw new AlreadyDeletedException("이미 삭제된 건의사항 글입니다.");
        }

        suggestion.setDeleted(true);
        suggestion.setDeleteDate(LocalDateTime.now());

        suggestionRepo.save(suggestion);
    }

}
