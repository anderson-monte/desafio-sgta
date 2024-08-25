package com.anderson_monte.desafio_tecnico_sgta.mappers;

import com.anderson_monte.desafio_tecnico_sgta.dtos.ItemDTO;
import com.anderson_monte.desafio_tecnico_sgta.models.Item;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ToItemDTOMapper implements Function<Item, ItemDTO> {
    @Override
    public ItemDTO apply(Item item) {
        return new ItemDTO(
                item.getId(),
                item.getName(),
                item.getState().name(),
                item.getHighlighted(),
                item.getList().getName()
        );
    }
}
