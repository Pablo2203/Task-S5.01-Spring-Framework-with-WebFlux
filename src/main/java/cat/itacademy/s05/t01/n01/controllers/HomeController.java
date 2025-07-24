package cat.itacademy.s05.t01.n01.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Bienvenido a la aplicación Blackjack. Visita los endpoints disponibles para gestionar jugadores y partidas.";
    }
}