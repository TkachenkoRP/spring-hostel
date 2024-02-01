package ru.tkachenko.springhostel.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import ru.tkachenko.springhostel.exception.RoomDeleteException;
import ru.tkachenko.springhostel.model.Room;
import ru.tkachenko.springhostel.service.RoomService;

@Aspect
@Component
@RequiredArgsConstructor
public class RoomDeleteCheckAspect {

    private final RoomService roomService;

    @Before("@annotation(RoomDeleteCheck)")
    public void checkDeleteRoom(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof Long id) {
                Room room = roomService.findById(id);
                if (!room.getGuests().isEmpty()) {
                    throw new RoomDeleteException("Нельзя удалить комнату с гостями!");
                }
            }
        }
    }
}