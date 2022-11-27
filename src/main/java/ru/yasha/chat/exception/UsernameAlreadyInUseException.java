package ru.yasha.chat.exception;

public class UsernameAlreadyInUseException extends RuntimeException {

    public UsernameAlreadyInUseException() {
        super();
    }

    public UsernameAlreadyInUseException(String message) {
        super(message);
    }
}
