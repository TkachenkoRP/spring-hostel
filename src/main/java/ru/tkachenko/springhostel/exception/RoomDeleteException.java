package ru.tkachenko.springhostel.exception;

public class RoomDeleteException extends RuntimeException {
    public RoomDeleteException(String message) {
        super(message);
    }
}
