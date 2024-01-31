package ru.tkachenko.springhostel.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tkachenko.springhostel.dto.GuestForListResponse;
import ru.tkachenko.springhostel.dto.GuestResponse;
import ru.tkachenko.springhostel.dto.UpsertGuestRequest;
import ru.tkachenko.springhostel.mapper.GuestMapper;
import ru.tkachenko.springhostel.model.Guest;
import ru.tkachenko.springhostel.service.GuestService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/guest")
@RequiredArgsConstructor
public class GuestController {
    private final GuestService guestService;
    private final GuestMapper guestMapper;

    @GetMapping
    public ResponseEntity<List<GuestForListResponse>> findAll() {
        return ResponseEntity.ok(guestService.findAll()
                .stream().map(guestMapper::entityToResponseList)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GuestResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(guestMapper.entityToResponse(guestService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<GuestResponse> create(@RequestBody UpsertGuestRequest request) {
        Guest created = guestService.save(guestMapper.requestToEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(guestMapper.entityToResponse(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GuestResponse> update(@PathVariable Long id, @RequestBody UpsertGuestRequest request) {
        Guest updated = guestService.update(guestMapper.requestToEntity(id, request));
        return ResponseEntity.ok(guestMapper.entityToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        guestService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
