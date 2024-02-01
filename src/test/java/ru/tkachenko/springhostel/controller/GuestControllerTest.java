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
    @Order(4)
    public void whenUpdateGuest_thenReturnUpdatedGuest() throws Exception {
        UpsertGuestRequest request = new UpsertGuestRequest();
        request.setLastName("newLastName");
        request.setFirstName("newFirstName");
        request.setMiddleName("newMiddleName");
        request.setGenderType("FEMALE");
        request.setRoomId(2L);

        String actualResponse = mockMvc.perform(put("/api/guest/16")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        GuestResponse response = objectMapper.readValue(actualResponse, GuestResponse.class);
        assertEquals(16L, response.getId());
        assertEquals("newLastName", response.getLastName());
        assertEquals("FEMALE", response.getGenderType().name());
        assertNotNull(response.getCreateAt());
        assertNotNull(response.getUpdateAt());
        assertEquals(2L, response.getRoom().getId());


        LocalDate today = LocalDate.now();
        LocalDate updateDate = response.getUpdateAt().atZone(ZoneId.systemDefault()).toLocalDate();

        assertEquals(today.getYear(), updateDate.getYear());
        assertEquals(today.getMonth(), updateDate.getMonth());
        assertEquals(today.getDayOfMonth(), updateDate.getDayOfMonth());
    }

    @Test
    @Order(5)
    public void whenDeleteGuestById_thenReturnStatusNoContent() throws Exception {
        mockMvc.perform(get("/api/guest/16"))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/api/guest/16"))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/api/guest/16"))
                .andExpect(status().isNotFound());
    }
}
