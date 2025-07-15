package cat.itacademy.s05.t01.n01.controllers;

import cat.itacademy.s05.t01.n01.model.Game;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import cat.itacademy.s05.t01.n01.services.GameService;

@RestController
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/new")
    public Mono<ResponseEntity<Game>> createGame(@RequestBody Game game) {
        return gameService.createGame(game)
                .map(savedGame -> new ResponseEntity<>(savedGame, HttpStatus.CREATED));
    }

    @PostMapping("/{id}/play")
    public Mono<ResponseEntity<Game>> playGame(@PathVariable String id, @RequestParam String action) {
        return gameService.playGame(id, action)
                .map(updatedGame -> new ResponseEntity<>(updatedGame, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @GetMapping("/player/{playerId}")
    public Flux<Game> getGamesByPlayerId(@PathVariable String playerId) {
        return gameService.getGamesByPlayerId(playerId);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Game>> getGameById(@PathVariable String id) {
        return gameService.getGameById(id)
                .map(game -> new ResponseEntity<>(game, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/{id}/finish")
    public Mono<ResponseEntity<Game>> finishGame(@PathVariable String id) {
        return gameService.finishGame(id)
                .map(updatedGame -> new ResponseEntity<>(updatedGame, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @DeleteMapping("/{id}/delete")
    public Mono<ResponseEntity<Void>> deleteGame(@PathVariable String id) {
        return gameService.getGameById(id) // Intentamos obtener el juego.
                .flatMap(game -> gameService.deleteGame(id) // Si existe, lo eliminamos.
                        .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT))))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND)); // Si no existe, devolvemos 404.
    }

}