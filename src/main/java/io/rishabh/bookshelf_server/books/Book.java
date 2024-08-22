package io.rishabh.bookshelf_server.books;

import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "books")
public class Book {

    @Id
    @UuidGenerator
    private UUID id;
    private String title;
    private String author;
    private String description = null;
    private double rating = 0.0;
    private int yearPublished;
    private String isbn = null;
    private String publisher = null;
    private int pages;
    private String language = null;
    private String coverImage = null;
    private List<String> readOnPlatforms = null;
    private String series = null;
    private String edition = null;
    private String volume = null;
    private List<String> genres = null;
    private List<String> tags = null;
    private long favoritedBy = 0;
    private UUID addedByUser;

    public Book(String title, String author, int pages) {
        this.title = title;
        this.author = author;
        this.pages = pages;
    }

    public void set(Book book) {
        this.title = book.title;
        this.author = book.author;
        this.description = book.description;
        this.rating = book.rating;
        this.yearPublished = book.yearPublished;
        this.isbn = book.isbn;
        this.publisher = book.publisher;
        this.pages = book.pages;
        this.language = book.language;
        this.coverImage = book.coverImage;
        this.readOnPlatforms = book.readOnPlatforms;
        this.series = book.series;
        this.edition = book.edition;
        this.volume = book.volume;
        this.genres = book.genres;
        this.tags = book.tags;
        this.favoritedBy = book.favoritedBy;
        this.addedByUser = book.addedByUser;
    }

    @Override
    public String toString() {
        return "Book [id=" + id + ", title=" + title + ", author=" + author + ", description=" + description
                + ", rating=" + rating + ", yearPublished=" + yearPublished + ", isbn=" + isbn + ", publisher="
                + publisher + ", pages=" + pages + ", language=" + language + ", coverImage=" + coverImage
                + ", readOnPlatforms=" + readOnPlatforms + ", series=" + series + ", edition=" + edition + ", volume="
                + volume + ", genres=" + genres + ", tags=" + tags + ", favoritedBy=" + favoritedBy + ", addedByUser="
                + addedByUser + "]";
    }
}
