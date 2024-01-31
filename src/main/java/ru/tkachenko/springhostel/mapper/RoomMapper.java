package ru.tkachenko.springhostel.mapper;

import org.mapstruct.*;
import ru.tkachenko.springhostel.dto.RoomForListResponse;
import ru.tkachenko.springhostel.dto.RoomResponse;
import ru.tkachenko.springhostel.dto.UpsertRoomRequest;
import ru.tkachenko.springhostel.model.Room;

@DecoratedWith(RoomMapperDelegate.class)
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoomMapper {
    @Mappings({
            @Mapping(source = "request.typeRoom", target = "typeRoom"),
            @Mapping(source = "request.comfortType", target = "comfortType")
    })
    Room requestToEntity(UpsertRoomRequest request);

    @Mappings({
            @Mapping(source = "request.typeRoom", target = "typeRoom"),
            @Mapping(source = "request.comfortType", target = "comfortType")
    })
    Room requestToEntity(Long id, UpsertRoomRequest request);

    RoomResponse entityToResponse(Room room);

    RoomForListResponse entityToResponseForList(Room room);
}
