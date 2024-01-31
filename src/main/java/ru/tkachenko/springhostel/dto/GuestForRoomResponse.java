package ru.tkachenko.springhostel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tkachenko.springhostel.model.GenderType;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuestForRoomResponse {
    private Long id;
    private String lastName;
    private String firstName;
    private String middleName;
    private GenderType genderType;
    private Instant createAt;
    private Instant updateAt;
}
