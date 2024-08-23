package io.rishabh.bookshelf_server.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import io.rishabh.bookshelf_server.model.Book;
import io.rishabh.bookshelf_server.repository.BookRepository;
import jakarta.annotation.Nonnull;

@RestController
public class BookController {

    BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping("/books")
    public List<Book> getBooks() {
        return bookRepository.findAll();
    }

    @PostMapping("/book/add")
    public Object newBook(@RequestBody @Nonnull Book newBook) {
        try {
            // check if book already exists
            Book existingBook = bookRepository.findByTitle(newBook.getTitle());
            if (existingBook != null) {
                return new ResponseEntity<>("Book already exists", HttpStatus.CONFLICT);
            }
            
            Book savedBook = bookRepository.save(newBook);
            return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<Book> one(@PathVariable UUID id) {
        Book book = bookRepository.findById(id).orElse(null);
        return book != null ? new ResponseEntity<>(book, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/book/update/{id}")
    public ResponseEntity<Book> updateBookWithId(@PathVariable UUID id, @RequestBody Book updatedBook) {
        return bookRepository.findById(id).map(book -> {
            book.set(updatedBook);
            Book savedBook = bookRepository.save(book);
            return new ResponseEntity<>(savedBook, HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/book/delete/{id}")
    public ResponseEntity<String> deleteBookWithId(@PathVariable UUID id, @RequestHeader UUID addedbyUserId) {
        // check if book is created by user
        Book book = bookRepository.findById(id).orElse(null);
        if (book == null) {
            return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
        } else if (!book.getAddedByUser().equals(addedbyUserId)) {
            return new ResponseEntity<>("You are not authorized to delete this book", HttpStatus.UNAUTHORIZED);
        } else {
            bookRepository.deleteById(id);
            return new ResponseEntity<>("Book deleted successfully", HttpStatus.OK);
        }
    }
}
