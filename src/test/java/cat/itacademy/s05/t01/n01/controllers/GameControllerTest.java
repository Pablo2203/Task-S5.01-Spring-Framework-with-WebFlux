package cat.itacademy.s05.t01.n01.controllers;

import cat.itacademy.s05.t01.n01.model.Game;
import cat.itacademy.s05.t01.n01.services.GameService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@WebFluxTest(controllers = GameController.class)
public class GameControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GameService gameService;

    private static Game game;

    @BeforeAll
    static void setup() {
        game = new Game();
        game.setId("1");
        game.setPlayerId("player1");
        game.setCurrentScore(0);
        game.setCards(Arrays.asList("Ace of Spades", "King of Hearts"));
        game.setFinished(false);
    }

    @Test
    void testCreateGame() {
        Mockito.when(gameService.createGame(game)).thenReturn(Mono.just(game));
        webTestClient.post()
                .uri("/game/new")
                .body(Mono.just(game), Game.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Game.class)
                .value(response -> assertEquals(game.getId(), response.getId()));
    }

    @Test
    void testPlayGame() {
        String gameId = "1";
        String action = "hit";

        Mockito.when(gameService.playGame(gameId, action)).thenReturn(Mono.just(game));

        webTestClient.post()
                .uri("/game/{id}/play?action={action}", gameId, action)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Game.class)
                .value(response -> assertEquals(game.getId(), response.getId()));
    }

    @Test
    void testGetGamesByPlayerId() {
        String playerId = "player1";

        Mockito.when(gameService.getGamesByPlayerId(playerId)).thenReturn(Flux.just(game));

        webTestClient.get()
                .uri("/game/player/{playerId}", playerId)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Game.class)
                .value(response -> {
                    assertNotNull(response);
                    assertEquals(1, response.size());
                    assertEquals(game.getId(), response.get(0).getId());
                });
    }

    @Test
    void testGetGameById() {
        String gameId = "1";

        Mockito.when(gameService.getGameById(gameId)).thenReturn(Mono.just(game));

        webTestClient.get()
                .uri("/game/{id}", gameId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Game.class)
                .value(response -> assertEquals(game.getId(), response.getId()));
    }

    @Test
    void testGetGameByIdNotFound() {
        String gameId = "2"; // Un ID inexistente.

        Mockito.when(gameService.getGameById(gameId)).thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/game/{id}", gameId)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testFinishGame() {
        String gameId = "1";
        game.setFinished(true); // Indicar que el juego finalizó.

        Mockito.when(gameService.finishGame(gameId)).thenReturn(Mono.just(game));

        webTestClient.patch()
                .uri("/game/{id}/finish", gameId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Game.class)
                .value(response -> {
                    assertEquals(game.getId(), response.getId());
                    assertEquals(true, response.isFinished());
                });
    }

    @Test
    void testFinishGameNotFound() {
        String gameId = "2"; // Un ID inexistente.

        Mockito.when(gameService.finishGame(gameId)).thenReturn(Mono.empty());

        webTestClient.patch()
                .uri("/game/{id}/finish", gameId)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testDeleteGame() {
        String gameId = "1";

        Mockito.when(gameService.getGameById(gameId)).thenReturn(Mono.just(game));
        Mockito.when(gameService.deleteGame(gameId)).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/game/{id}/delete", gameId)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testDeleteGameNotFound() {
        String gameId = "2"; // Un ID inexistente.

        Mockito.when(gameService.getGameById(gameId)).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/game/{id}/delete", gameId)
                .exchange()
                .expectStatus().isNotFound();
    }
}