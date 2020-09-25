package org.astashonok.library.model;

import lombok.*;
import org.astashonok.library.model.abstracts.Entity;
import org.astashonok.library.model.adapters.DateAdapter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode()
@XmlRootElement(name = "book")
@XmlAccessorType(XmlAccessType.FIELD)
public class Book extends Entity {
    @XmlAttribute
    private String name;
    @XmlElementWrapper(name = "authors")
    @XmlElement(name = "author")
    private List<Author> authors;
    @XmlElementWrapper(name = "genres")
    @XmlElement(name = "genre")
    private List<Genre> genres;
    @XmlAttribute
    private String isbn;
    @XmlAttribute
    @XmlJavaTypeAdapter(DateAdapter.class)
    private Date date;

    @Builder
    public Book(long id, List<Author> authors, String name, List<Genre> genres, String isbn, Date date) {
        super(id);
        this.name = name;
        this.authors = authors;
        this.genres = genres;
        this.isbn = isbn;
        this.date = date;
    }
}
