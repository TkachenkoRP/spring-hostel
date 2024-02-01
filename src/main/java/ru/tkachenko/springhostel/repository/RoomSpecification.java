package ru.tkachenko.springhostel.repository;

import org.springframework.data.jpa.domain.Specification;
import ru.tkachenko.springhostel.dto.RoomFilter;
import ru.tkachenko.springhostel.model.ComfortType;
import ru.tkachenko.springhostel.model.Room;
import ru.tkachenko.springhostel.model.RoomType;

public interface RoomSpecification {
    static Specification<Room> withFilter(RoomFilter filter) {
        return Specification.where(byTypeRoom(filter.getTypeRoom()))
                .and(byTypeComfort(filter.getTypeComfort()))
                .and(byVacant(filter.isHasVacant()));
    }

    static Specification<Room> byTypeRoom(String typeRoom) {
        return (root, query, criteriaBuilder) -> {
            if (typeRoom == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get(Room.Fields.typeRoom), Enum.valueOf(RoomType.class, typeRoom));
        };
    }

    static Specification<Room> byTypeComfort(String typeComfort) {
        return (root, query, criteriaBuilder) -> {
            if (typeComfort == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get(Room.Fields.comfortType), Enum.valueOf(ComfortType.class, typeComfort));
        };
    }

    static Specification<Room> byVacant(boolean hasVacant) {
        return (root, query, criteriaBuilder) -> {
            if (hasVacant) {
                return criteriaBuilder.lessThan(
                        criteriaBuilder.size(root.get(Room.Fields.guests)),
                        root.get(Room.Fields.capacity)
                );
            }
            return null;
        };
    }
}
