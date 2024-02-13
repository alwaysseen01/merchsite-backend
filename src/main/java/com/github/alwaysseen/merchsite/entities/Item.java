package com.github.alwaysseen.merchsite.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Item {
    @Id
    @SequenceGenerator(
            name="item_sequence",
            sequenceName = "item_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "item_sequence"
    )
    private Integer id;
    private String name;
    private String desc;
    private Double price;
    private Integer quantity;
    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;
    @JsonIgnore
    @OneToMany(mappedBy = "photo")
    private List<Photo> photos;

    public Item(String name, String desc, Double price, Integer quantity, Category category) {
        this.name = name;
        this.desc = desc;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
    }
}
