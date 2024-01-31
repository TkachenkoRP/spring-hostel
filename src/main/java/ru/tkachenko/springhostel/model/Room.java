package ru.tkachenko.springhostel.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private byte floor;
    @Column(name = "room_number")
    private byte roomNumber;
    @Column(name = "room_type")
    @Enumerated(value = EnumType.STRING)
    private RoomType typeRoom;
    @Column(name = "comfort_type")
    @Enumerated(value = EnumType.STRING)
    private ComfortType comfortType;
    private Byte capacity;
    @Column(name = "create_at")
    @CreationTimestamp
    private Instant createAt;
    @Column(name = "update_at")
    @UpdateTimestamp
    private Instant updateAt;
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Guest> guests;
}
