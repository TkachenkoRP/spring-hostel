package ru.tkachenko.springhostel.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tkachenko.springhostel.model.ComfortType;
import ru.tkachenko.springhostel.model.GenderType;
import ru.tkachenko.springhostel.valid.ValueOfEnum;

@Data
@NoArgsConstructor
public class GuestFilter {
    @ValueOfEnum(enumClass = GenderType.class, message = "Неверно указан пол гостя (MALE, FEMALE)!")
    private String typeGender;
    private Long roomId;
    @ValueOfEnum(enumClass = ComfortType.class, message = "Неверно указан тип комфорта (STANDARD, HIGH_COMFORT, LUXURY)!")
    private String typeComfort;
}
