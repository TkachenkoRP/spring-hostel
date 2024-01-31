package ru.tkachenko.springhostel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tkachenko.springhostel.model.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
