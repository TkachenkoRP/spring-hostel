package ru.tkachenko.springhostel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tkachenko.springhostel.model.Gender;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuestResponse {
    private Long id;
    private String lastName;
    private String firstName;
    private String middleName;
    private Gender gender;
    private Instant createAt;
    private Instant updateAt;
    private RoomResponse room;
}
