package ru.tkachenko.springhostel.mapper;

import org.mapstruct.*;
import ru.tkachenko.springhostel.dto.RoomForListResponse;
import ru.tkachenko.springhostel.dto.RoomResponse;
import ru.tkachenko.springhostel.dto.UpsertRoomRequest;
import ru.tkachenko.springhostel.model.Room;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoomMapper {
    Room requestToEntity(UpsertRoomRequest request);

    Room requestToEntity(Long id, UpsertRoomRequest request);

    RoomResponse entityToResponse(Room room);

    @Mappings({
            @Mapping(target = "guestsCount", expression = "java(room.getGuests().size())")
    })
    RoomForListResponse entityToResponseForList(Room room);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "guests", ignore = true),
            @Mapping(target = "createAt", ignore = true),
            @Mapping(target = "updateAt", ignore = true)
    })
    void updateRoom(Room sourceRoom, @MappingTarget Room targetRoom);
}
