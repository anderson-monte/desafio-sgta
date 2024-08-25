package com.anderson_monte.desafio_tecnico_sgta.services.impl;

import com.anderson_monte.desafio_tecnico_sgta.dtos.ListInput;
import com.anderson_monte.desafio_tecnico_sgta.dtos.ListOutput;
import com.anderson_monte.desafio_tecnico_sgta.exceptions.EntityNotFound;
import com.anderson_monte.desafio_tecnico_sgta.mappers.ToListMapper;
import com.anderson_monte.desafio_tecnico_sgta.mappers.ToListOutputMapper;
import com.anderson_monte.desafio_tecnico_sgta.models.List;
import com.anderson_monte.desafio_tecnico_sgta.repositories.ListRepository;
import com.anderson_monte.desafio_tecnico_sgta.services.ListService;
import com.anderson_monte.desafio_tecnico_sgta.specifications.ListSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ListServiceImpl implements ListService {
    private final ListRepository repository;
    private final ToListOutputMapper toListOutputMapper;
    private final ToListMapper toListMapper;

    private static final String ERROR_MESSAGE = "Something went wrong. Please try again";

    @Override
    @Transactional(readOnly = true)
    public Page<ListOutput> getAll(
            String name,
            Integer page,
            Integer size
    ) {
        Specification<List> spec = Specification.where(ListSpec.hasName(name));

        return repository
                .findAll(spec, PageRequest.of(page, size))
                .map(list -> {
                    list.sortItemsByHighlight();
                    return list;
                })
                .map(toListOutputMapper);
    }

    @Override
    @Transactional(readOnly = true)
    public ListOutput getById(Long id) {
        return Optional.of(this.findList(id))
                .map(list -> {
                    list.sortItemsByHighlight();
                    return list;
                })
                .map(toListOutputMapper)
                .orElseThrow(() -> new RuntimeException(ERROR_MESSAGE));
    }

    @Override
    @Transactional
    public ListOutput save(ListInput list) {
        return Optional.of(list)
                .map(toListMapper)
                .map(this::validateList)
                .map(repository::save)
                .map(toListOutputMapper)
                .orElseThrow(() -> new RuntimeException(ERROR_MESSAGE));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.findById(id)
                .ifPresentOrElse(repository::delete, () -> {
                    throw new EntityNotFound("List with id " + id + " not found");
                });
    }

    @Override
    @Transactional
    public ListOutput deleteItem(Long id, Long itemId) {
        return Optional.of(this.findList(id))
                .map(list -> {
                    boolean itemExists = list.getItems().stream()
                            .anyMatch(item -> item.getId().equals(itemId));
                    if (!itemExists) {
                        throw new EntityNotFound("No item with id " + itemId + " found in the list");
                    }
                    list.getItems().removeIf(item -> item.getId().equals(itemId));
                    return list;
                })
                .map(repository::save)
                .map(toListOutputMapper)
                .orElseThrow(() -> new RuntimeException(ERROR_MESSAGE));
    }

    private List findList(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFound("List with id " + id + " not found"));
    }

    private List validateList(List list) {
        var exists = repository.existsByName(list.getName());
        if (exists)
            throw new IllegalArgumentException("List with name " + list.getName() + " already exists");
        return list;
    }
}
