package ru.tkachenko.springhostel.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tkachenko.springhostel.dto.RoomForListResponse;
import ru.tkachenko.springhostel.dto.RoomResponse;
import ru.tkachenko.springhostel.dto.UpsertRoomRequest;
import ru.tkachenko.springhostel.mapper.RoomMapper;
import ru.tkachenko.springhostel.model.Room;
import ru.tkachenko.springhostel.service.RoomService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;
    private final RoomMapper roomMapper;

    @GetMapping
    public ResponseEntity<List<RoomForListResponse>> findAll() {
        return ResponseEntity.ok(roomService.findAll()
                .stream().map(roomMapper::entityToResponseForList)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(roomMapper.entityToResponse(roomService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<RoomResponse> create(@RequestBody @Valid UpsertRoomRequest request) {
        Room created = roomService.save(roomMapper.requestToEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(roomMapper.entityToResponse(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomResponse> update(@PathVariable Long id, @RequestBody @Valid UpsertRoomRequest request) {
        Room updated = roomService.update(roomMapper.requestToEntity(id, request));
        return ResponseEntity.ok(roomMapper.entityToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        roomService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
