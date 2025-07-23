/*
package cat.itacademy.s05.t01.n01.configurationsDB;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

@Configuration
@EnableR2dbcRepositories(
        basePackages = "cat.itacademy.s05.t01.n01.repositories.r2dbc"
)
public class R2dbcConfiguration {

    @Bean
    public ConnectionFactory connectionFactory() {
        return ConnectionFactories.get(
                ConnectionFactoryOptions.builder()
                        .option(DRIVER, "mysql")
                        .option(HOST, System.getenv("MYSQL_ADDON_HOST")) // Host desde variables de entorno
                        .option(PORT, Integer.parseInt(System.getenv("MYSQL_ADDON_PORT"))) // Puerto
                        .option(USER, System.getenv("MYSQL_ADDON_USER")) // Usuario
                        .option(PASSWORD, System.getenv("MYSQL_ADDON_PASSWORD")) // Contraseña
                        .option(DATABASE, System.getenv("MYSQL_ADDON_DB")) // Base de datos
                        .option(SSL, true) // Conexión segura
                        .build()
        );
    }
}*/
