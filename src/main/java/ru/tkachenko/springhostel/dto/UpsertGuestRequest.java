package ru.tkachenko.springhostel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tkachenko.springhostel.model.Gender;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpsertGuestRequest {
    private String lastName;
    private String firstName;
    private String middleName;
    private Gender gender;
    private Long roomId;
}
