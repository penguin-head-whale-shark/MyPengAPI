package com.github.pdgs.MyPengAPI.suggestion.entity;

import com.github.pdgs.MyPengAPI.account.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Suggestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int suggestionId;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 1000000)
    private String content;

    @Column(nullable = false)
    private LocalDateTime regDate;

    @Column(nullable = false)
    private LocalDateTime updateDate;

    @Column
    private int hits;

    @Column
    private int likes;

}
