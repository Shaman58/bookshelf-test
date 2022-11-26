package edu.shmonin.bookshelftest.dto;

import edu.shmonin.bookshelftest.model.Page;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class BookDto {

    private String title;
    private String owner;
    private int position;
    private List<Page> pages;
}