package cat.itacademy.s05.t01.n01.configurationsDB;

import com.mongodb.reactivestreams.client.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;


@Configuration
@EnableReactiveMongoRepositories(
        basePackages = "cat.itacademy.s05.t01.n01.repositories.mongo"
)
public class MongoConfiguration {

    @Value("${spring.data.mongodb.database}") // Leemos el nombre de la base de datos de las propiedades
    private String databaseName;

    @Bean("reactiveMongoTemplate")
    public ReactiveMongoTemplate reactiveMongoTemplate(MongoClient mongoClient) {
        return new ReactiveMongoTemplate(mongoClient, databaseName);
    }
}