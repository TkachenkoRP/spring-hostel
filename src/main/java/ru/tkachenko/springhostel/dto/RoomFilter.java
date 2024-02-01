package ru.tkachenko.springhostel.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoomFilter {
    private String typeRoom;
    private String typeComfort;
    private boolean hasVacant;
}
