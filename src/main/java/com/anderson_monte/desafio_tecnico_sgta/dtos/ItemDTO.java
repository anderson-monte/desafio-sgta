package com.anderson_monte.desafio_tecnico_sgta.dtos;

public record ItemDTO(
        Long id,
        String name,
        String state,
        Boolean highlighted,
        String listName
) {
}
