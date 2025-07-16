package cat.itacademy.s05.t01.n01.controllers;

import cat.itacademy.s05.t01.n01.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import cat.itacademy.s05.t01.n01.services.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/player")
@Tag(name = "PlayerController", description = "Controller for managing players in Blackjack games")
public class PlayerController {

    @Autowired
    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping
    @Operation(summary = "Create a new player",
            description = "Create a new player for Blackjack games.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Player successfully created"),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            })
    public Mono<ResponseEntity<Player>> createPlayer(@RequestBody Player player) {
        return playerService.createPlayer(player)
                .map(savedPlayer -> new ResponseEntity<>(savedPlayer, HttpStatus.CREATED));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get player by ID",
            description = "Retrieve a specific player's details by their ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Player found successfully"),
                    @ApiResponse(responseCode = "404", description = "Player not found")
            })
    public Mono<ResponseEntity<Player>> getPlayerById(@PathVariable int id) {
        return playerService.getPlayerById(id)
                .map(player -> new ResponseEntity<>(player, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    @Operation(summary = "Get all players",
            description = "Retrieve all players participating in Blackjack games.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Players retrieved successfully")
            })
    public Flux<Player> getAllPlayers() {
        return playerService.getAllPlayers();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a player by ID",
            description = "Delete a player by their ID from the system.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Player successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "Player not found")
            })
    public Mono<ResponseEntity<Void>> deletePlayerById(@PathVariable int id) {
        return playerService.deletePlayerById(id)
                .then(Mono.just(new ResponseEntity<>(HttpStatus.NO_CONTENT)));
    }


    @PutMapping("/{id}")
    @Operation(summary = "Update a player's name",
            description = "Update the name of an existing player in the system by their ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Player updated successfully"),
                    @ApiResponse(responseCode = "404", description = "Player not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            })
    public Mono<ResponseEntity<Player>> updatePlayerById(@PathVariable int id, @RequestBody Player player) {
        return playerService.getPlayerById(id)
                .flatMap(currentPlayer -> {
                    currentPlayer.setName(player.getName());
                    return playerService.createPlayer(currentPlayer);
                })
                .map(updatedPlayer -> new ResponseEntity<>(updatedPlayer, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/ranking")
    @Operation(summary = "Get player rankings",
            description = "Retrieve player rankings based on their game performance in Blackjack.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Player rankings retrieved successfully")
            })
    public Flux<Player> getRanking() {
        return playerService.getRanking();
    }
}