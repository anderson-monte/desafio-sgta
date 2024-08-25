package com.anderson_monte.desafio_tecnico_sgta.controllers;

import com.anderson_monte.desafio_tecnico_sgta.dtos.ListInput;
import com.anderson_monte.desafio_tecnico_sgta.dtos.ListOutput;
import com.anderson_monte.desafio_tecnico_sgta.services.ListService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("lists")
@RequiredArgsConstructor
public class ListController {
    private final ListService service;

    @GetMapping
    public Page<ListOutput> listAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Optional<Integer> page,
            @RequestParam(required = false) Optional<Integer> size
    ) {
        return service.getAll(
                name,
                page.orElse(0),
                size.orElse(10)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListOutput> getList(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<ListOutput> saveList(@RequestBody ListInput category) {
        return ResponseEntity.status(CREATED).body(service.save(category));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteList(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/items/{itemId}")
    public ResponseEntity<ListOutput> deleteItem(@PathVariable Long id, @PathVariable Long itemId) {
        return ResponseEntity.ok(service.deleteItem(id, itemId));
    }
}
