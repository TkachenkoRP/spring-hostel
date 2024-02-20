package ru.tkachenko.springhostel.mapper;

import org.mapstruct.*;
import ru.tkachenko.springhostel.dto.GuestForListResponse;
import ru.tkachenko.springhostel.dto.GuestResponse;
import ru.tkachenko.springhostel.dto.UpsertGuestRequest;
import ru.tkachenko.springhostel.model.Guest;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {RoomMapper.class, RoomMap.class})
public interface GuestMapper {
    @Mappings({
            @Mapping(target = "room", source = "roomId")
    })
    Guest requestToEntity(UpsertGuestRequest request);

    @Mappings({
            @Mapping(target = "room", source = "request.roomId")
    })
    Guest requestToEntity(Long id, UpsertGuestRequest request);

    GuestResponse entityToResponse(Guest guest);

    @Mappings({
            @Mapping(target = "roomId", source = "room.id")
    })
    GuestForListResponse entityToResponseList(Guest guest);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createAt", ignore = true),
            @Mapping(target = "updateAt", ignore = true)
    })
    void updateGuest(Guest sourceGuest, @MappingTarget Guest targetGuest);
}
