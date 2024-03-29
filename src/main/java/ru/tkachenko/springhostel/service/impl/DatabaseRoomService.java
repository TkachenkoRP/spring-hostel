package ru.tkachenko.springhostel.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tkachenko.springhostel.dto.RoomFilter;
import ru.tkachenko.springhostel.exception.EntityNotFoundException;
import ru.tkachenko.springhostel.exception.RoomDeleteException;
import ru.tkachenko.springhostel.exception.RoomEditException;
import ru.tkachenko.springhostel.mapper.RoomMapper;
import ru.tkachenko.springhostel.model.Room;
import ru.tkachenko.springhostel.repository.RoomRepository;
import ru.tkachenko.springhostel.repository.RoomSpecification;
import ru.tkachenko.springhostel.service.RoomService;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DatabaseRoomService implements RoomService {

    private final RoomRepository repository;
    private final RoomMapper mapper;

    @Override
    public List<Room> findAll(RoomFilter filter) {
        return repository.findAll(RoomSpecification.withFilter(filter));
    }

    @Override
    public Room findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format(
                        "Комната с ID {0} не найдена!", id
                )));
    }

    @Override
    public Room save(Room room) {
        return repository.save(room);
    }

    @Override
    public Room update(Room room) {
        Room existedRoom = findById(room.getId());
        if (!existedRoom.getGuests().isEmpty() && room.getTypeRoom() != existedRoom.getTypeRoom()) {
            throw new RoomEditException("Нельзя изменить тип комнаты с гостями!");
        }
        mapper.updateRoom(room, existedRoom);
        return repository.save(existedRoom);
    }

    @Override
    public void delete(Long id) {
        Room room = findById(id);
        if (!room.getGuests().isEmpty()) {
            throw new RoomDeleteException("Нельзя удалить комнату с гостями!");
        }
        repository.deleteById(id);
    }
}
