package ru.tkachenko.springhostel.mapper;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.tkachenko.springhostel.dto.RoomForListResponse;
import ru.tkachenko.springhostel.dto.RoomResponse;
import ru.tkachenko.springhostel.dto.UpsertRoomRequest;
import ru.tkachenko.springhostel.model.Room;

@DecoratedWith(RoomMapperDelegate.class)
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoomMapper {
    Room requestToEntity(UpsertRoomRequest request);

    Room requestToEntity(Long id, UpsertRoomRequest request);

    RoomResponse entityToResponse(Room room);

    RoomForListResponse entityToResponseForList(Room room);
}
