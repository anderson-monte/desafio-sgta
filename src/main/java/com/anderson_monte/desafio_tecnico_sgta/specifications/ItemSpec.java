package com.anderson_monte.desafio_tecnico_sgta.specifications;

import com.anderson_monte.desafio_tecnico_sgta.models.Item;
import com.anderson_monte.desafio_tecnico_sgta.models.List;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

public abstract class ItemSpec {
    public static Specification<Item> hasName(String name) {
        return (root, criteriaQuery, criteriaBuilder) ->
                Objects.isNull(name) ?
                        criteriaBuilder.conjunction() :
                        criteriaBuilder.like(root.get("name"), "%" + name.toUpperCase() + "%");
    }


    public static Specification<Item> hasList(String nameList) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            Join<Item, List> pessoaJoin = root.join("list", JoinType.INNER);

            return Objects.isNull(nameList) ?
                    criteriaBuilder.conjunction() :
                    criteriaBuilder.like(pessoaJoin.get("name"), "%" + nameList.toUpperCase() + "%");
        };
    }
}
