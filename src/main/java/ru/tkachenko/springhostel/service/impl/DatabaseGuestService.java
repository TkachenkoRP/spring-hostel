package ru.tkachenko.springhostel.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tkachenko.springhostel.model.Guest;
import ru.tkachenko.springhostel.repository.GuestRepository;
import ru.tkachenko.springhostel.service.GuestService;
import ru.tkachenko.springhostel.utils.BeanUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DatabaseGuestService implements GuestService {

    private final GuestRepository repository;

    @Override
    public List<Guest> findAll() {
        return repository.findAll();
    }

    @Override
    public Guest findById(Long id) {
        return repository.findById(id)
                .orElse(null);
    }

    @Override
    public Guest save(Guest guest) {
        return repository.save(guest);
    }

    @Override
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
