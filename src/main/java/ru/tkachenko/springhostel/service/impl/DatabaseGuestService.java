package ru.tkachenko.springhostel.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tkachenko.springhostel.aop.RoomAllocationCheck;
import ru.tkachenko.springhostel.dto.GuestFilter;
import ru.tkachenko.springhostel.exception.EntityNotFoundException;
import ru.tkachenko.springhostel.model.Guest;
import ru.tkachenko.springhostel.repository.GuestRepository;
import ru.tkachenko.springhostel.repository.GuestSpecification;
import ru.tkachenko.springhostel.service.GuestService;
import ru.tkachenko.springhostel.utils.BeanUtils;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DatabaseGuestService implements GuestService {

    private final GuestRepository repository;

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
    @RoomAllocationCheck
    public Guest save(Guest guest) {
        return repository.save(guest);
    }

    @Override
    @RoomAllocationCheck
    public Guest update(Guest guest) {
        Guest existedGuest = findById(guest.getId());
        BeanUtils.copyNonNullProperties(guest, existedGuest);
        return repository.save(existedGuest);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
