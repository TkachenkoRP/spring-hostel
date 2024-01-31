package ru.tkachenko.springhostel.init;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.tkachenko.springhostel.model.*;
import ru.tkachenko.springhostel.repository.GuestRepository;
import ru.tkachenko.springhostel.repository.RoomRepository;

import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(prefix = "app.database.init", name = "enabled", havingValue = "true")
public class InitDatabase {
    private final RoomRepository roomRepository;
    private final GuestRepository guestRepository;

    @PostConstruct
    public void initData() {

        if (roomRepository.count() > 0) {
            log.info("В базе уже есть записи!");
            return;
        }

        int countRooms = 15;
        int maxCapacity = 12;
        int minCapacity = 2;

        for (int i = 0; i < countRooms; ) {

            byte rndCapacity = (byte) (new Random().nextInt(maxCapacity - minCapacity + 1) + minCapacity);

            Room room = Room.builder()
                    .floor((byte) ++i)
                    .roomNumber((byte) (i + 100))
                    .typeRoom(i % 2 == 0 ? RoomType.MALE : RoomType.FEMALE)
                    .comfortType(i % 3 == 0 ? ComfortType.STANDARD : i % 3 == 1 ? ComfortType.HIGH_COMFORT : ComfortType.LUXURY)
                    .capacity(rndCapacity)
                    .build();
            roomRepository.save(room);

            int rndGuestCount = new Random().nextInt(rndCapacity + 1);
            Gender guestGender = room.getTypeRoom() == RoomType.MALE ? Gender.MALE : Gender.FEMALE;
            for (int j = 0; j < rndGuestCount; ) {
                Guest guest = new Guest().builder()
                        .lastName("lastName_" + i + ++j)
                        .firstName("firstName_" + i + j)
                        .middleName("middleName_" + i + j)
                        .gender(guestGender)
                        .room(room)
                        .build();
                guestRepository.save(guest);
            }
        }
    }
}
