package cat.itacademy.s05.t01.n01.controllers;

import cat.itacademy.s05.t01.n01.model.Game;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import cat.itacademy.s05.t01.n01.services.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/game")
@Tag(name = "GameController", description = "Controller for Blackjack games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/new")
    @Operation(summary = "Create a new game",
            description = "Create a new game for a specific player",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Game successfully created"),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            })
    public Mono<ResponseEntity<Game>> createGame(@RequestBody Game game) {
        return gameService.createGame(game)
                .map(savedGame -> new ResponseEntity<>(savedGame, HttpStatus.CREATED));
    }

    @PostMapping("/{id}/play")
    @Operation(summary = "Play a game",
            description = "Play an existing game by providing the game ID and the player's action.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Game successfully played"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "404", description = "Game not found")
            })
    public Mono<ResponseEntity<Game>> playGame(@PathVariable String id, @RequestParam String action) {
        return gameService.playGame(id, action)
                .map(updatedGame -> new ResponseEntity<>(updatedGame, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/player/{playerId}")
    @Operation(summary = "Retrieve games by player ID",
            description = "Retrieve all games associated with a specific player's ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Games retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "No games found for the given player ID")
            })
    public Flux<Game> getGamesByPlayerId(@PathVariable String playerId) {
        return gameService.getGamesByPlayerId(playerId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a game by ID",
            description = "Retrieve a specific game by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Game retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "Game not found")
            })
    public Mono<ResponseEntity<Game>> getGameById(@PathVariable String id) {
        return gameService.getGameById(id)
                .map(game -> new ResponseEntity<>(game, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/{id}/finish")
    @Operation(summary = "Finish a game",
            description = "Mark a game as finished by providing its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Game finished successfully"),
                    @ApiResponse(responseCode = "404", description = "Game not found")
            })
    public Mono<ResponseEntity<Game>> finishGame(@PathVariable String id) {
        return gameService.finishGame(id)
                .map(updatedGame -> new ResponseEntity<>(updatedGame, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}/delete")
    @Operation(summary = "Delete a game",
            description = "Delete a game by its ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Game deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Game not found")
            })
    public Mono<ResponseEntity<Void>> deleteGame(@PathVariable String id) {
        return gameService.getGameById(id)
                .flatMap(game -> gameService.deleteGame(id)
                        .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT))))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}