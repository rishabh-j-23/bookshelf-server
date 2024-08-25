package io.rishabh.bookshelf_server.services;

import java.util.List;
import java.util.UUID;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import io.rishabh.bookshelf_server.model.Book;
import io.rishabh.bookshelf_server.repository.BookRepository;

@Service
@EnableCaching
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository, RedisTemplate<String, Object> redisTemplate) {
        this.bookRepository = bookRepository;
    }

    @Cacheable("books")
    public List<Book> getAllBooks() {

        return bookRepository.findAll();
    }

    @Cacheable(value = "books", key = "#id")
    public Book getBookById(UUID id) {
        return bookRepository.findById(id).orElse(null);
    }
    
    public Book addNewBook(Book newBook) throws Exception {
        Book existingBook = bookRepository.findByTitle(newBook.getTitle());
        if (existingBook != null) {
            throw new Exception("Book already exists");
        }
        return bookRepository.save(newBook);
    }

    public Book updateBook(UUID id, Book updatedBook) {
        return bookRepository.findById(id).map(book -> {
            book.set(updatedBook);
            return bookRepository.save(book);
        }).orElse(null);
    }

    public boolean deleteBook(UUID id, UUID addedByUserId) throws Exception {
        Book book = bookRepository.findById(id).orElse(null);
        if (book == null) {
            throw new Exception("Book not found");
        } else if (!book.getAddedByUser().equals(addedByUserId)) {
            throw new Exception("You are not authorized to delete this book");
        } else {
            bookRepository.deleteById(id);
            return true;
        }
    }
}
