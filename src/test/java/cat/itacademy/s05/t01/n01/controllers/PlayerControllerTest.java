package cat.itacademy.s05.t01.n01.controllers;

import cat.itacademy.s05.t01.n01.model.Player;
import cat.itacademy.s05.t01.n01.services.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
@ActiveProfiles("test")
@WebFluxTest(controllers = PlayerController.class)
class PlayerControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private PlayerService playerService;


    private Player player;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        player = new Player();
        player.setId(1);
        player.setName("Test Player");
    }

    @Test
    void testCreatePlayer() {
        Mockito.when(playerService.createPlayer(any(Player.class)))
                .thenReturn(Mono.just(player));

        webTestClient.post()
                .uri("/players")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(player)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Player.class)
                .value(response -> {
                    assert response.getId() == player.getId();
                    assert response.getName().equals(player.getName());
                });
    }

    @Test
    void testGetPlayerByIdSuccess() {
        Mockito.when(playerService.getPlayerById(eq(1)))
                .thenReturn(Mono.just(player));

        webTestClient.get()
                .uri("/players/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Player.class)
                .value(response -> {
                    assert response.getId() == player.getId();
                    assert response.getName().equals(player.getName());
                });
    }

    @Test
    void testGetPlayerByIdNotFound() {
        Mockito.when(playerService.getPlayerById(eq(1)))
                .thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/players/1")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testGetAllPlayers() {
        Player player2 = new Player();
        player2.setId(2);
        player2.setName("Second Player");

        Mockito.when(playerService.getAllPlayers())
                .thenReturn(Flux.just(player, player2));

        webTestClient.get()
                .uri("/players")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Player.class)
                .value(players -> {
                    assert players.size() == 2;
                    assert players.get(0).getId() == player.getId();
                    assert players.get(1).getId() == player2.getId();
                });
    }

    @Test
    void testDeletePlayerById() {
        Mockito.when(playerService.deletePlayerById(eq(1)))
                .thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/players/1")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testUpdatePlayerById() {
        Player updatedPlayer = new Player();
        updatedPlayer.setId(1);
        updatedPlayer.setName("Updated Player");

        Mockito.when(playerService.getPlayerById(eq(1)))
                .thenReturn(Mono.just(player));
        Mockito.when(playerService.createPlayer(any(Player.class)))
                .thenReturn(Mono.just(updatedPlayer));

        webTestClient.put()
                .uri("/players/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedPlayer)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Player.class)
                .value(response -> {
                    assert response.getId() == updatedPlayer.getId();
                    assert response.getName().equals("Updated Player");
                });
    }
}