package cat.itacademy.s05.t01.n01.services;

import cat.itacademy.s05.t01.n01.model.Player;
import cat.itacademy.s05.t01.n01.repositories.PlayerRepository;
import org.springframework.stereotype.Service;
import cat.itacademy.s05.t01.n01.repositories.PlayerRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class PlayerService {
    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
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
}
