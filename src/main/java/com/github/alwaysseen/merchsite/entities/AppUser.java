package com.github.alwaysseen.merchsite.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.alwaysseen.merchsite.services.LocalDateToStringConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class AppUser {
    @Id
    @SequenceGenerator(
            name = "userSequence",
            sequenceName = "userSequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "userSequence"
    )
    private Integer id;
    private String fname;
    private String lname;
    private String email;
    @JsonIgnore
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Convert(converter = LocalDateToStringConverter.class)
    private LocalDate creationDate;

    public AppUser(String fname, String lname, String email, String password, Role role, LocalDate creationDate) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.password = password;
        this.role = role;
        this.creationDate = creationDate;
    }
}
