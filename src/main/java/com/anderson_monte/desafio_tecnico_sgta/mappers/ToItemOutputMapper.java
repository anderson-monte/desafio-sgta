package com.anderson_monte.desafio_tecnico_sgta.mappers;

import com.anderson_monte.desafio_tecnico_sgta.dtos.ItemOutput;
import com.anderson_monte.desafio_tecnico_sgta.models.Item;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ToItemOutputMapper implements Function<Item, ItemOutput> {
    @Override
    public ItemOutput apply(Item item) {
        return new ItemOutput(
                item.getId(),
                item.getName(),
                item.getState().name(),
                item.getHighlighted()
        );
    }
}
