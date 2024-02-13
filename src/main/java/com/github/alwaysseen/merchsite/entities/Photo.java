package com.github.alwaysseen.merchsite.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Photo {
    @Id
    @SequenceGenerator(
            name="photo_sequence",
            sequenceName = "photo_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "photo_sequence"
    )
    private Integer id;
    private String url;
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    public Photo(String url, Item item) {
        this.url = url;
        this.item = item;
    }
}
