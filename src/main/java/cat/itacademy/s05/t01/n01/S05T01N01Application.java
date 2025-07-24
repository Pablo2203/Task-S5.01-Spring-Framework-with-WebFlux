package cat.itacademy.s05.t01.n01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class S05T01N01Application {

	public static void main(String[] args) {
		SpringApplication.run(S05T01N01Application.class, args);
	}

}
//https://blackjack-app-oy23.onrender.com
//http://localhost:8081/swagger-ui.html


/*POST http://localhost:8081/game/new
GET http://localhost:8081/game/{id}
POST http://localhost:8081/game/{id}/play
DELETE http://localhost:8081/game/{id}/delete
GET http://localhost:8081/ranking
PUT http://localhost:8081/player/{playerId}*/

