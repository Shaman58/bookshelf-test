package edu.shmonin.bookshelftest.service.impl;

import edu.shmonin.bookshelftest.dto.BookDto;
import edu.shmonin.bookshelftest.dto.BookmarkDto;
import edu.shmonin.bookshelftest.exception.EntityNotFoundException;
import edu.shmonin.bookshelftest.model.Book;
import edu.shmonin.bookshelftest.model.Bookmark;
import edu.shmonin.bookshelftest.model.Shelf;
import edu.shmonin.bookshelftest.repository.BookRepository;
import edu.shmonin.bookshelftest.repository.BookmarkRepository;
import edu.shmonin.bookshelftest.repository.ShelfRepository;
import edu.shmonin.bookshelftest.service.BookService;
import edu.shmonin.bookshelftest.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.Locale.ENGLISH;


@RequiredArgsConstructor
@Slf4j
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final ShelfRepository shelfRepository;
    private final BookmarkRepository bookmarkRepository;
    private final UserService userService;
    private final AsyncBookService asyncBookService;
    private final MessageSource messageSource;

    @Override
    @Transactional
    public BookDto getBook(String title, String bookOwner, String shelfName, String username) {
        log.info("Looking book with title {}, username {}, shelf {} into DB", title, bookOwner, shelfName);
        var book = getBooksByShelf(shelfName, bookOwner).stream()
                .filter(b -> b.getTitle().equals(title)).findFirst()
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("exception.bookNotFound", new Object[]{title}, ENGLISH)));
        book.setBookmark(bookmarkRepository.findByBookIdAndUserUsername(book.getId(), username).orElse(null));
        var bookResponse = BookDto.builder()
                .owner(bookOwner)
                .title(book.getTitle())
                .position(0)
                .build();
        if (book.getBookmark() != null) {
            bookResponse.setPages(book.getPages());
            bookResponse.setPosition(book.getBookmark().getPosition());
        }
        return bookResponse;
    }

    @Override
    @Transactional
    public List<BookDto> getBookResponsesByShelf(String shelfName, String username) {
        log.info("Looking books on the user {} shelf {} into DB", username, shelfName);
        return getBooksByShelf(shelfName, username).stream()
                .map(b -> BookDto.builder()
                        .owner(b.getBookmark().getUser().getUsername())
                        .title(b.getTitle())
                        .build()
                ).toList();
    }

    @Override
    @Transactional
    public List<Book> getGivenUserBooks(String username) {
        var user = userService.getUser(username);
        log.info("Looking given books to user {} into DB", user.getUsername());
        var allUserBooks = shelfRepository.getShelvesByUser(user).stream()
                .flatMap(s -> s.getBooks().stream())
                .toList();
        var userBookWithBookmarks = bookmarkRepository.findAllByUser(user).stream()
                .map(Bookmark::getBook)
                .toList();
        var givenBooks = new ArrayList<>(userBookWithBookmarks);
        givenBooks.removeAll(allUserBooks);
        return givenBooks;

    }

    @Override
    @Transactional
    public List<Shelf> getUserShelves(String username) {
        log.info("Looking shelves of the user {} into DB", username);
        var user = userService.getUser(username);
        return shelfRepository.getShelvesByUser(user);
    }

    @Override
    @Transactional
    public Shelf saveUserShelf(String shelfName, String username) {
        log.info("Saving shelf {} into DB", shelfName);
        var user = userService.getUser(username);
        return shelfRepository.save(new Shelf(shelfName, user));
    }

    @Override
    @Transactional
    public BookmarkDto saveBookmark(String bookOwner, String username, String bookTitle, LocalDateTime expiredAt, Integer position) {
        log.info("Saving bookmark with user {} and book {} into DB", username, bookTitle);
        var authUser = userService.getUser(bookOwner);
        var user = userService.getUser(username);
        var book = shelfRepository.getShelvesByUser(authUser).stream()
                .flatMap(s -> s.getBooks().stream())
                .filter(b -> b.getTitle().equals(bookTitle)).findFirst()
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("exception.bookNotFound", new Object[]{bookTitle}, ENGLISH)));
        var bookmark = new Bookmark();
        bookmark.setBook(book);
        bookmark.setUser(user);
        bookmark.setExpiredAt(expiredAt);
        bookmark.setPosition(position);
        bookmark = bookmarkRepository.save(bookmark);
        return BookmarkDto.builder()
                .title(bookmark.getBook().getTitle())
                .username(bookmark.getUser().getUsername())
                .expiredAt(bookmark.getExpiredAt())
                .position(bookmark.getPosition())
                .build();
    }

    @Override
    @Transactional
    public BookDto saveBookToShelf(String shelfName, String title, MultipartFile file, String username) {
        log.info("Saving book {} into DB", title);
        var user = userService.getUser(username);
        var shelf = getUserShelves(user.getUsername()).stream()
                .filter(s -> s.getName().equals(shelfName)).findFirst()
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("exception.shelfNotFound", new Object[]{shelfName}, ENGLISH)));
        var book = new Book();
        book.setTitle(title);
        book.setIsConverted(false);
        var savedBook = bookRepository.save(book);
        var bookmark = new Bookmark();
        bookmark.setBook(savedBook);
        bookmark.setExpiredAt(LocalDateTime.now().plusYears(100));
        bookmark.setUser(user);
        bookmark.setPosition(1);
        bookmarkRepository.save(bookmark);
        shelf.getBooks().add(savedBook);
        shelfRepository.save(shelf);
        try {
            var tempFile = File.createTempFile("file", "tmp");
            asyncBookService.saveBookPages(savedBook, file, tempFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return BookDto.builder()
                .position(bookmark.getPosition())
                .title(savedBook.getTitle())
                .pages(savedBook.getPages())
                .owner(username)
                .build();
    }

    private List<Book> getBooksByShelf(String shelfName, String username) {
        var shelf = getUserShelves(username).stream()
                .filter(s -> s.getName().equals(shelfName)).findFirst()
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("exception.shelfNotFound", new Object[]{shelfName}, ENGLISH)));
        return shelf.getBooks().stream()
                .filter(b -> b.getIsConverted().equals(true))
                .toList();
    }
}