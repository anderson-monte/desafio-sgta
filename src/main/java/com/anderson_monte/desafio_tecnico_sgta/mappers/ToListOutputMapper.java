package com.anderson_monte.desafio_tecnico_sgta.mappers;

import com.anderson_monte.desafio_tecnico_sgta.dtos.ListOutput;
import com.anderson_monte.desafio_tecnico_sgta.models.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ToListOutputMapper implements Function<List, ListOutput> {
    private final ToItemOutputMapper toItemOutputMapper;
    @Override
    public ListOutput apply(List list) {
        return new ListOutput(
                list.getId(),
                list.getName(),
                list.getItems().stream().map(toItemOutputMapper).toList()
        );
    }
}
