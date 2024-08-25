package com.anderson_monte.desafio_tecnico_sgta.controllers;

import com.anderson_monte.desafio_tecnico_sgta.dtos.ItemDTO;
import com.anderson_monte.desafio_tecnico_sgta.dtos.ItemInput;
import com.anderson_monte.desafio_tecnico_sgta.dtos.ItemOutput;
import com.anderson_monte.desafio_tecnico_sgta.dtos.ListOutput;
import com.anderson_monte.desafio_tecnico_sgta.models.State;
import com.anderson_monte.desafio_tecnico_sgta.services.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService service;

    @GetMapping
    public Page<ItemDTO> listAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String listName,
            @RequestParam(required = false) Optional<Integer> page,
            @RequestParam(required = false) Optional<Integer> size
    ) {
        return service.getAll(
                name,
                listName,
                page.orElse(0),
                size.orElse(10)
        );
    }

    @PostMapping
    public ResponseEntity<ListOutput> saveItem(@RequestBody ItemInput item) {
        return ResponseEntity.status(CREATED).body(service.addOrUpdate(item));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDTO> getItem(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping("/{id}/active")
    public ResponseEntity<ItemOutput> activeItem(@PathVariable Long id) {
        return ResponseEntity.ok(service.alterItemState(id, State.ACTIVATED));
    }

    @PostMapping("/{id}/inactive")
    public ResponseEntity<ItemOutput> inactiveItem(@PathVariable Long id) {
        return ResponseEntity.ok(service.alterItemState(id, State.INACTIVATED));
    }

    @PostMapping("/{id}/highlight")
    public ResponseEntity<ItemOutput> highlightItem(@PathVariable Long id) {
        return ResponseEntity.ok(service.highlightItem(id));
    }

    @PostMapping("/{id}/remove-highlight")
    public ResponseEntity<ItemOutput> removeHighlight(@PathVariable Long id) {
        return ResponseEntity.ok(service.removeHighlight(id));
    }
}
