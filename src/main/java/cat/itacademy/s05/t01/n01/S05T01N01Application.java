package cat.itacademy.s05.t01.n01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class S05T01N01Application {

	public static void main(String[] args) {
		SpringApplication.run(S05T01N01Application.class, args);
	}

}

/*POST http://localhost:8081/game/new
GET http://localhost:8081/game/{id}
POST http://localhost:8081/game/{id}/play
DELETE http://localhost:8081/game/{id}/delete
GET http://localhost:8081/ranking
PUT http://localhost:8081/player/{playerId}*/


/*
Endpoints para el juego:
Crear partida:

Método: POST

Descripción: Crear una nueva partida de Blackjack.

Endpoint: /game/new

Cuerpo de la solicitud (body): Nuevo nombre del jugador.

Parámetros de entrada: Ninguna

Respuesta exitosa: Código 201 Created con información sobre la partida creada.


Obtener detalles de partida:

Método: GET

Descripción: Obtiene los detalles de una partida específica de Blackjack.

		Endpoint: /game/{id}

Parámetros de entrada: Identificador único de la partida (id)

Respuesta exitosa: Código 200 OK con información detallada sobre la partida.


Realizar jugada:

Método: POST

Descripción: Realiza una jugada en una partida de Blackjack existente.

		Endpoint: /game/{id}/play

Parámetros de entrada: Identificador único de la partida (id), datos de la jugada (por ejemplo, tipos de jugada y cantidad apostada).

Respuesta exitosa: Código 200 OK con el resultado de la jugada y el estado actual de la partida.


Eliminar partida:

Método: DELETE

Descripción: Elimina una partida de Blackjack existente.

Endpoint: /game/{id}/delete

Parámetros de entrada: Identificador único de la partida (id).

Respuesta exitosa: Código 204 No Contento si la partida se elimina correctamente.


Obtener ranking de jugadores:

Método: GET

Descripción: Obtiene el ranking de los jugadores basado en su rendimiento en las partidas de Blackjack.

		Endpoint: /ranking

Parámetros de entrada: Ninguna

Respuesta exitosa: Código 200 OK con la lista de jugadores ordenada por su posición en el ranking y su puntaje.


Cambiar nombre del jugador:

Método: PUT

Descripción: Cambia el nombre de un jugador en una partida de Blackjack existente.

Endpoint: /player/{playerId}

Cuerpo de la solicitud (body): Nuevo nombre del jugador.

Parámetros de entrada: identificador único del jugador (playerId).

Respuesta exitosa: Código 200 OK con información actualizada del jugador.*/
