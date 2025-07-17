package cat.itacademy.s05.t01.n01.services;

import cat.itacademy.s05.t01.n01.exceptions.InvalidGameStateException;
import cat.itacademy.s05.t01.n01.model.Game;
import cat.itacademy.s05.t01.n01.repositories.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameService gameService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createGame_ShouldInitializeGameAndSave() {
        Game newGame = new Game();
        newGame.setPlayerId("player1");

        Game savedGame = new Game("1", "player1", 0, List.of(), false);

        when(gameRepository.save(any(Game.class))).thenReturn(Mono.just(savedGame));

        Mono<Game> result = gameService.createGame(newGame);

        StepVerifier.create(result)
                .expectNext(savedGame)
                .verifyComplete();

        verify(gameRepository, times(1)).save(any(Game.class));
    }

    @Test
    void getGamesByPlayerId_ShouldReturnGames() {
        Game game1 = new Game("1", "player1", 10, List.of("A", "5"), false);
        Game game2 = new Game("2", "player1", 21, List.of("K", "A"), true);

        when(gameRepository.findByPlayerId("player1")).thenReturn(Flux.just(game1, game2));

        Flux<Game> result = gameService.getGamesByPlayerId("player1");

        StepVerifier.create(result)
                .expectNext(game1, game2)
                .verifyComplete();

        verify(gameRepository, times(1)).findByPlayerId("player1");
    }

    @Test
    void getGameById_ShouldReturnGame() {
        Game game = new Game("1", "player1", 15, List.of("8", "7"), false);

        when(gameRepository.findById("1")).thenReturn(Mono.just(game));

        Mono<Game> result = gameService.getGameById("1");

        StepVerifier.create(result)
                .expectNext(game)
                .verifyComplete();

        verify(gameRepository, times(1)).findById("1");
    }

    @Test
    void finishGame_ShouldMarkGameAsFinished() {
        Game game = new Game("1", "player1", 15, List.of("8", "7"), false);
        Game finishedGame = new Game("1", "player1", 15, List.of("8", "7"), true);

        when(gameRepository.findById("1")).thenReturn(Mono.just(game));
        when(gameRepository.save(any(Game.class))).thenReturn(Mono.just(finishedGame));

        Mono<Game> result = gameService.finishGame("1");

        StepVerifier.create(result)
                .expectNext(finishedGame)
                .verifyComplete();

        verify(gameRepository, times(1)).findById("1");
        verify(gameRepository, times(1)).save(any(Game.class));
    }

    @Test
    void deleteGame_ShouldDeleteGame() {
        Game game = new Game("1", "player1", 15, List.of("8", "7"), false);

        when(gameRepository.findById("1")).thenReturn(Mono.just(game));
        when(gameRepository.deleteById("1")).thenReturn(Mono.empty());

        Mono<Void> result = gameService.deleteGame("1");

        StepVerifier.create(result)
                .verifyComplete();

        verify(gameRepository, times(1)).findById("1");
        verify(gameRepository, times(1)).deleteById("1");
    }

    @Test
    void playGame_ShouldUpdateGameState_WhenActionIsHit() {
        Game game = new Game("1", "player1", 15, List.of("8", "7"), false);
        Game updatedGame = new Game("1", "player1", 18, List.of("8", "7", "3"), false);

        when(gameRepository.findById("1")).thenReturn(Mono.just(game));
        when(gameRepository.save(any(Game.class))).thenReturn(Mono.just(updatedGame));

        Mono<Game> result = gameService.playGame("1", "HIT");

        StepVerifier.create(result)
                .expectNext(updatedGame)
                .verifyComplete();

        verify(gameRepository, times(1)).findById("1");
        verify(gameRepository, times(1)).save(any(Game.class));
    }

    @Test
    void playGame_ShouldFinishGame_WhenActionIsStand() {
        Game game = new Game("1", "player1", 15, List.of("8", "7"), false);
        Game finishedGame = new Game("1", "player1", 15, List.of("8", "7"), true);

        when(gameRepository.findById("1")).thenReturn(Mono.just(game));
        when(gameRepository.save(any(Game.class))).thenReturn(Mono.just(finishedGame));

        Mono<Game> result = gameService.playGame("1", "STAND");

        StepVerifier.create(result)
                .expectNext(finishedGame)
                .verifyComplete();

        verify(gameRepository, times(1)).findById("1");
        verify(gameRepository, times(1)).save(any(Game.class));
    }

    @Test
    void playGame_ShouldThrowError_WhenGameIsAlreadyFinished() {
        Game game = new Game("1", "player1", 21, List.of("A", "Q"), true);

        when(gameRepository.findById("1")).thenReturn(Mono.just(game));

        Mono<Game> result = gameService.playGame("1", "HIT");

        StepVerifier.create(result)
                .expectError(InvalidGameStateException.class)
                .verify();

        verify(gameRepository, times(1)).findById("1");
        verify(gameRepository, times(0)).save(any(Game.class));
    }
}