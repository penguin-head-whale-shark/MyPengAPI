package com.github.pdgs.MyPengAPI.suggestion.controller;

import com.github.pdgs.MyPengAPI.account.entity.User;
import com.github.pdgs.MyPengAPI.account.repository.UserJpaRepo;
import com.github.pdgs.MyPengAPI.account.response.CommonResult;
import com.github.pdgs.MyPengAPI.account.service.posts.ResponseService;
import com.github.pdgs.MyPengAPI.advice.exception.CEmailSignInFailedException;
import com.github.pdgs.MyPengAPI.suggestion.entity.Suggestion;
import com.github.pdgs.MyPengAPI.suggestion.exception.AlreadyDeletedException;
import com.github.pdgs.MyPengAPI.suggestion.exception.DuplicateSuggestionException;
import com.github.pdgs.MyPengAPI.suggestion.exception.LengthShortException;
import com.github.pdgs.MyPengAPI.suggestion.exception.SuggestionNotFoundException;
import com.github.pdgs.MyPengAPI.suggestion.repository.SuggestionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/suggestion/")
public class SuggestionController {

    private final SuggestionRepo suggestionRepo;
    private final UserJpaRepo userJpaRepo;
    private final ResponseService responseService;

    @ExceptionHandler(DuplicateSuggestionException.class)
    public ResponseEntity<?> handlerDuplicateSuggestionException(DuplicateSuggestionException e) {
        return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("add-suggestion")
    public CommonResult addSuggestion(@RequestParam String title,
                                      @RequestParam String content,
                                      @RequestParam String writerId) {

        LocalDateTime checkDate = LocalDateTime.now().minusMinutes(1);
        int suggestionCount = suggestionRepo.countByTitleAndContentAndRegDateIsGreaterThanEqual(
                title,
                content,
                checkDate
        );

        if (title == null || title.length() < 1) {
            throw new LengthShortException("제목의 길이가 너무 짧거나 없습니다.");
        }
        if (content == null || content.length() < 1) {
            throw new LengthShortException("내용이 너무 짧거나 없습니다.");
        }
        if (writerId == null || writerId.length() < 1) {
            throw new LengthShortException("작성자가 없습니다.");
        }

        if (suggestionCount > 0) {
            throw new DuplicateSuggestionException("1분 이내에 동일한 내용의 건의사항이 등록되었습니다.");
        }

        suggestionRepo.save(Suggestion.builder()
                .title(title)
                .content(content)
                .writerId(writerId)
                .hits(0)
                .likes(0)
                .regDate(LocalDateTime.now())
                .adopted(false)
                .build());

        return responseService.getSuccessResult();
    }

    @PostMapping("adopt-suggestion/{suggestionId}")
    public CommonResult adoptSuggestion(@PathVariable Long suggestionId,
                                                  @RequestParam Long teacherId) {
        Suggestion suggestion = suggestionRepo.findById(suggestionId)
                .orElseThrow(() -> new SuggestionNotFoundException("건의사항에 글이 존재하지 않습니다."));
        User user = userJpaRepo.findById(teacherId).orElseThrow(CEmailSignInFailedException::new);

        if (!user.isTeacher()) {
            return responseService.getFailResult(HttpStatus.BAD_REQUEST.value(), "선생님 계정만 접근 가능합니다.");
        }
        
        suggestion.setAdopted(true);
        suggestionRepo.save(suggestion);
        return responseService.getSuccessResult();
    }

    @GetMapping("get/{suggestionId}")
    public Suggestion suggestion(@PathVariable Long suggestionId) {
        Optional<Suggestion> suggestion = suggestionRepo.findById(suggestionId);
        return suggestion.orElse(null);
    }

    @GetMapping("get/adopt-suggestions/{userId}")
    public Page<Suggestion> getAdoptSuggestions(@PathVariable long userId) {
        return suggestionsLatest(suggestionRepo.findByWriterId(userId)
                .orElseThrow(() -> new SuggestionNotFoundException("채택된 건의사항이 없습니다.")).size());
    }

    @GetMapping("get/latest/{size}")
    public Page<Suggestion> suggestionsLatest(@PathVariable int size) {
        return suggestionRepo.findAll(
                PageRequest.of(0, size, Sort.Direction.DESC, "regDate")
        );
    }

    @GetMapping("search-suggestions/{keyword}")
    public Page<Suggestion> searchSuggestionByKeyword(@PathVariable String keyword) {

        return suggestionRepo.findAllByTitleIsContaining(keyword,
                PageRequest.of(0, suggestionRepo.countByTitleContaining(keyword), Sort.Direction.DESC, "regDate")
                );
    }

    @ExceptionHandler(SuggestionNotFoundException.class)
    public ResponseEntity<String> handlerSuggestionNotFoundException(SuggestionNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @PutMapping("put/{suggestionId}")
    public void updateSuggestion(@PathVariable Long suggestionId,
                                 @RequestParam String title,
                                 @RequestParam String content,
                                 @RequestParam String writerId) {
        Suggestion suggestion = suggestionRepo.findById(suggestionId)
                        .orElseThrow(() -> new SuggestionNotFoundException("건의사항에 글이 존재하지 않습니다."));

        suggestion.setTitle(title);
        suggestion.setContent(content);
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

    @DeleteMapping("delete/{suggestionId}")
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

    @DeleteMapping("delete/all")
    public void deleteSuggestionAll() {
        List<Suggestion> suggestions = suggestionRepo.findAll();

        if (!suggestions.isEmpty()) {
            suggestions.forEach(e -> {
                if (!e.isDeleted()) {
                    e.setDeleted(true);
                    e.setDeleteDate(LocalDateTime.now());
                }
            });
        }

        suggestionRepo.saveAll(suggestions);
    }

}
