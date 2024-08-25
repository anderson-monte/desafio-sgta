package com.anderson_monte.desafio_tecnico_sgta.dtos;

public record ListOutput(
        Long id,
        String name,
        java.util.List<ItemOutput> items
) {
}
