package ru.tkachenko.springhostel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tkachenko.springhostel.model.ComfortType;
import ru.tkachenko.springhostel.model.RoomType;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomForListResponse {
    private Long id;
    private Byte floor;
    private Byte roomNumber;
    private RoomType typeRoom;
    private ComfortType comfortType;
    private Byte capacity;
    private Instant createAt;
    private Instant updateAt;
    private int guestsCount;
}
