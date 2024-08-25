package com.anderson_monte.desafio_tecnico_sgta.services;

import com.anderson_monte.desafio_tecnico_sgta.dtos.ListInput;
import com.anderson_monte.desafio_tecnico_sgta.mappers.ToItemOutputMapper;
import com.anderson_monte.desafio_tecnico_sgta.mappers.ToListMapper;
import com.anderson_monte.desafio_tecnico_sgta.mappers.ToListOutputMapper;
import com.anderson_monte.desafio_tecnico_sgta.models.Item;
import com.anderson_monte.desafio_tecnico_sgta.models.List;
import com.anderson_monte.desafio_tecnico_sgta.repositories.ItemRepository;
import com.anderson_monte.desafio_tecnico_sgta.repositories.ListRepository;
import com.anderson_monte.desafio_tecnico_sgta.services.impl.ItemServiceImpl;
import com.anderson_monte.desafio_tecnico_sgta.services.impl.ListServiceImpl;
import com.anderson_monte.desafio_tecnico_sgta.utils.TestHelper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DisplayName("Tests for services")
class ServicesTest {
    @InjectMocks
    private ListServiceImpl listService;
    @InjectMocks
    private ItemServiceImpl itemService;

    @Mock
    private ListRepository listRepositoryMock;
    @Mock
    private ItemRepository itemRepositoryMock;
    @Mock
    private ToListMapper toListMapperMock;
    @Mock
    private ToListOutputMapper toListOutputMapperMock;
    @Mock
    private ToItemOutputMapper toItemOutputMapperMock;

    @BeforeEach
    void setUp() {
        BDDMockito.when(listRepositoryMock.save(ArgumentMatchers.any(List.class)))
                .thenReturn(TestHelper.createPersistedList());
        BDDMockito.when(listRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(java.util.Optional.of(TestHelper.createPersistedList()));
        BDDMockito.doNothing().when(listRepositoryMock).delete(ArgumentMatchers.any(List.class));
        BDDMockito.when(listRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(java.util.Optional.of(TestHelper.createPersistedList()));
        BDDMockito.when(itemRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(java.util.Optional.of(TestHelper.createItem(TestHelper.createPersistedList())));

        BDDMockito.when(toListMapperMock.apply(ArgumentMatchers.any(ListInput.class)))
                .thenReturn(TestHelper.createList());
        BDDMockito.when(toListOutputMapperMock.apply(ArgumentMatchers.any(List.class)))
                .thenReturn(TestHelper.createListOutput());

    }

    @Test
    @DisplayName("Should return a ListOutput when successful")
    void save_ReturnListOutput_WhenSuccessful() {
        var listInput = TestHelper.createListInput();

        var listOutput = listService.save(listInput);

        Assertions.assertThat(listOutput).isNotNull();
        Assertions.assertThat(listOutput.id()).isNotNull();
        Assertions.assertThat(listOutput.name()).isEqualTo(listInput.name());
        Assertions.assertThat(listOutput.items()).isEmpty();
    }

    @Test
    @DisplayName("Should return void when trying to delete a list")
    void delete_RemoveList_WhenSuccessful() {
        Assertions.assertThatCode(() -> listService.delete(1L))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should return a ListOutput when trying to get a list by id")
    void getById_ReturnListOutput_WhenSuccessful() {
        var listOutput = listService.getById(1L);

        Assertions.assertThat(listOutput).isNotNull();
        Assertions.assertThat(listOutput.id()).isNotNull();
        Assertions.assertThat(listOutput.name()).isEqualTo(TestHelper.createPersistedList().getName());
        Assertions.assertThat(listOutput.items()).isEmpty();
    }

    @Test
    @DisplayName("Should add an item to a list when successful")
    void addOrUpdate_AddItemToList_WhenSuccessful() {
        BDDMockito.when(listRepositoryMock.save(ArgumentMatchers.any(List.class)))
                .thenReturn(TestHelper.createListWithItems());
        BDDMockito.when(toListOutputMapperMock.apply(ArgumentMatchers.any(List.class)))
                .thenReturn(TestHelper.createListOutputWithItems());

        var itemInput = TestHelper.createItemInput(TestHelper.createPersistedList());

        var listOutput = itemService.addOrUpdate(itemInput);

        Assertions.assertThat(listOutput).isNotNull();
        Assertions.assertThat(listOutput.id()).isNotNull();
        Assertions.assertThat(listOutput.name()).isEqualTo(TestHelper.createPersistedList().getName());
        Assertions.assertThat(listOutput.items()).isNotEmpty();
    }

    @Test
    @DisplayName("Should item with highlight equals true when successful")
    void highlightItem_HighlightItem_WhenSuccessful() {
        BDDMockito.when(itemRepositoryMock.save(ArgumentMatchers.any(Item.class)))
                .thenReturn(TestHelper.itemHighlight(TestHelper.createPersistedList()));

        BDDMockito.when(toItemOutputMapperMock.apply(ArgumentMatchers.any(Item.class)))
                .thenReturn(TestHelper.itemOutputHighlight());

        var itemOutput = itemService.highlightItem(1L);

        Assertions.assertThat(itemOutput).isNotNull();
        Assertions.assertThat(itemOutput.id()).isNotNull();
        Assertions.assertThat(itemOutput.name()).isEqualTo(TestHelper.createItem(TestHelper.createPersistedList()).getName());
        Assertions.assertThat(itemOutput.highlighted()).isTrue();
    }
}
