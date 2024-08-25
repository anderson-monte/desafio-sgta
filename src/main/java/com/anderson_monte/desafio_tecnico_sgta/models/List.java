package com.anderson_monte.desafio_tecnico_sgta.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Comparator;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class List {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    @OneToMany(
            mappedBy = "list",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private java.util.List<Item> items;

    public List(String name) {
        validate(name);
        this.name = name.toUpperCase();
        this.items = new java.util.ArrayList<>();
    }

    public void sortItemsByHighlight() {
        items.sort(Comparator.comparing(Item::getHighlighted, Comparator.nullsLast(Comparator.reverseOrder())));
    }

    private void validate(String name) {
        if (name.isBlank())
            throw new IllegalArgumentException("List name is required");
    }
}
