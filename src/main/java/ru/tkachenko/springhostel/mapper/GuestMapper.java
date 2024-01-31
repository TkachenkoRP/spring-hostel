package ru.tkachenko.springhostel.mapper;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.tkachenko.springhostel.dto.GuestForListResponse;
import ru.tkachenko.springhostel.dto.GuestResponse;
import ru.tkachenko.springhostel.dto.UpsertGuestRequest;
import ru.tkachenko.springhostel.model.Guest;

@DecoratedWith(GuestMapperDelegate.class)
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {RoomMapper.class})
public interface GuestMapper {
    Guest requestToEntity(UpsertGuestRequest request);

    Guest requestToEntity(Long id, UpsertGuestRequest request);

    GuestResponse entityToResponse(Guest guest);

    GuestForListResponse entityToResponseList(Guest guest);
}
