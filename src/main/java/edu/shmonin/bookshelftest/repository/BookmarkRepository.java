package edu.shmonin.bookshelftest.repository;

import edu.shmonin.bookshelftest.model.Book;
import edu.shmonin.bookshelftest.model.Bookmark;
import edu.shmonin.bookshelftest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    List<Bookmark> findAllByUser(User user);

    Optional<Bookmark> findByBookIdAndUserUsername(Long bookId, String username);
}