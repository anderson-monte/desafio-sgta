package com.anderson_monte.desafio_tecnico_sgta.services;

import com.anderson_monte.desafio_tecnico_sgta.dtos.ItemDTO;
import com.anderson_monte.desafio_tecnico_sgta.dtos.ItemInput;
import com.anderson_monte.desafio_tecnico_sgta.dtos.ItemOutput;
import com.anderson_monte.desafio_tecnico_sgta.dtos.ListOutput;
import com.anderson_monte.desafio_tecnico_sgta.models.State;
import org.springframework.data.domain.Page;

public interface ItemService {
    Page<ItemDTO> getAll(String name, String listName, Integer page, Integer size);
    ItemDTO getById(Long id);
    ListOutput addOrUpdate(ItemInput itemInput);
    ItemOutput alterItemState(Long id, State state);
    ItemOutput highlightItem(Long id);
    ItemOutput removeHighlight(Long id);
}
