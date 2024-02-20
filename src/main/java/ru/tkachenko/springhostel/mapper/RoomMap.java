package ru.tkachenko.springhostel.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tkachenko.springhostel.model.Room;
import ru.tkachenko.springhostel.service.RoomService;

@Component
@RequiredArgsConstructor
public class RoomMap {
    private final RoomService roomService;

    public Room fromId(Long id) {
        return id != null ? roomService.findById(id) : null;
    }
}
