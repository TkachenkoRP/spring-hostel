package ru.tkachenko.springhostel.repository;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import ru.tkachenko.springhostel.dto.GuestFilter;
import ru.tkachenko.springhostel.model.ComfortType;
import ru.tkachenko.springhostel.model.GenderType;
import ru.tkachenko.springhostel.model.Guest;
import ru.tkachenko.springhostel.model.Room;

public interface GuestSpecification {
    static Specification<Guest> withFilter(GuestFilter filter) {
        return Specification.where(byTypeGender(filter.getTypeGender()))
                .and(byRoom(filter.getRoomId()))
                .and(byTypeComfortRoom(filter.getTypeComfort()));
    }

    static Specification<Guest> byTypeGender(String typeGender) {
        return (root, query, criteriaBuilder) -> {
            if (typeGender == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get(Guest.Fields.genderType), Enum.valueOf(GenderType.class, typeGender));
        };
    }

    static Specification<Guest> byRoom(Long roomId) {
        return (root, query, criteriaBuilder) -> {
            if (roomId == null) {
                return null;
            }
            Join<Guest, Room> roomJoin = root.join(Guest.Fields.room);
            return criteriaBuilder.equal(roomJoin.get(Room.Fields.id), roomId);
        };
    }

    static Specification<Guest> byTypeComfortRoom(String typeComfort) {
        return (root, query, criteriaBuilder) -> {
            if (typeComfort == null) {
                return null;
            }
            Join<Guest, Room> roomJoin = root.join(Guest.Fields.room);
            return criteriaBuilder.equal(roomJoin.get(Room.Fields.comfortType), Enum.valueOf(ComfortType.class, typeComfort));
        };
    }
}
