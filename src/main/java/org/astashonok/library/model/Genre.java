package org.astashonok.library.model;

import lombok.*;
import org.astashonok.library.model.abstracts.Entity;

import javax.xml.bind.annotation.*;

@EqualsAndHashCode()
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@XmlRootElement(name = "genre")
@XmlAccessorType(XmlAccessType.FIELD)
public class Genre extends Entity {
    @XmlAttribute
    private String name;

    @Builder
    public Genre(long id, String name) {
        super(id);
        this.name = name;
    }
}
