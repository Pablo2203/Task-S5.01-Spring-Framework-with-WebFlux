package cat.itacademy.s05.t01.n01.model;


import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection ="games")
public class Game {
    @Id
    private String id;
    private String playerId;
    private List<String> cards;
    private int currentScore;
    private boolean finished;

}
