package cat.itacademy.s05.t01.n01.repositories;
import cat.itacademy.s05.t01.n01.model.Game;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface GameRepository extends ReactiveMongoRepository<Game, String> {
    Flux<Game> findByPlayerId(String playerId);

    Flux<Game> findByPlayerIdAndFinishedAndCurrentScore(String playerId, boolean finished, long currentScore);
}

