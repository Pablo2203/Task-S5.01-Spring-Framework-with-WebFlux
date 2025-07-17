package cat.itacademy.s05.t01.n01.controllers;

import cat.itacademy.s05.t01.n01.model.Player;
import cat.itacademy.s05.t01.n01.services.PlayerService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@WebFluxTest(controllers = PlayerController.class)
public class PlayerControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private PlayerService playerService;

    private static Player player;

    @BeforeAll
    static void setup() {
        player = new Player();
        player.setId(1);
        player.setName("John Doe");
        player.setTotalScore(10); // Total de partidas ganadas o puntaje del jugador.
    }

    @Test
    void testCreatePlayer() {
        Mockito.when(playerService.createPlayer(Mockito.any(Player.class)))
                .thenReturn(Mono.just(player));

        webTestClient.post()
                .uri("/player")
                .body(Mono.just(player), Player.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Player.class)
                .value(response -> {
                    assertNotNull(response);
                    assertEquals(player.getId(), response.getId());
                    assertEquals(player.getName(), response.getName());
                });
    }

    @Test
    void testGetPlayerById() {
        Mockito.when(playerService.getPlayerById(player.getId()))
                .thenReturn(Mono.just(player));

        webTestClient.get()
                .uri("/player/{id}", player.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Player.class)
                .value(response -> {
                    assertNotNull(response);
                    assertEquals(player.getId(), response.getId());
                    assertEquals(player.getName(), response.getName());
                });
    }

    @Test
    void testGetPlayerByIdNotFound() {
        int playerId = 999; // Un id inexistente.

        Mockito.when(playerService.getPlayerById(playerId))
                .thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/player/{id}", playerId)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testGetAllPlayers() {
        Mockito.when(playerService.getAllPlayers())
                .thenReturn(Flux.just(player, new Player(2, "Jane Doe", 5)));

        webTestClient.get()
                .uri("/player")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Player.class)
                .value(response -> {
                    assertNotNull(response);
                    assertEquals(2, response.size());
                    assertEquals(player.getId(), response.get(0).getId());
                    assertEquals("Jane Doe", response.get(1).getName());
                });
    }

    @Test
    void testDeletePlayerById() {
        Mockito.when(playerService.deletePlayerById(player.getId()))
                .thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/player/{id}", player.getId())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testDeletePlayerByIdNotFound() {
        int playerId = 999; // Un id inexistente.

        Mockito.when(playerService.deletePlayerById(playerId))
                .thenReturn(Mono.error(new RuntimeException("Player not found")));

        webTestClient.delete()
                .uri("/player/{id}", playerId)
                .exchange()
                .expectStatus().is5xxServerError(); // Si deseas capturar errores, puedes mejorar esto para un 404.
    }

    @Test
    void testUpdatePlayerById() {
        player.setId(1);
        player.setName("Original Name");
        player.setTotalScore(100);

        // Crear el jugador actualizado
        Player updatedPlayer = new Player();
        updatedPlayer.setId(player.getId());
        updatedPlayer.setName("Updated Name");
        updatedPlayer.setTotalScore(player.getTotalScore());

        // Mockear el servicio para que devuelva el jugador existente
        Mockito.when(playerService.getPlayerById(1))
                .thenReturn(Mono.just(player)); // Devuelve el jugador inicial cuando se consulta por ID

        // Mockear el servicio para que devuelva el jugador actualizado
        Mockito.when(playerService.updatePlayerById(Mockito.eq(1), Mockito.any(Player.class)))
                .thenReturn(Mono.just(updatedPlayer)); // Devuelve el jugador actualizado

        // Ejecuta el test
        webTestClient.put()
                .uri("/player/{id}", 1)
                .body(Mono.just(updatedPlayer), Player.class) // Cuerpo con los datos del jugador actualizado
                .exchange()
                .expectStatus().isOk() // Verifica que la respuesta es 200 OK
                .expectBody(Player.class) // Verifica el cuerpo de respuesta
                .value(response -> {
                    assertEquals(updatedPlayer.getId(), response.getId());
                    assertEquals("Updated Name", response.getName());
                });
    }


    @Test
    void testUpdatePlayerByIdNotFound() {
        int playerId = 999; // Un ID inexistente.
        Player updatedPlayer = new Player();
        updatedPlayer.setId(playerId);
        updatedPlayer.setName("Updated Name");

        Mockito.when(playerService.getPlayerById(playerId)).thenReturn(Mono.empty());
        Mockito.when(playerService.updatePlayerById(playerId, updatedPlayer)).thenReturn(Mono.empty());

        webTestClient.put()
                .uri("/player/{id}", playerId)
                .body(Mono.just(updatedPlayer), Player.class)
                .exchange()
                .expectStatus().isNotFound(); // Ahora debería responder con 404
    }

    @Test
    void testGetRanking() {
        Player player2 = new Player(2, "Jane Doe", 15);

        Mockito.when(playerService.getRanking())
                .thenReturn(Flux.just(player2, player));

        webTestClient.get()
                .uri("/player/ranking")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Player.class)
                .value(response -> {
                    assertNotNull(response);
                    assertEquals(2, response.size());
                    assertEquals("Jane Doe", response.get(0).getName()); // Jane tiene mayor puntuación
                    assertEquals("John Doe", response.get(1).getName());
                });
    }
}