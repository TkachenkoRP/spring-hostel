package ru.tkachenko.springhostel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tkachenko.springhostel.model.Guest;

public interface GuestRepository extends JpaRepository<Guest, Long> {
}
