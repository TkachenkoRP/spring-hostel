package ru.tkachenko.springhostel.mapper;

import ru.tkachenko.springhostel.dto.RoomForListResponse;
import ru.tkachenko.springhostel.model.Room;

public abstract class RoomMapperDelegate implements RoomMapper {
    @Override
    public RoomForListResponse entityToResponseForList(Room room) {
        RoomForListResponse response = new RoomForListResponse();
        response.setId(room.getId());
        response.setFloor(room.getFloor());
        response.setRoomNumber(room.getRoomNumber());
        response.setTypeRoom(room.getTypeRoom());
        response.setComfortType(room.getComfortType());
        response.setCapacity(room.getCapacity());
        response.setCreateAt(room.getCreateAt());
        response.setUpdateAt(room.getUpdateAt());
        response.setGuestsCount(room.getGuests().size());
        return response;
    }
}
