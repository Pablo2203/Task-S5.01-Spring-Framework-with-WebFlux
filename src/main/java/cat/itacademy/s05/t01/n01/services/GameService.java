package cat.itacademy.s05.t01.n01.services;

import cat.itacademy.s05.t01.n01.exceptions.InvalidGameStateException;
import cat.itacademy.s05.t01.n01.model.Game;
import cat.itacademy.s05.t01.n01.model.Player;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import cat.itacademy.s05.t01.n01.repositories.GameRepository;

import java.util.Comparator;
import java.util.List;

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
    public Mono<Void> deleteGame(String id) {
        return gameRepository.findById(id)
                .flatMap(game -> gameRepository.deleteById(game.getId()));
    }

    public Mono<Game> playGame(String id, String action) {
        return gameRepository.findById(id)
                .flatMap(game -> {
                    if (game.isFinished()) {
                        return Mono.error(new InvalidGameStateException("La partida ya ha terminado"));
                    }

                    // Acción del jugador: "HIT" (Pedir carta) o "STAND" (Plantarse)
                    if ("HIT".equalsIgnoreCase(action)) {
                        String newCard = drawCard(); // Método para obtener una nueva carta aleatoria
                        game.getCards().add(newCard);
                        game.setCurrentScore(calculateScore(game.getCards())); // Método para recalcular el puntaje

                        // Revisar si el juego termina (Blackjack o se pasa del puntaje permitido)
                        if (game.getCurrentScore() >= 21) {
                            game.setFinished(true);
                        }
                    } else if ("STAND".equalsIgnoreCase(action)) {
                        game.setFinished(true); // El jugador se planta, partida finaliza.
                    } else {
                        return Mono.error(new IllegalArgumentException("Acción no válida"));
                    }

                    return gameRepository.save(game);
                });
    }
    private String drawCard() {
        // Simulación de cartas: valores del 1 al 11 (simplificado).
        String[] cards = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        return cards[(int) (Math.random() * cards.length)];
    }

    private int calculateScore(List<String> cards) {
        // Calcular el puntaje total con reglas simplificadas.
        int score = 0;
        int aces = 0;

        for (String card : cards) {
            if ("JQK".contains(card)) {
                score += 10;
            } else if ("A".equals(card)) {
                aces++;
                score += 11;
            } else {
                score += Integer.parseInt(card);
            }
        }
        // Ajustar el valor del As si el puntaje supera 21
        while (score > 21 && aces > 0) {
            score -= 10;
            aces--;
        }

        return score;
    }

}