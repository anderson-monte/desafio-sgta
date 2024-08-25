package com.anderson_monte.desafio_tecnico_sgta.services;

import com.anderson_monte.desafio_tecnico_sgta.dtos.ListInput;
import com.anderson_monte.desafio_tecnico_sgta.dtos.ListOutput;
import org.springframework.data.domain.Page;

import java.util.Set;

public interface ListService {
   Page<ListOutput> getAll(String name, Integer page, Integer size);
    ListOutput getById(Long id);
    ListOutput save(ListInput category);
    void delete(Long id);
    ListOutput deleteItem(Long id, Long itemId);
}
