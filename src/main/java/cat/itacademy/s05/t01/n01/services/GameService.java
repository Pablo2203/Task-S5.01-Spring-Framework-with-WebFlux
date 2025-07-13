package cat.itacademy.s05.t01.n01.services;

import cat.itacademy.s05.t01.n01.model.Game;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import cat.itacademy.s05.t01.n01.repositories.GameRepository;

@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Mono<Game> createGame(Game game) {
        return gameRepository.save(game);
    }

    public Flux<Game> getGamesByPlayerId(String playerId) {
        return gameRepository.findByPlayerId(playerId);
    }

    public Mono<Game> getGameById(String id) {
        return gameRepository.findById(id);
    }

    public Mono<Game> finishGame(String id) {
        return gameRepository.findById(id)
                .flatMap(game -> {
                    game.setFinished(true);
                    return gameRepository.save(game);
                });
    }
}