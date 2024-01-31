package ru.tkachenko.springhostel.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tkachenko.springhostel.model.Room;
import ru.tkachenko.springhostel.repository.RoomRepository;
import ru.tkachenko.springhostel.service.RoomService;
import ru.tkachenko.springhostel.utils.BeanUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DatabaseRoomService implements RoomService {

    private final RoomRepository repository;

    @Override
    public List<Room> findAll() {
        return repository.findAll();
    }

    @Override
    public Room findById(Long id) {
        return repository.findById(id)
                .orElse(null);
    }

    @Override
    public Room save(Room room) {
        return repository.save(room);
    }

    @Override
    public Room update(Room room) {
        Room existedRoom = findById(room.getId());
        BeanUtils.copyNonNullProperties(room, existedRoom);
        return repository.save(existedRoom);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
