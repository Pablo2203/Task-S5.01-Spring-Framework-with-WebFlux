package cat.itacademy.s05.t01.n01.services;
import cat.itacademy.s05.t01.n01.model.Player;
import cat.itacademy.s05.t01.n01.repositories.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerService playerService;

    @Test
    void testCreatePlayer() {
        // Configurar datos de prueba
        Player player = new Player(1, "John Doe");
        Mockito.when(playerRepository.save(player)).thenReturn(Mono.just(player));

        // Ejecutar el método a probar
        Mono<Player> result = playerService.createPlayer(player);

        // Verificar el resultado usando StepVerifier
        StepVerifier.create(result)
                .expectNext(player)
                .verifyComplete();

        // Verificar si se llamó al repositorio
        Mockito.verify(playerRepository, Mockito.times(1)).save(player);
    }
    @Test
    void testGetPlayerById_whenPlayerExists() {
        // Configurar datos de prueba
        Player player = new Player(1, "John Doe");
        Mockito.when(playerRepository.findById(1)).thenReturn(Mono.just(player));

        // Ejecutar el método a probar
        Mono<Player> result = playerService.getPlayerById(1);

        // Verificar el resultado
        StepVerifier.create(result)
                .expectNext(player)
                .verifyComplete();
    }

    @Test
    void testGetPlayerById_whenPlayerNotFound() {
        // Simular que no se encuentra un jugador
        Mockito.when(playerRepository.findById(99)).thenReturn(Mono.empty());

        // Ejecutar el método a probar
        Mono<Player> result = playerService.getPlayerById(99);

        // Verificar que el Mono esté vacío
        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
    }
    @Test
    void testGetAllPlayers() {
        // Configurar datos de prueba
        Player player1 = new Player(1, "Alice");
        Player player2 = new Player(2, "Bob");
        Mockito.when(playerRepository.findAll()).thenReturn(Flux.just(player1, player2));

        // Ejecutar el método a probar
        Flux<Player> result = playerService.getAllPlayers();

        // Verificar el contenido del Flux
        StepVerifier.create(result)
                .expectNext(player1)
                .expectNext(player2)
                .verifyComplete();

        Mockito.verify(playerRepository, Mockito.times(1)).findAll();
    }
    @Test
    void testDeletePlayerById() {
        Mockito.when(playerRepository.deleteById(1)).thenReturn(Mono.empty());

        // Ejecutar el método a probar
        Mono<Void> result = playerService.deletePlayerById(1);

        // Verificar que el Mono termine correctamente
        StepVerifier.create(result)
                .verifyComplete();

        Mockito.verify(playerRepository, Mockito.times(1)).deleteById(1);
    }
    @Test
    void testUpdatePlayerById_whenPlayerExists() {
        // Configurar datos
        Player existingPlayer = new Player(1, "Old Name");
        Player updatedPlayer = new Player(1, "New Name");

        Mockito.when(playerRepository.findById(1)).thenReturn(Mono.just(existingPlayer));
        Mockito.when(playerRepository.save(existingPlayer)).thenReturn(Mono.just(existingPlayer));

        // Ejecutar el método a probar
        Mono<Player> result = playerService.updatePlayerById(1, updatedPlayer);

        // Verificar los resultados
        StepVerifier.create(result)
                .expectNextMatches(player -> player.getName().equals("New Name"))
                .verifyComplete();

        Mockito.verify(playerRepository, Mockito.times(1)).findById(1);
        Mockito.verify(playerRepository, Mockito.times(1)).save(existingPlayer);
    }
}
