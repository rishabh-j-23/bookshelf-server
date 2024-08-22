package io.rishabh.bookshelf_server.books;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID>{
    
    public Book findByTitle(String title);
    public List<Book> findByAuthor(String author);
    public Book findByIsbn(String isbn);
    public List<Book> findByPublisher(String publisher);
    public List<Book> findByYearPublished(int yearPublished);
    public List<Book> findByLanguage(String language);
    public List<Book> findBySeries(String series);
        
}

