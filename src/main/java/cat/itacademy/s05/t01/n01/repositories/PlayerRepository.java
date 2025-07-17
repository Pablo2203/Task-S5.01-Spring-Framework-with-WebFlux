package cat.itacademy.s05.t01.n01.repositories;

import cat.itacademy.s05.t01.n01.model.Player;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface PlayerRepository extends ReactiveCrudRepository<Player, Integer> {
    Mono<Player> findByName(String name);
    Mono<Player> findById(int id);
}
