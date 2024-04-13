package spring.hw10.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.WebTestClient;
import spring.hw10.model.Book;
import spring.hw10.repository.BookRepository;
import spring.hw10.servies.BookService;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class BookControllerTest {

    WebTestClient webTestClient;
    @Autowired
    BookService bookService;

    @BeforeEach
    void setup() {
        webTestClient = WebTestClient.bindToController(new BookController(bookService))
                .configureClient()
                .baseUrl("/book")
                .build();

    }

    @Test
    void findBookByIdStatusOk() {
        String nameBook ="BookTest";
        bookService.addNewBook(nameBook);
        Long id = bookService.findByName(nameBook).getId();
        Book findBook = webTestClient.get()
                .uri("/" + id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Book.class)
                .returnResult()
                .getResponseBody();
        assertEquals(nameBook, findBook.getName());
        assertEquals(id, findBook.getId());
    }

    @Test
    void getAllBooksStatusOk() {
        List<Book> books = bookService.getAll();
        List<Book> responseBody = webTestClient.get()
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<Book>>() {})
                .returnResult()
                .getResponseBody();

        Assertions.assertEquals(books.size(), responseBody.size());
        for (Book book : responseBody) {
            boolean found = books.stream()
                    .filter(b -> Objects.equals(book.getId(), b.getId()))
                    .anyMatch(b -> Objects.equals(book.getName(), b.getName()));
            Assertions.assertTrue(found);
        }
    }



    @Test
    void createNewBookStatusCreated() {
        String nameBook = "TestBook";

        Book newBook = webTestClient.post()
                .bodyValue(nameBook)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Book.class)
                .returnResult()
                .getResponseBody();
        assertEquals(nameBook, newBook.getName());
    }
    @Test
    void deleteBookByIdStatusOk() {
        String nameBook = "Book";
        bookService.addNewBook(nameBook);
        Long id = bookService.findByName(nameBook).getId();
        bookService.deleteById(id);

        Book deleteBook = webTestClient.get()
                .uri("/" + id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Book.class)
                .returnResult()
                .getResponseBody();
        assertNull(deleteBook);
    }
}