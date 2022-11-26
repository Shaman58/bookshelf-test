package edu.shmonin.bookshelftest.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BookmarkDto {

    private String title;
    private String username;
    private int position;
    private LocalDateTime expiredAt;
}