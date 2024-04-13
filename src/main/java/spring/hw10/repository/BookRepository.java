package spring.hw10.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.hw10.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
    Book findByName(String name);
}
