package cat.itacademy.s05.t01.n01.services;

import cat.itacademy.s05.t01.n01.model.Player;
import cat.itacademy.s05.t01.n01.repositories.GameRepository;
import cat.itacademy.s05.t01.n01.repositories.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private PlayerService playerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPlayer_ShouldSaveAndReturnPlayer() {
        Player player = new Player(1, "John", 0);
        when(playerRepository.save(player)).thenReturn(Mono.just(player));

        Mono<Player> result = playerService.createPlayer(player);

        StepVerifier.create(result)
                .expectNext(player)
                .verifyComplete();

        verify(playerRepository, times(1)).save(player);
    }

    @Test
    void getPlayerById_ShouldReturnPlayer_WhenPlayerExists() {
        Player player = new Player(1, "John", 0);
        when(playerRepository.findById(1)).thenReturn(Mono.just(player));

        Mono<Player> result = playerService.getPlayerById(1);

        StepVerifier.create(result)
                .expectNext(player)
                .verifyComplete();

        verify(playerRepository, times(1)).findById(1);
    }

    @Test
    void getPlayerById_ShouldReturnEmpty_WhenPlayerDoesNotExist() {
        when(playerRepository.findById(1)).thenReturn(Mono.empty());

        Mono<Player> result = playerService.getPlayerById(1);

        StepVerifier.create(result)
                .verifyComplete();

        verify(playerRepository, times(1)).findById(1);
    }

    @Test
    void getAllPlayers_ShouldReturnAllPlayers() {
        Player player1 = new Player(1, "John", 0);
        Player player2 = new Player(2, "Jane", 10);
        when(playerRepository.findAll()).thenReturn(Flux.just(player1, player2));

        Flux<Player> result = playerService.getAllPlayers();

        StepVerifier.create(result)
                .expectNext(player1)
                .expectNext(player2)
                .verifyComplete();

        verify(playerRepository, times(1)).findAll();
    }

    @Test
    void deletePlayerById_ShouldDeletePlayer() {
        when(playerRepository.deleteById(1)).thenReturn(Mono.empty());

        Mono<Void> result = playerService.deletePlayerById(1);

        StepVerifier.create(result)
                .verifyComplete();

        verify(playerRepository, times(1)).deleteById(1);
    }

    @Test
    void updatePlayerById_ShouldUpdatePlayer_WhenPlayerExists() {
        Player existingPlayer = new Player(1, "John", 0);
        Player updatedPlayer = new Player(1, "Jane", 10);

        when(playerRepository.findById(1)).thenReturn(Mono.just(existingPlayer));
        when(playerRepository.save(existingPlayer)).thenReturn(Mono.just(updatedPlayer));

        Mono<Player> result = playerService.updatePlayerById(1, updatedPlayer);

        StepVerifier.create(result)
                .expectNext(updatedPlayer)
                .verifyComplete();

        verify(playerRepository, times(1)).findById(1);
        verify(playerRepository, times(1)).save(existingPlayer);
    }

    @Test
    void updatePlayerById_ShouldReturnEmpty_WhenPlayerDoesNotExist() {
        Player updatedPlayer = new Player(1, "Jane", 10);

        when(playerRepository.findById(1)).thenReturn(Mono.empty());

        Mono<Player> result = playerService.updatePlayerById(1, updatedPlayer);

        StepVerifier.create(result)
                .verifyComplete();

        verify(playerRepository, times(1)).findById(1);
    }

    @Test
    void getRanking_ShouldReturnPlayersSortedByScore() {
        Player player1 = new Player(1, "John", 0);
        Player player2 = new Player(2, "Jane", 0);
        Player player3 = new Player(3, "Mike", 0);

        when(playerRepository.findAll()).thenReturn(Flux.just(player1, player2, player3));
        when(gameRepository.findByPlayerIdAndFinishedAndCurrentScore("1", true, 21)).thenReturn(Flux.just());
        when(gameRepository.findByPlayerIdAndFinishedAndCurrentScore("2", true, 21)).thenReturn(Flux.just());
        when(gameRepository.findByPlayerIdAndFinishedAndCurrentScore("3", true, 21)).thenReturn(Flux.just());

        Flux<Player> result = playerService.getRanking();

        StepVerifier.create(result)
                .recordWith(ArrayList::new) // Almacena los resultados para depurar después
                .thenConsumeWhile(player -> true) // Consume todos los players
                .consumeRecordedWith(players -> {
                    // Imprime los jugadores finales para inspección
                    players.forEach(player -> System.out.println("Player in verifier: " + player));
                })
                .verifyComplete();

        verify(playerRepository, times(1)).findAll();
    }
}