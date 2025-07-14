package cat.itacademy.s05.t01.n01.controllers;

import cat.itacademy.s05.t01.n01.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import cat.itacademy.s05.t01.n01.services.PlayerService;

@RestController
@RequestMapping("/player")
public class PlayerController {

    @Autowired
    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping
    public Mono<ResponseEntity<Player>> createPlayer(@RequestBody Player player) {
        return playerService.createPlayer(player)
                .map(savedPlayer -> new ResponseEntity<>(savedPlayer, HttpStatus.CREATED));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Player>> getPlayerById(@PathVariable int id) {
        return playerService.getPlayerById(id)
                .map(player -> new ResponseEntity<>(player, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public Flux<Player> getAllPlayers() {
        return playerService.getAllPlayers();
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deletePlayerById(@PathVariable int id) {
        return playerService.deletePlayerById(id)
                .then(Mono.just(new ResponseEntity<>(HttpStatus.NO_CONTENT)));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Player>> updatePlayerById(@PathVariable int id, @RequestBody Player player) {
        return playerService.getPlayerById(id)
                .flatMap(currentPlayer -> {
                    currentPlayer.setName(player.getName());
                    return playerService.createPlayer(currentPlayer);
                })
                .map(updatedPlayer -> new ResponseEntity<>(updatedPlayer, HttpStatus.OK));
    }

}
/*Cambiar nombre del jugador:

Método: PUT

Descripción: Cambia el nombre de un jugador en una partida de Blackjack existente.

Endpoint: /player/{playerId}

Cuerpo de la solicitud (body): Nuevo nombre del jugador.

Parámetros de entrada: identificador único del jugador (playerId).

Respuesta exitosa: Código 200 OK con información actualizada del jugador.*/

