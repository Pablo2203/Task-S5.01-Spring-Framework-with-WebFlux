package cat.itacademy.s05.t01.n01.exceptions;

public class InvalidGameStateException extends RuntimeException {
    public InvalidGameStateException(String message) {
        super(message);
    }
}
