package by.aston.entity;

import lombok.*;

import java.util.List;

@Data
@Setter
@Getter

@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private Long id;
    private String title;
    private String isbn;
    private int publicationYear;
    private List<Author> authors;
}