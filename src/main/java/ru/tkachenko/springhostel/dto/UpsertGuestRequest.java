package ru.tkachenko.springhostel.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tkachenko.springhostel.model.GenderType;
import ru.tkachenko.springhostel.valid.ValueOfEnum;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpsertGuestRequest {
    @NotBlank(message = "Укажите фамилию гостя!")
    private String lastName;
    @NotBlank(message = "Укажите имя гостя!")
    private String firstName;
    @NotBlank(message = "Укажите отчество гостя!")
    private String middleName;
    @NotNull(message = "Укажите пол гостя!")
    @ValueOfEnum(enumClass = GenderType.class, message = "Неверно указан пол гостя (MALE, FEMALE)!")
    private String genderType;
    @NotNull(message = "Укажите ID комнаты заселения!")
    private Long roomId;
}
