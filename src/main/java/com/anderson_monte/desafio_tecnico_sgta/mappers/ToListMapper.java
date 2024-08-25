package com.anderson_monte.desafio_tecnico_sgta.mappers;

import com.anderson_monte.desafio_tecnico_sgta.dtos.ListInput;
import com.anderson_monte.desafio_tecnico_sgta.models.List;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ToListMapper implements Function<ListInput, List> {
    @Override
    public List apply(ListInput listInput) {
        return new List(
                listInput.name()
        );
    }
}
