package com.anderson_monte.desafio_tecnico_sgta.repositories;

import com.anderson_monte.desafio_tecnico_sgta.models.List;
import com.anderson_monte.desafio_tecnico_sgta.models.State;
import com.anderson_monte.desafio_tecnico_sgta.utils.TestHelper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@DataJpaTest
@EnableJpaAuditing
@DisplayName("Tests for repositories")
class RepositoriesTest {
    @Autowired
    private ListRepository listRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("Should persist a list when successful")
    void save_PersistList_WhenSuccessful() {
       var listToBeSaved = TestHelper.createList();
         var listSaved = listRepository.save(listToBeSaved);

        Assertions.assertThat(listSaved).isNotNull();
        Assertions.assertThat(listSaved.getId()).isNotNull();
        Assertions.assertThat(listSaved.getName()).isEqualTo(listToBeSaved.getName());
        Assertions.assertThat(listSaved.getItems()).isEmpty();
    }

    @Test
    @DisplayName("Should delete a list when successful")
    void delete_RemoveList_WhenSuccessful() {
        var listToBeSaved = TestHelper.createList();
        var listSaved = listRepository.save(listToBeSaved);
        em.flush();

        listRepository.delete(listSaved);
        em.flush();

        var listFound = listRepository.findById(listSaved.getId());

        Assertions.assertThat(listFound).isEmpty();
    }

    @Test
    @DisplayName("Shooud throw an DataIntegrityViolationException when trying to save a list whith a null name")
    void save_ThrowDataIntegrityViolationException_WhenNameIsNull() {
        var list = new List();
        Assertions.assertThatThrownBy(() -> listRepository.save(list))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Should persist a item when successful")
    void save_PersistItem_WhenSuccessful() {
        var listToBeSaved = TestHelper.createList();
        var listSaved = listRepository.save(listToBeSaved);
        em.flush();

        var itemToBeSaved = TestHelper.createItem(listSaved);
        var itemSaved = itemRepository.save(itemToBeSaved);

        Assertions.assertThat(itemSaved).isNotNull();
        Assertions.assertThat(itemSaved.getId()).isNotNull();
        Assertions.assertThat(itemSaved.getName()).isEqualTo(itemToBeSaved.getName());
        Assertions.assertThat(itemSaved.getList()).isEqualTo(itemToBeSaved.getList());
        Assertions.assertThat(itemSaved.getHighlighted()).isFalse();
        Assertions.assertThat(itemSaved.getState()).isEqualTo(State.PENDING);
    }

    @Test
    @DisplayName("Should delete a item when successful")
    void delete_RemoveItem_WhenSuccessful() {
        var listToBeSaved = TestHelper.createList();
        var listSaved = listRepository.save(listToBeSaved);
        em.flush();

        var itemToBeSaved = TestHelper.createItem(listSaved);
        var itemSaved = itemRepository.save(itemToBeSaved);
        em.flush();

        itemRepository.delete(itemSaved);
        em.flush();

        var itemFound = itemRepository.findById(itemSaved.getId());

        Assertions.assertThat(itemFound).isEmpty();
    }
}
