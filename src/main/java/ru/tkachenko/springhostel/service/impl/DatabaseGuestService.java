package ru.tkachenko.springhostel.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tkachenko.springhostel.dto.GuestFilter;
import ru.tkachenko.springhostel.exception.EntityNotFoundException;
import ru.tkachenko.springhostel.exception.RoomAllocationException;
import ru.tkachenko.springhostel.model.GenderType;
import ru.tkachenko.springhostel.model.Guest;
import ru.tkachenko.springhostel.model.Room;
import ru.tkachenko.springhostel.model.RoomType;
import ru.tkachenko.springhostel.repository.GuestRepository;
import ru.tkachenko.springhostel.repository.GuestSpecification;
import ru.tkachenko.springhostel.service.GuestService;
import ru.tkachenko.springhostel.service.RoomService;
import ru.tkachenko.springhostel.utils.BeanUtils;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DatabaseGuestService implements GuestService {

    private final GuestRepository repository;
    private final RoomService roomService;

    @Override
    public List<Guest> findAll(GuestFilter filter) {
        return repository.findAll(GuestSpecification.withFilter(filter));
    }

    @Override
    public Guest findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format(
                        "Гость с ID {0} не найден!", id
                )));
    }

    @Override
    public Guest save(Guest guest) {
        checkRoomAllocation(guest);
        return repository.save(guest);
    }

    @Override
    public Guest update(Guest guest) {
        checkRoomAllocation(guest);
        Guest existedGuest = findById(guest.getId());
        BeanUtils.copyNonNullProperties(guest, existedGuest);
        return repository.save(existedGuest);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private void checkRoomAllocation(Guest guest) {
        Room room = roomService.findById(guest.getRoom().getId());

        if (room.getTypeRoom() != null && guest.getGenderType() != null) {
            if ((room.getTypeRoom() == RoomType.FEMALE && guest.getGenderType() == GenderType.MALE) ||
                    (room.getTypeRoom() == RoomType.MALE && guest.getGenderType() == GenderType.FEMALE)) {
                throw new RoomAllocationException("Несоответствие типа комнаты и пола гостя!");
            }
        }

        if (room.getGuests().stream().noneMatch(g -> g.getId().equals(guest.getId()))) {
            if (room.getCapacity() <= room.getGuests().size()) {
                throw new RoomAllocationException("Комната заполнена, нет свободных мест!");
            }
        }
    }
}
