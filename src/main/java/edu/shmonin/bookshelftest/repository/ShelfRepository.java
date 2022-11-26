package edu.shmonin.bookshelftest.repository;

import edu.shmonin.bookshelftest.model.Shelf;
import edu.shmonin.bookshelftest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShelfRepository extends JpaRepository<Shelf, Long> {

    List<Shelf> getShelvesByUser(User user);
}