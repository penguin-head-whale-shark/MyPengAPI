package com.github.pdgs.MyPengAPI.suggestion.repository;

import com.github.pdgs.MyPengAPI.suggestion.entity.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SuggestionRepo extends JpaRepository<Suggestion, Long> {

    Optional<List<Suggestion>> findBySuggestionIdIn(List<Long> idList);

    Optional<List<Suggestion>> findByWriterId(Long writerId);

    int countByTitleAndContentAndRegDateIsGreaterThanEqual(String title, String content, LocalDateTime regDate);

}
