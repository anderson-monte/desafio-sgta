package com.anderson_monte.desafio_tecnico_sgta.specifications;

import com.anderson_monte.desafio_tecnico_sgta.models.List;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

public abstract class ListSpec {
    public static Specification<List> hasName(String name) {
        return (root, criteriaQuery, criteriaBuilder) ->
                Objects.isNull(name) ?
                        criteriaBuilder.conjunction() :
                        criteriaBuilder.like(root.get("name"), "%" + name.toUpperCase() + "%");
    }
}
