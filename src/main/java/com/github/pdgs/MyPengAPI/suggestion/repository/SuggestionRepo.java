package com.github.pdgs.MyPengAPI.suggestion.repository;

import com.github.pdgs.MyPengAPI.suggestion.entity.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuggestionRepo extends JpaRepository<Suggestion, Long> {
}
