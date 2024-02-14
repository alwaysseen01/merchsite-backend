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
public class Category {
    @Id
    @SequenceGenerator(
            name="category_sequence",
            sequenceName = "category_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "category_sequence"
    )
    private Integer id;
    private String name;
    @JsonIgnore
    @OneToMany(mappedBy = "category")
    private List<Item> items;


    public Category(String name) {
        this.name = name;
    }
}
