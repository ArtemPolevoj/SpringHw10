package spring.hw10.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.hw10.model.Reader;

public interface ReaderRepository extends JpaRepository<Reader, Long> {
    Reader findByName(String name);
}
