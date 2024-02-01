package ru.tkachenko.springhostel.exception;

public class RoomAllocationException extends RuntimeException {
    public RoomAllocationException(String message) {
        super(message);
    }
}
