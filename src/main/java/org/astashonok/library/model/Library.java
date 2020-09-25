package org.astashonok.library.model;

import lombok.*;

import javax.xml.bind.annotation.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString()
@EqualsAndHashCode()
@Builder
@XmlRootElement(name = "library")
@XmlAccessorType(XmlAccessType.FIELD)
public class Library {
    @XmlElementWrapper(name = "books")
    @XmlElement(name = "book")
    private List<Book> books;
}
