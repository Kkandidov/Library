package org.astashonok.library.model;

import lombok.*;
import org.astashonok.library.model.abstracts.Entity;

import javax.xml.bind.annotation.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode()
@XmlRootElement (name="author")
@XmlAccessorType(XmlAccessType.FIELD)
public class Author extends Entity {
    @XmlAttribute
    private String name;
    @XmlAttribute
    private String surname;

    @Builder
    public Author(long id, String name, String surname) {
        super(id);
        this.name = name;
        this.surname = surname;
    }
}
