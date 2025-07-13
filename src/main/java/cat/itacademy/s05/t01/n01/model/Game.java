package cat.itacademy.s05.t01.n01.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection ="games")
public class Game {
    @Id
    private String id;
    private String playerId;
    private List<String> cards;
    private int currentScore;
    private boolean finished;

    public String getId() {
        return id;
    }

    public String getPlayerId() {
        return playerId;
    }

    public List<String> getCards() {
        return cards;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public void setCards(List<String> cards) {
        this.cards = cards;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}
