package ru.tkachenko.springhostel.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tkachenko.springhostel.model.ComfortType;
import ru.tkachenko.springhostel.model.RoomType;
import ru.tkachenko.springhostel.valid.ValueOfEnum;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpsertRoomRequest {
    @Min(value = 1, message = "Укажите этаж от 1 до 10!")
    @Max(value = 10, message = "Укажите этаж от 1 до 10!")
    private byte floor;
    @Min(value = 1, message = "Укажите номер комнаты от 1 до 100!")
    @Max(value = 100, message = "Укажите номер комнаты от 1 до 100!")
    private byte roomNumber;
    @NotNull(message = "Укажите тип комнаты (MALE, FEMALE)!")
    @ValueOfEnum(enumClass = RoomType.class, message = "Неверно указан тип комнаты (MALE, FEMALE)!")
    private String typeRoom;
    @NotNull(message = "Укажите тип комфорта комнаты (STANDARD, HIGH_COMFORT, LUXURY)!")
    @ValueOfEnum(enumClass = ComfortType.class, message = "Неверно указан тип комфорта (STANDARD, HIGH_COMFORT, LUXURY)!")
    private String comfortType;
    @Min(value = 1, message = "Количество мест в комнате должно быть больше {value}!")
    private byte capacity;
}
