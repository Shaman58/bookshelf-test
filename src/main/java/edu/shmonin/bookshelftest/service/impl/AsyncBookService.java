package edu.shmonin.bookshelftest.service.impl;

import edu.shmonin.bookshelftest.model.Book;
import edu.shmonin.bookshelftest.repository.BookRepository;
import edu.shmonin.bookshelftest.util.EpubToPageConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
@RequiredArgsConstructor
public class AsyncBookService {

    private final BookRepository bookRepository;
    private final EpubToPageConverter epubToPageConverter;

    @Async
    public void saveBookPages(Book book, MultipartFile file, File tempFile) {
        var pages = epubToPageConverter.convert(file, tempFile);
        book.setPages(pages);
        book.setIsConverted(true);
        bookRepository.save(book);
    }
}