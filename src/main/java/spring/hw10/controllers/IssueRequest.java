package spring.hw10.controllers;

import lombok.Data;

@Data
public class IssueRequest {
    private Long idReader;
    private Long bookId;
}
