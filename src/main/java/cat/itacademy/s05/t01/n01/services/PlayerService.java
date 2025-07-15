package cat.itacademy.s05.t01.n01.services;

import cat.itacademy.s05.t01.n01.model.Game;
import cat.itacademy.s05.t01.n01.model.Player;
import cat.itacademy.s05.t01.n01.repositories.GameRepository;
import cat.itacademy.s05.t01.n01.repositories.PlayerRepository;
import org.springframework.stereotype.Service;
import cat.itacademy.s05.t01.n01.repositories.GameRepository;
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
            return playerRepository.findById(id);
        }

        public Flux<Player> getAllPlayers() {
            return playerRepository.findAll();
        }


        public Mono<Void> deletePlayerById(int id) {
            return playerRepository.deleteById(id);
        }
        public Mono<Player> updatePlayerById(int id, Player player) {
            return playerRepository.findById(id)
                    .flatMap(currentPlayer -> {
                        currentPlayer.setName(player.getName());
                        return playerRepository.save(currentPlayer);
                    });
        }
    public Flux<Player> getRanking() {
        return playerRepository.findAll()
                .flatMap(player ->
                        gameRepository.findByPlayerIdAndFinishedAndCurrentScore(
                                        String.valueOf(player.getId()),
                                        true,
                                        21
                                ) // Obtener solo partidas ganadas
                                .count() // Contar el número de partidas ganadas directamente en la operación reactiva
                                .map(totalWins -> {
                                    player.setTotalScore(totalWins); // Actualizar totalScore del jugador
                                    return player;
                                })
                )
                .sort(Comparator.comparingLong(Player::getTotalScore).reversed()); // Ordenar por totalScore
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
/*

*/
