package ru.tkachenko.springhostel.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.MediaType;
import ru.tkachenko.springhostel.AbstractTestController;
import ru.tkachenko.springhostel.StringTestUtils;
import ru.tkachenko.springhostel.dto.RoomForListResponse;
import ru.tkachenko.springhostel.dto.RoomResponse;
import ru.tkachenko.springhostel.dto.UpsertRoomRequest;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RoomControllerTest extends AbstractTestController {

    @Test
    @Order(1)
    public void whenFindAllRooms_thenReturnAllRooms() throws Exception {

        String actualResponse = mockMvc.perform(get("/api/room"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<RoomForListResponse> roomResponses = objectMapper.readValue(actualResponse, new TypeReference<>() {
        });

        assertEquals(8, roomResponses.size());
    }

    @Test
    @Order(1)
    public void whenFindAllWithTypeRoomFilter_thenReturnFilteredRooms() throws Exception {

        String typeRoom = "FEMALE";

        String actualResponse = mockMvc.perform(get("/api/room?typeRoom=" + typeRoom))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<RoomForListResponse> roomResponses = objectMapper.readValue(actualResponse, new TypeReference<>() {
        });

        assertEquals(4, roomResponses.size());

        for (RoomForListResponse response : roomResponses) {
            assertEquals(typeRoom, response.getTypeRoom().name());
        }
    }

    @Test
    @Order(1)
    public void whenFindAllWithTypeComfortFilter_thenReturnFilteredRooms() throws Exception {

        String typeComfort = "HIGH_COMFORT";

        String actualResponse = mockMvc.perform(get("/api/room?typeComfort=" + typeComfort))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<RoomForListResponse> roomResponses = objectMapper.readValue(actualResponse, new TypeReference<>() {
        });

        assertEquals(4, roomResponses.size());

        for (RoomForListResponse response : roomResponses) {
            assertEquals(typeComfort, response.getComfortType().name());
        }
    }

    @Test
    @Order(1)
    public void whenFindAllWithVacantFilter_thenReturnFilteredRooms() throws Exception {

        String actualResponse = mockMvc.perform(get("/api/room?hasVacant=" + true))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<RoomForListResponse> roomResponses = objectMapper.readValue(actualResponse, new TypeReference<>() {
        });

        assertEquals(7, roomResponses.size());

        for (RoomForListResponse response : roomResponses) {
            assertTrue(response.getGuestsCount() < response.getCapacity());
        }
    }

    @Test
    @Order(1)
    public void whenFindAllWithAllFilter_thenReturnFilteredRooms() throws Exception {

        String typeRoom = "FEMALE";
        String typeComfort = "HIGH_COMFORT";

        String actualResponse = mockMvc.perform(get("/api/room?typeRoom=" + typeRoom + "&typeComfort=" + typeComfort + "&hasVacant=" + true))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<RoomForListResponse> roomResponses = objectMapper.readValue(actualResponse, new TypeReference<>() {
        });

        assertEquals(2, roomResponses.size());

        for (RoomForListResponse response : roomResponses) {
            assertEquals(typeRoom, response.getTypeRoom().name());
            assertEquals(typeComfort, response.getComfortType().name());
            assertTrue(response.getGuestsCount() < response.getCapacity());
        }
    }

    @Test
    public void whenFindAllWithWrongTypeRoomFilter_thenReturnError() throws Exception {

        String typeRoom = "FEMALEE";

        var response = mockMvc.perform(get("/api/room?typeRoom=" + typeRoom))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/wrong_type_room.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    public void whenFindAllWithWrongTypeComfortFilter_thenReturnError() throws Exception {

        String typeComfort = "HIGH_COMFORTT";

        var response = mockMvc.perform(get("/api/room?typeComfort=" + typeComfort))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/wrong_comfort_type_room.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    @Order(2)
    public void whenFindById_thenReturnRoom() throws Exception {

        String actualResponse = mockMvc.perform(get("/api/room/5"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        RoomResponse response = objectMapper.readValue(actualResponse, RoomResponse.class);

        assertEquals(5, response.getId());
        assertEquals((byte) 5, response.getFloor());
        assertEquals((byte) 5, response.getRoomNumber());
        assertEquals("FEMALE", response.getTypeRoom());
        assertEquals("HIGH_COMFORT", response.getComfortType());
        assertEquals((byte) 10, response.getCapacity());
    }

    @Test
    public void whenFindByWrongId_thenReturnError() throws Exception {
        var response = mockMvc.perform(get("/api/room/500"))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/wrong_room_id_500_not_found_response.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    @Order(3)
    public void whenCreateRoom_thenReturnNewRoom() throws Exception {
        UpsertRoomRequest request = new UpsertRoomRequest();
        request.setFloor((byte) 5);
        request.setRoomNumber((byte) 50);
        request.setTypeRoom("MALE");
        request.setComfortType("HIGH_COMFORT");
        request.setCapacity((byte) 10);

        String actualResponse = mockMvc.perform(post("/api/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        RoomResponse response = objectMapper.readValue(actualResponse, RoomResponse.class);
        assertEquals(9L, response.getId());
        assertNotNull(response.getCreateAt());
        assertNotNull(response.getUpdateAt());
    }

    @ParameterizedTest
    @MethodSource("invalidNumbers")
    public void whenCreateRoomWithWrongFloor_thenReturnError(int number) throws Exception {
        UpsertRoomRequest request = new UpsertRoomRequest();
        request.setFloor((byte) number);
        request.setRoomNumber((byte) 1);
        request.setTypeRoom("MALE");
        request.setComfortType("HIGH_COMFORT");
        request.setCapacity((byte) 1);

        var response = mockMvc.perform(post("/api/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/create_room_with_wrong_data_floor.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @ParameterizedTest
    @MethodSource("invalidNumbers")
    public void whenCreateRoomWithWrongRoomNumber_thenReturnError(int number) throws Exception {
        UpsertRoomRequest request = new UpsertRoomRequest();
        request.setFloor((byte) 1);
        request.setRoomNumber((byte) number);
        request.setTypeRoom("MALE");
        request.setComfortType("HIGH_COMFORT");
        request.setCapacity((byte) 1);

        var response = mockMvc.perform(post("/api/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/create_room_with_wrong_data_room_number.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    public void whenCreateRoomWithWrongTypeRoom_thenReturnError() throws Exception {
        UpsertRoomRequest request = new UpsertRoomRequest();
        request.setFloor((byte) 1);
        request.setRoomNumber((byte) 1);
        request.setTypeRoom("MALEE");
        request.setComfortType("HIGH_COMFORT");
        request.setCapacity((byte) 1);

        var response = mockMvc.perform(post("/api/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/wrong_type_room.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    public void whenCreateRoomWithoutTypeRoom_thenReturnError() throws Exception {
        UpsertRoomRequest request = new UpsertRoomRequest();
        request.setFloor((byte) 1);
        request.setRoomNumber((byte) 1);
        request.setComfortType("HIGH_COMFORT");
        request.setCapacity((byte) 1);

        var response = mockMvc.perform(post("/api/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/create_room_without_data_type_room.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    public void whenCreateRoomWithWrongComfortType_thenReturnError() throws Exception {
        UpsertRoomRequest request = new UpsertRoomRequest();
        request.setFloor((byte) 1);
        request.setRoomNumber((byte) 1);
        request.setTypeRoom("MALE");
        request.setComfortType("HIGH_COMFORTT");
        request.setCapacity((byte) 1);

        var response = mockMvc.perform(post("/api/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/wrong_comfort_type_room.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    public void whenCreateRoomWithoutComfortType_thenReturnError() throws Exception {
        UpsertRoomRequest request = new UpsertRoomRequest();
        request.setFloor((byte) 1);
        request.setRoomNumber((byte) 1);
        request.setTypeRoom("MALE");
        request.setCapacity((byte) 1);

        var response = mockMvc.perform(post("/api/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/create_room_without_data_comfort_type.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    public void whenCreateRoomWithWrongCapacity_thenReturnError() throws Exception {
        UpsertRoomRequest request = new UpsertRoomRequest();
        request.setFloor((byte) 1);
        request.setRoomNumber((byte) 1);
        request.setTypeRoom("MALE");
        request.setComfortType("HIGH_COMFORT");
        request.setCapacity((byte) -5);

        var response = mockMvc.perform(post("/api/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/create_room_with_wrong_data_capacity.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    @Order(4)
    public void whenUpdateRoom_thenReturnUpdatedRoom() throws Exception {
        UpsertRoomRequest request = new UpsertRoomRequest();
        request.setFloor((byte) 1);
        request.setRoomNumber((byte) 49);
        request.setTypeRoom("FEMALE");
        request.setComfortType("STANDARD");
        request.setCapacity((byte) 9);

        String actualResponse = mockMvc.perform(put("/api/room/7")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        RoomResponse response = objectMapper.readValue(actualResponse, RoomResponse.class);
        assertEquals(7L, response.getId());
        assertEquals((byte) 1, response.getFloor());
        assertEquals((byte) 49, response.getRoomNumber());
        assertEquals("FEMALE", response.getTypeRoom());
        assertEquals("STANDARD", response.getComfortType());
        assertEquals((byte) 9, response.getCapacity());


        LocalDate today = LocalDate.now();
        LocalDate updateDate = response.getUpdateAt().atZone(ZoneId.systemDefault()).toLocalDate();

        assertEquals(today.getYear(), updateDate.getYear());
        assertEquals(today.getMonth(), updateDate.getMonth());
        assertEquals(today.getDayOfMonth(), updateDate.getDayOfMonth());
    }

    @Test
    public void whenUpdateRoomWithWrongId_thenReturnError() throws Exception {
        UpsertRoomRequest request = new UpsertRoomRequest();
        request.setFloor((byte) 1);
        request.setRoomNumber((byte) 49);
        request.setTypeRoom("MALE");
        request.setComfortType("STANDARD");
        request.setCapacity((byte) 9);

        var response = mockMvc.perform(put("/api/room/500")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/wrong_room_id_500_not_found_response.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    public void whenUpdateRoomWithWrongTypeAndWithGuest_thenReturnError() throws Exception {
        UpsertRoomRequest request = new UpsertRoomRequest();
        request.setFloor((byte) 1);
        request.setRoomNumber((byte) 49);
        request.setTypeRoom("MALE");
        request.setComfortType("STANDARD");
        request.setCapacity((byte) 9);

        var response = mockMvc.perform(put("/api/room/7")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/fail_room_update.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    @Order(5)
    public void whenDeleteRoomById_thenReturnStatusNoContent() throws Exception {
        mockMvc.perform(get("/api/room/8"))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/api/room/8"))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/api/room/8"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(5)
    public void whenDeleteRoomByIdWithGuests_thenReturnError() throws Exception {
        var response = mockMvc.perform(delete("/api/room/1"))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/fail_room_delete.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    private static Stream<Arguments> invalidNumbers() {
        return Stream.of(
                Arguments.of(-1),
                Arguments.of(0),
                Arguments.of(1001)
        );
    }
}
