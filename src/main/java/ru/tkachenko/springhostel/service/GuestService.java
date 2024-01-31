package ru.tkachenko.springhostel.service;

import ru.tkachenko.springhostel.model.Guest;

import java.util.List;

public interface GuestService {
    List<Guest> findAll();

    Guest findById(Long id);

    Guest save(Guest guest);

    Guest update(Guest guest);

    void delete(Long id);
}
