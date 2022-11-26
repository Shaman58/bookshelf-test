package edu.shmonin.bookshelftest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String title;
    @OneToMany(cascade = {javax.persistence.CascadeType.ALL})
    private List<Page> pages = new ArrayList<>();
    private Boolean isConverted;
    @Transient
    Bookmark bookmark;
}