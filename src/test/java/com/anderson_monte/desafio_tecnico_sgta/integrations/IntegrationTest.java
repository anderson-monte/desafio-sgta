package com.anderson_monte.desafio_tecnico_sgta.integrations;

import com.anderson_monte.desafio_tecnico_sgta.dtos.ListInput;
import com.anderson_monte.desafio_tecnico_sgta.dtos.ListOutput;
import com.anderson_monte.desafio_tecnico_sgta.services.ListService;
import com.anderson_monte.desafio_tecnico_sgta.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Integration Tests")
class IntegrationTest {
    @Autowired
    private WebTestClient testClient;
    @Autowired
    private ListService listService;

    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.3-alpine3.20")
            .withReuse(true);

    @DynamicPropertySource
    static void setUpContainers (DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @BeforeAll
    static void startUpContainers() {
        postgreSQLContainer.start();
    }

    @Test
    @DisplayName("Should return a lpaged list of lists")
    void listAll_shouldReturnAPagedListOfLists_WhenSuccessful() {
        ListInput listInput_1 = new ListInput("List name 1");
        ListInput listInput_2 = new ListInput("List name 2");

        var listSaved_1 = this.testClient.post()
                .uri("/lists")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(listInput_1))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ListOutput.class)
                .returnResult()
                .getResponseBody();

        var listSaved_2 = this.testClient.post()
                .uri("/lists")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(listInput_2))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ListOutput.class)
                .returnResult()
                .getResponseBody();

        PageableResponse<ListOutput> agentePage = this.testClient.get()
                .uri(uriBuilder -> uriBuilder.path("/lists")
                        .queryParam("page", 0)
                        .queryParam("size", 10)
                        .build()
                )
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<PageableResponse<ListOutput>>() {
                })
                .consumeWith(System.out::println)
                .returnResult()
                .getResponseBody();

        java.util.List<ListOutput> listsSaved = java.util.List.of(
                listSaved_1,
                listSaved_2
        );

        Assertions.assertThat(agentePage)
                .isNotNull();

        Assertions.assertThat(agentePage.getContent())
                .isNotEmpty()
                .hasSize(listsSaved.size())
                .containsAll(listsSaved);
    }

    @Test
    @DisplayName("Should return a empty list when trying search for a non existent liist with a name passed as parameter")
    void listAll_shouldReturnAnEmptyList_WhenTryingSearchForANonExistentListWithANamePassedAsParameter() {
        PageableResponse<ListOutput> agentePage = this.testClient.get()
                .uri(uriBuilder -> uriBuilder.path("/lists")
                        .queryParam("name", "No name")
                        .queryParam("page", 0)
                        .queryParam("size", 10)
                        .build()
                )
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<PageableResponse<ListOutput>>() {
                })
                .consumeWith(System.out::println)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(agentePage)
                .isNotNull();

        Assertions.assertThat(agentePage.getContent())
                .isEmpty();
    }
}
