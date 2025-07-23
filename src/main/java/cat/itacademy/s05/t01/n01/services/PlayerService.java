package cat.itacademy.s05.t01.n01.services;

import cat.itacademy.s05.t01.n01.model.Player;
import cat.itacademy.s05.t01.n01.repositories.mongo.GameRepository;
import cat.itacademy.s05.t01.n01.repositories.r2dbc.PlayerRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;


@Service
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final GameRepository gameRepository;

    public PlayerService(PlayerRepository playerRepository, GameRepository gameRepository) {
        this.playerRepository = playerRepository;
        this.gameRepository = gameRepository;

    }

        public Mono<Player> createPlayer(Player player) {
            return playerRepository.save(player);
        }

    public Mono<Player> getPlayerById(int id) {
        return playerRepository.findById(id)
                .switchIfEmpty(Mono.empty()); // Nunca devuelve null
    }

        public Flux<Player> getAllPlayers() {
            return playerRepository.findAll();
        }


        public Mono<Void> deletePlayerById(int id) {
            return playerRepository.deleteById(id);
        }

        public Mono<Player> updatePlayerById(int id, Player player) {
        return playerRepository.findById(id)  // Verifica si el jugador existe
                .flatMap(existingPlayer -> { // Si el jugador existe, actualiza
                    existingPlayer.setName(player.getName());
                    existingPlayer.setTotalScore(player.getTotalScore());
                    return playerRepository.save(existingPlayer); // Guarda y devuelve
                });
    }

    public Flux<Player> getRanking() {
        return playerRepository.findAll()
                .flatMap(player ->
                        gameRepository.findByPlayerIdAndFinishedAndCurrentScore(
                                        String.valueOf(player.getId()),
                                        true,
                                        21
                                )
                                .count() // Contar el número de partidas ganadas
                                .map(totalWins -> {
                                    player.setTotalScore(totalWins); // Actualizar totalScore del jugador
                                    return player;
                                })
                )
                .doOnNext(player -> System.out.println("Player after score computation: " + player))
                .sort(Comparator.comparingLong(Player::getTotalScore).reversed()) // Clasificar por totalScore
                .log("Ranking Flux");
    }
   /* public Flux<Player> getRanking() {
        return playerRepository.findAll()
                .flatMap(player -> {
                    return gameRepository.findByPlayerId(String.valueOf(player.getId()))
                            .filter(Game::isFinished)
                            .collectList()
                            .map(games -> {
                                long wins = games.stream().filter(game -> game.getCurrentScore() == 21).count();
                                player.setTotalScore(wins); // Añade un campo score al modelo Player si no está
                                return player;
                            });
                })
                .sort(Comparator.comparingLong(Player::getTotalScore).reversed()); // Ordenar por puntaje
    }*/
}
