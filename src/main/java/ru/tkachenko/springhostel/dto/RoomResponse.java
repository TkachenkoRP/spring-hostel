package ru.tkachenko.springhostel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomResponse {
    private Long id;
    private Byte floor;
    private Byte roomNumber;
    private String typeRoom;
    private String comfortType;
    private Byte capacity;
    private Instant createAt;
    private Instant updateAt;
    private List<GuestForRoomResponse> guests;
}
