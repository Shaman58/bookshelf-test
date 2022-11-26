package edu.shmonin.bookshelftest.service;

import edu.shmonin.bookshelftest.dto.BookDto;
import edu.shmonin.bookshelftest.dto.BookmarkDto;
import edu.shmonin.bookshelftest.model.Book;
import edu.shmonin.bookshelftest.model.Shelf;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface BookService {

    BookDto saveBookToShelf(String shelfName, String title, MultipartFile file, String username);

    BookDto getBook(String title, String bookOwner, String shelfName, String username);

    List<BookDto> getBookResponsesByShelf(String shelfName, String username);

    List<Book> getGivenUserBooks(String username);

    List<Shelf> getUserShelves(String username);

    Shelf saveUserShelf(String shelfName, String username);

    BookmarkDto saveBookmark(String bookOwner, String username, String bookTitle, LocalDateTime expiredAt, Integer position);
}