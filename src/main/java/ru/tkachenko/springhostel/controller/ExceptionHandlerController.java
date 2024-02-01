package ru.tkachenko.springhostel.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.tkachenko.springhostel.dto.ErrorResponse;
import ru.tkachenko.springhostel.exception.EntityNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import ru.tkachenko.springhostel.exception.RoomAllocationException;
import ru.tkachenko.springhostel.exception.RoomDeleteException;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerController {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> notFound(EntityNotFoundException e) {
        log.error("Ошибка при попытке получить сущность", e);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(e.getLocalizedMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> notValid(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<String> errorMessages = bindingResult.getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        String errorMessage = String.join(";", errorMessages);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(errorMessage));
    }

    @ExceptionHandler(RoomAllocationException.class)
    public ResponseEntity<ErrorResponse> notAddedGuest(RoomAllocationException e) {
        log.error("Ошибка при попытке доабавления гостя", e);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getLocalizedMessage()));
    }

    @ExceptionHandler(RoomDeleteException.class)
    public ResponseEntity<ErrorResponse> dontDelete(RoomDeleteException e) {
        log.error("Ошибка удаления комнаты", e);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getMessage()));
    }
}
