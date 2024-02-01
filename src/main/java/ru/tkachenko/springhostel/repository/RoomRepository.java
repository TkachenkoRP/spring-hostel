package ru.tkachenko.springhostel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.tkachenko.springhostel.model.ComfortType;
import ru.tkachenko.springhostel.model.Room;
import ru.tkachenko.springhostel.model.RoomType;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long>, JpaSpecificationExecutor<Room> {
    List<Room> findByTypeRoom(RoomType roomType);

    List<Room> findByComfortType(ComfortType comfortType);

}
