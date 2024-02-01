package ru.tkachenko.springhostel.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.*;
import org.springframework.http.MediaType;
import ru.tkachenko.springhostel.AbstractTestController;
import ru.tkachenko.springhostel.StringTestUtils;
import ru.tkachenko.springhostel.dto.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GuestControllerTest extends AbstractTestController {

    @Test
    @Order(1)
    public void whenFindAllGuests_thenReturnAllGuests() throws Exception {

        String actualResponse = mockMvc.perform(get("/api/guest"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<GuestForListResponse> guestsResponses = objectMapper.readValue(actualResponse, new TypeReference<>() {
        });

        assertEquals(15, guestsResponses.size());
    }

    @Test
    @Order(1)
    public void whenFindAllGuestsWithTypeGenderFilter_thenReturnFilteredGuests() throws Exception {

        String typeGender = "MALE";

        String actualResponse = mockMvc.perform(get("/api/guest?typeGender=" + typeGender))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<GuestForListResponse> guestsResponses = objectMapper.readValue(actualResponse, new TypeReference<>() {
        });

        assertEquals(6, guestsResponses.size());

        for (GuestForListResponse response : guestsResponses) {
            assertEquals(typeGender, response.getGenderType().name());
        }
    }

    @Test
    @Order(1)
    public void whenFindAllGuestsWithRoomIdFilter_thenReturnFilteredGuests() throws Exception {

        Long roomId = 4L;

        String actualResponse = mockMvc.perform(get("/api/guest?roomId=" + roomId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<GuestForListResponse> guestsResponses = objectMapper.readValue(actualResponse, new TypeReference<>() {
        });

        assertEquals(2, guestsResponses.size());

        for (GuestForListResponse response : guestsResponses) {
            assertEquals(roomId, response.getRoomId());
        }
    }

    @Test
    @Order(1)
    public void whenFindAllGuestsWithTypeComfortFilter_thenReturnFilteredGuests() throws Exception {

        String typeComfort = "LUXURY";

        String actualResponse = mockMvc.perform(get("/api/guest?typeComfort=" + typeComfort))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<GuestForListResponse> guestsResponses = objectMapper.readValue(actualResponse, new TypeReference<>() {
        });

        assertEquals(2, guestsResponses.size());

        for (GuestForListResponse response : guestsResponses) {
            assertEquals(6, response.getRoomId());
        }
    }

    @Test
    @Order(1)
    public void whenFindAllGuestsWithAllFilter_thenReturnFilteredGuests() throws Exception {

        String typeGender = "FEMALE";
        Long roomId = 5L;
        String typeComfort = "HIGH_COMFORT";

        String actualResponse = mockMvc.perform(get("/api/guest?typeGender=" + typeGender + "&roomId=" + roomId + "&typeComfort=" + typeComfort))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<GuestForListResponse> guestsResponses = objectMapper.readValue(actualResponse, new TypeReference<>() {
        });

        assertEquals(3, guestsResponses.size());

        for (GuestForListResponse response : guestsResponses) {
            assertEquals(roomId, response.getRoomId());
            assertEquals(typeGender, response.getGenderType().name());
        }
    }

    @Test
    public void whenFindAllGuestsWithWrongTypeGenderFilter_thenReturnError() throws Exception {

        String typeGender = "MALEE";

        var response = mockMvc.perform(get("/api/guest?typeGender=" + typeGender))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/wrong_gender_type_guest.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    public void whenFindAllGuestsWithWrongRoomIdFilter_thenReturnError() throws Exception {

        Long roomId = -500L;

        var response = mockMvc.perform(get("/api/guest?roomId=" + roomId))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/wrong_room_id_guest.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    public void whenFindAllGuestsWithWrongTypeComfortFilter_thenReturnError() throws Exception {

        String typeComfort = "LUXURYY";

        var response = mockMvc.perform(get("/api/guest?typeComfort=" + typeComfort))
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
    public void whenFindById_thenReturnGuest() throws Exception {

        String actualResponse = mockMvc.perform(get("/api/guest/5"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        GuestResponse response = objectMapper.readValue(actualResponse, GuestResponse.class);

        assertEquals(5, response.getId());
        assertEquals("last_name5", response.getLastName());
        assertEquals("first_name5", response.getFirstName());
        assertEquals("middle_name5", response.getMiddleName());
        assertEquals("FEMALE", response.getGenderType().name());
    }

    @Test
    public void whenFindByWrongId_thenReturnError() throws Exception {

        var response = mockMvc.perform(get("/api/guest/500"))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/wrong_guest_id_500_not_found_response.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    @Order(3)
    public void whenCreateGuest_thenReturnNewGuest() throws Exception {

        String female = "FEMALE";

        UpsertGuestRequest request = new UpsertGuestRequest();
        request.setLastName("newLastName");
        request.setFirstName("newFirstName");
        request.setMiddleName("newMiddleName");
        request.setGenderType(female);
        request.setRoomId(2L);

        String actualResponse = mockMvc.perform(post("/api/guest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        GuestResponse response = objectMapper.readValue(actualResponse, GuestResponse.class);
        assertEquals(16L, response.getId());
        assertEquals("newLastName", response.getLastName());
        assertEquals(female, response.getGenderType().name());
        assertNotNull(response.getCreateAt());
        assertNotNull(response.getUpdateAt());
        assertEquals(2L, response.getRoom().getId());
        assertEquals(female, response.getRoom().getTypeRoom());
    }

    @Test
    public void whenCreateGuestWithoutLastName_thenReturnError() throws Exception {

        String female = "FEMALE";

        UpsertGuestRequest request = new UpsertGuestRequest();
        request.setFirstName("newFirstName");
        request.setMiddleName("newMiddleName");
        request.setGenderType(female);
        request.setRoomId(2L);

        var response = mockMvc.perform(post("/api/guest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/create_guest_without_data_last_name.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    public void whenCreateGuestWithoutFirstName_thenReturnError() throws Exception {

        String female = "FEMALE";

        UpsertGuestRequest request = new UpsertGuestRequest();
        request.setLastName("newLastName");
        request.setMiddleName("newMiddleName");
        request.setGenderType(female);
        request.setRoomId(2L);

        var response = mockMvc.perform(post("/api/guest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/create_guest_without_data_first_name.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    public void whenCreateGuestWithoutMiddleName_thenReturnError() throws Exception {

        String female = "FEMALE";

        UpsertGuestRequest request = new UpsertGuestRequest();
        request.setLastName("newLastName");
        request.setFirstName("newFirstName");
        request.setGenderType(female);
        request.setRoomId(2L);

        var response = mockMvc.perform(post("/api/guest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/create_guest_without_data_middle_name.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    public void whenCreateGuestWithoutGenderType_thenReturnError() throws Exception {
        UpsertGuestRequest request = new UpsertGuestRequest();
        request.setLastName("newLastName");
        request.setFirstName("newFirstName");
        request.setMiddleName("newMiddleName");
        request.setRoomId(2L);

        var response = mockMvc.perform(post("/api/guest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/create_guest_without_data_gender_type.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    public void whenCreateGuestWithoutRoomId_thenReturnError() throws Exception {

        String female = "FEMALE";

        UpsertGuestRequest request = new UpsertGuestRequest();
        request.setLastName("newLastName");
        request.setFirstName("newFirstName");
        request.setMiddleName("newMiddleName");
        request.setGenderType(female);

        var response = mockMvc.perform(post("/api/guest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/create_guest_without_data_room_id.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    public void whenCreateGuestWithWrongRoomId_thenReturnError() throws Exception {

        String female = "FEMALE";

        UpsertGuestRequest request = new UpsertGuestRequest();
        request.setLastName("newLastName");
        request.setFirstName("newFirstName");
        request.setMiddleName("newMiddleName");
        request.setGenderType(female);
        request.setRoomId(500L);

        var response = mockMvc.perform(post("/api/guest")
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
    public void whenCreateGuestWithWrongRoomType_thenReturnError() throws Exception {
        UpsertGuestRequest request = new UpsertGuestRequest();
        request.setLastName("newLastName");
        request.setFirstName("newFirstName");
        request.setMiddleName("newMiddleName");
        request.setGenderType("FEMALE");
        request.setRoomId(1L);

        var response = mockMvc.perform(post("/api/guest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/guest_with_wrong_room_type.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    public void whenCreateGuestInOccupiedRoom_thenReturnError() throws Exception {
        UpsertGuestRequest request = new UpsertGuestRequest();
        request.setLastName("newLastName");
        request.setFirstName("newFirstName");
        request.setMiddleName("newMiddleName");
        request.setGenderType("FEMALE");
        request.setRoomId(7L);

        var response = mockMvc.perform(post("/api/guest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/guest_in_occupied_room.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    @Order(4)
    public void whenUpdateGuest_thenReturnUpdatedGuest() throws Exception {
        UpsertGuestRequest request = new UpsertGuestRequest();
        request.setLastName("newLastName");
        request.setFirstName("newFirstName");
        request.setMiddleName("newMiddleName");
        request.setGenderType("FEMALE");
        request.setRoomId(3L);

        String actualResponse = mockMvc.perform(put("/api/guest/9")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        GuestResponse response = objectMapper.readValue(actualResponse, GuestResponse.class);
        assertEquals(9L, response.getId());
        assertEquals("newLastName", response.getLastName());
        assertEquals("FEMALE", response.getGenderType().name());
        assertNotNull(response.getCreateAt());
        assertNotNull(response.getUpdateAt());
        assertEquals(3L, response.getRoom().getId());


        LocalDate today = LocalDate.now();
        LocalDate updateDate = response.getUpdateAt().atZone(ZoneId.systemDefault()).toLocalDate();

        assertEquals(today.getYear(), updateDate.getYear());
        assertEquals(today.getMonth(), updateDate.getMonth());
        assertEquals(today.getDayOfMonth(), updateDate.getDayOfMonth());
    }

    @Test
    public void whenUpdateGuestWithWrongRoomType_thenReturnError() throws Exception {
        UpsertGuestRequest request = new UpsertGuestRequest();
        request.setLastName("newLastName");
        request.setFirstName("newFirstName");
        request.setMiddleName("newMiddleName");
        request.setGenderType("FEMALE");
        request.setRoomId(1L);

        var response = mockMvc.perform(put("/api/guest/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/guest_with_wrong_room_type.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    public void whenUpdateGuestInOccupiedRoom_thenReturnError() throws Exception {
        UpsertGuestRequest request = new UpsertGuestRequest();
        request.setLastName("newLastName");
        request.setFirstName("newFirstName");
        request.setMiddleName("newMiddleName");
        request.setGenderType("FEMALE");
        request.setRoomId(7L);

        var response = mockMvc.perform(put("/api/guest/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectResponse = StringTestUtils.readStringFromResource("response/guest_in_occupied_room.json");

        JsonAssert.assertJsonEquals(expectResponse, actualResponse);
    }

    @Test
    public void whenUpdateGuestInOccupiedRoomHeFromHer_thenReturnError() throws Exception {
        UpsertGuestRequest request = new UpsertGuestRequest();
        request.setLastName("newLastName");
        request.setFirstName("newFirstName");
        request.setMiddleName("newMiddleName");
        request.setGenderType("FEMALE");
        request.setRoomId(7L);

        var actualResponse = mockMvc.perform(put("/api/guest/15")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        GuestResponse response = objectMapper.readValue(actualResponse, GuestResponse.class);
        assertEquals(15L, response.getId());
        assertEquals("newLastName", response.getLastName());
        assertEquals("FEMALE", response.getGenderType().name());
        assertEquals(7L, response.getRoom().getId());


        LocalDate today = LocalDate.now();
        LocalDate updateDate = response.getUpdateAt().atZone(ZoneId.systemDefault()).toLocalDate();

        assertEquals(today.getYear(), updateDate.getYear());
        assertEquals(today.getMonth(), updateDate.getMonth());
        assertEquals(today.getDayOfMonth(), updateDate.getDayOfMonth());
    }

    @Test
    @Order(5)
    public void whenDeleteGuestById_thenReturnStatusNoContent() throws Exception {
        mockMvc.perform(get("/api/guest/13"))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/api/guest/13"))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/api/guest/13"))
                .andExpect(status().isNotFound());
    }
}
