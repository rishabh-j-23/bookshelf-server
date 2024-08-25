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
import io.rishabh.bookshelf_server.services.BookService;
import jakarta.annotation.Nonnull;

@RestController
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public List<Book> getBooks() {
        return bookService.getAllBooks();
    }

    @PostMapping("/books/add")
    public ResponseEntity<Object> newBook(@RequestBody @Nonnull Book newBook) {
        try {
            Book savedBook = bookService.addNewBook(newBook);
            return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<Book> one(@PathVariable UUID id) {
        Book book = bookService.getBookById(id);
        return book != null ? new ResponseEntity<>(book, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/books/update/{id}")
    public ResponseEntity<Book> updateBookWithId(@PathVariable UUID id, @RequestBody Book updatedBook) {
        Book book = bookService.updateBook(id, updatedBook);
        return book != null ? new ResponseEntity<>(book, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/books/delete/{id}")
    public ResponseEntity<String> deleteBookWithId(@PathVariable UUID id, @RequestHeader UUID addedbyUserId) {
        try {
            bookService.deleteBook(id, addedbyUserId);
            return new ResponseEntity<>("Book deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
