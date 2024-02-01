package ru.tkachenko.springhostel.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import ru.tkachenko.springhostel.exception.RoomAllocationException;
import ru.tkachenko.springhostel.model.GenderType;
import ru.tkachenko.springhostel.model.Guest;
import ru.tkachenko.springhostel.model.Room;
import ru.tkachenko.springhostel.model.RoomType;
import ru.tkachenko.springhostel.service.RoomService;

@Aspect
@Component
@RequiredArgsConstructor
public class RoomAllocationCheckAspect {
    private final RoomService roomService;

    @Before("@annotation(RoomAllocationCheck)")
    public void checkRoomAllocation(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof Guest guest) {

                Room room = roomService.findById(guest.getRoom().getId());

                if (room.getTypeRoom() != null && guest.getGenderType() != null) {
                    if ((room.getTypeRoom() == RoomType.FEMALE && guest.getGenderType() == GenderType.MALE) ||
                            (room.getTypeRoom() == RoomType.MALE && guest.getGenderType() == GenderType.FEMALE)) {
                        throw new RoomAllocationException("Несоответствие типа комнаты и пола гостя!");
                    }
                }

                if (room.getGuests().stream().noneMatch(g -> g.getId().equals(guest.getId()))) {
                    if (room.getCapacity() <= room.getGuests().size()) {
                        throw new RoomAllocationException("Комната заполнена, нет свободных мест!");
                    }
                }
            }
        }
    }
}
