package edu.shmonin.bookshelftest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

import static javax.persistence.CascadeType.REMOVE;
import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookmarks")
public class Bookmark {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @ManyToOne(cascade = REMOVE)
    @JoinColumn(name = "book_id")
    private Book book;
    @OneToOne(cascade = REMOVE)
    @JoinColumn(name = "user_id")
    private User user;
    private int position;
    @Column(name = "expired_at")
    private LocalDateTime expiredAt;
}