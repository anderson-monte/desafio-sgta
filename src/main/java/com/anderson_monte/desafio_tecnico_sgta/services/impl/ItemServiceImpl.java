package com.anderson_monte.desafio_tecnico_sgta.services.impl;

import com.anderson_monte.desafio_tecnico_sgta.dtos.ItemDTO;
import com.anderson_monte.desafio_tecnico_sgta.dtos.ItemInput;
import com.anderson_monte.desafio_tecnico_sgta.dtos.ItemOutput;
import com.anderson_monte.desafio_tecnico_sgta.dtos.ListOutput;
import com.anderson_monte.desafio_tecnico_sgta.exceptions.EntityNotFound;
import com.anderson_monte.desafio_tecnico_sgta.mappers.ToItemDTOMapper;
import com.anderson_monte.desafio_tecnico_sgta.mappers.ToItemOutputMapper;
import com.anderson_monte.desafio_tecnico_sgta.mappers.ToListOutputMapper;
import com.anderson_monte.desafio_tecnico_sgta.models.Item;
import com.anderson_monte.desafio_tecnico_sgta.models.State;
import com.anderson_monte.desafio_tecnico_sgta.repositories.ItemRepository;
import com.anderson_monte.desafio_tecnico_sgta.repositories.ListRepository;
import com.anderson_monte.desafio_tecnico_sgta.services.ItemService;
import com.anderson_monte.desafio_tecnico_sgta.specifications.ItemSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ListRepository listRepository;
    private final ToItemOutputMapper toItemOutputMapper;
    private final ToListOutputMapper toListOutputMapper;
    private final ToItemDTOMapper toItemDTOMapper;

    private static final String ERROR_MESSAGE = "Something went wrong. Please try again";

    @Override
    @Transactional(readOnly = true)
    public Page<ItemDTO> getAll(
            String name,
            String listName,
            Integer page,
            Integer size
    ) {
        Specification<Item> spec = Specification
                .where(ItemSpec.hasName(name))
                .and(ItemSpec.hasList(listName));

        return itemRepository
                .findAll(spec, PageRequest.of(page, size).withSort(Sort.by(Sort.Direction.DESC, "highlighted")))
                .map(item -> {
                    item.getList().sortItemsByHighlight();
                    return item;
                })
                .map(toItemDTOMapper);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemDTO getById(Long id) {
        return Optional.of(this.findItem(id))
                .map(toItemDTOMapper)
                .orElseThrow(() -> new RuntimeException(ERROR_MESSAGE));
    }

    @Override
    @Transactional
    public ListOutput addOrUpdate(ItemInput itemInput) {
        var item = this.validateItem(itemInput);

        return Optional.of(item.getList())
                .map(list -> {
                    list.getItems().add(item);
                    return this.listRepository.save(list);
                })
                .map(list -> {
                    list.sortItemsByHighlight();
                    return list;
                })
                .map(toListOutputMapper)
                .orElseThrow(() -> new RuntimeException(ERROR_MESSAGE));
    }

    @Override
    @Transactional
    public ItemOutput alterItemState(Long id, State state) {
        return Optional.of(this.findItem(id))
                .map(item -> {
                    item.alterState(state);
                    return this.itemRepository.save(item);
                })
                .map(list -> {
                    list.getList().sortItemsByHighlight();
                    return list;
                })
                .map(toItemOutputMapper)
                .orElseThrow(() -> new RuntimeException(ERROR_MESSAGE));
    }

    @Override
    @Transactional
    public ItemOutput highlightItem(Long id) {
        return Optional.of(this.findItem(id))
                .map(item -> {
                    item.highlight();
                    return this.itemRepository.save(item);
                })
                .map(toItemOutputMapper)
                .orElseThrow(() -> new RuntimeException(ERROR_MESSAGE));
    }

    @Override
    @Transactional
    public ItemOutput removeHighlight(Long id) {
        return Optional.of(this.findItem(id))
                .map(item -> {
                    item.unhighlight();
                    return this.itemRepository.save(item);
                })
                .map(toItemOutputMapper)
                .orElseThrow(() -> new RuntimeException(ERROR_MESSAGE));
    }

    private Item findItem(Long id) {
        return this.itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFound("Item with id " + id + " not found"));
    }

    private Item validateItem(ItemInput item) {
        if (item.list().isBlank())
            throw new IllegalArgumentException("List name is required");

        var list = this.listRepository.findByName(item.list().toUpperCase())
                .orElseThrow(() -> new EntityNotFound("List which Item belongs to not found"));

        return new Item(item.id(), item.name(), list);
    }
}
