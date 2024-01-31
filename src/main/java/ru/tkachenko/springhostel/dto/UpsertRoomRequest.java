package ru.tkachenko.springhostel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tkachenko.springhostel.model.ComfortType;
import ru.tkachenko.springhostel.model.RoomType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpsertRoomRequest {
    private byte floor;
    private byte roomNumber;
    private RoomType typeRoom;
    private ComfortType comfortType;
    private byte capacity;
}
