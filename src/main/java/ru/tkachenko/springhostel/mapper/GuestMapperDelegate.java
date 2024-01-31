package ru.tkachenko.springhostel.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import ru.tkachenko.springhostel.dto.GuestForListResponse;
import ru.tkachenko.springhostel.dto.UpsertGuestRequest;
import ru.tkachenko.springhostel.model.Guest;
import ru.tkachenko.springhostel.service.RoomService;

public abstract class GuestMapperDelegate implements GuestMapper {
    @Autowired
    private RoomService roomService;

    @Override
    public Guest requestToEntity(UpsertGuestRequest request) {
        Guest entity = new Guest();
        entity.setLastName(request.getLastName());
        entity.setFirstName(request.getFirstName());
        entity.setMiddleName(request.getMiddleName());
        entity.setGender(request.getGender());
        entity.setRoom(roomService.findById(request.getRoomId()));
        return entity;
    }

    @Override
    public Guest requestToEntity(Long id, UpsertGuestRequest request) {
        Guest entity = requestToEntity(request);
        entity.setId(id);
        return entity;
    }

    @Override
    public GuestForListResponse entityToResponseList(Guest guest) {
        GuestForListResponse response = new GuestForListResponse();
        response.setId(guest.getId());
        response.setLastName(guest.getLastName());
        response.setFirstName(guest.getFirstName());
        response.setMiddleName(guest.getMiddleName());
        response.setGender(guest.getGender());
        response.setCreateAt(guest.getCreateAt());
        response.setUpdateAt(guest.getUpdateAt());
        response.setRoomId(guest.getRoom().getId());
        return response;
    }
}
