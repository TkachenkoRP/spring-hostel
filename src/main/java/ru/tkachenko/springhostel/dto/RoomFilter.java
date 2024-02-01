package ru.tkachenko.springhostel.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tkachenko.springhostel.model.ComfortType;
import ru.tkachenko.springhostel.model.RoomType;
import ru.tkachenko.springhostel.valid.ValueOfEnum;

@Data
@NoArgsConstructor
public class RoomFilter {
    @ValueOfEnum(enumClass = RoomType.class, message = "Неверно указан тип комнаты (MALE, FEMALE)!")
    private String typeRoom;
    @ValueOfEnum(enumClass = ComfortType.class, message = "Неверно указан тип комфорта (STANDARD, HIGH_COMFORT, LUXURY)!")
    private String typeComfort;
    private boolean hasVacant;
}
