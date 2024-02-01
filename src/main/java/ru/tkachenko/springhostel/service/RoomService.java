package ru.tkachenko.springhostel.service;

import ru.tkachenko.springhostel.dto.RoomFilter;
import ru.tkachenko.springhostel.model.Room;

import java.util.List;

public interface RoomService {

    List<Room> findAll(RoomFilter filter);

    Room findById(Long id);

    Room save(Room room);

    Room update(Room room);

    void delete(Long id);
}
