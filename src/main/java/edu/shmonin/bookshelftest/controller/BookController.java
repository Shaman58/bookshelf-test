package edu.shmonin.bookshelftest.controller;

import edu.shmonin.bookshelftest.dto.BookDto;
import edu.shmonin.bookshelftest.model.Shelf;
import edu.shmonin.bookshelftest.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BookController {

    private final BookService bookService;

    @PostMapping("/book")
    public BookDto saveBook(String shelfName, String title, MultipartFile file, Principal principal) {
        return bookService.saveBookToShelf(shelfName, title, file, principal.getName());
    }

    @PostMapping("/shelf")
    public Shelf saveShelf(String shelfName, Principal principal) {
        return bookService.saveUserShelf(shelfName, principal.getName());
    }

    @GetMapping("/book")
    public BookDto getBook(String title, String bookOwner, String shelfName, Principal principal) {
        return bookService.getBook(title, bookOwner, shelfName, principal.getName());
    }
}