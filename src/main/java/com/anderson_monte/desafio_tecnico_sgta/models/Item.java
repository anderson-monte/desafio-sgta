package com.anderson_monte.desafio_tecnico_sgta.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    private List list;

    @Enumerated(EnumType.STRING)
    private State state = State.PENDING;
    private Boolean highlighted = false;

    public Item(Long id, String name, List list) {
        validate(name, list);
        this.id = id;
        this.name = name.toUpperCase();
        this.list = list;
    }

    public void alterState(State state) {
        this.state = state;
    }

    public void highlight() {
        this.highlighted = true;
    }

    public void unhighlight() {
        this.highlighted = false;
    }

    private void validate(String name, List list) {
        if (Objects.isNull(list))
            throw new IllegalArgumentException("List is required");

        if (name.isBlank())
            throw new IllegalArgumentException("Item name is required");

        if (name.length() > 50)
            throw new IllegalArgumentException("Item name must have at most 50 characters");
    }
}
