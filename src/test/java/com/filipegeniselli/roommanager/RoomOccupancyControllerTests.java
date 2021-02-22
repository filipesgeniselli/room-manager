package com.filipegeniselli.roommanager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.filipegeniselli.roommanager.controller.RoomOccupancyController;
import com.filipegeniselli.roommanager.dto.OptimizedRooms;
import com.filipegeniselli.roommanager.dto.RoomOccupancy;
import com.filipegeniselli.roommanager.service.RoomOccupancyService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@WebMvcTest(controllers = RoomOccupancyController.class)
public class RoomOccupancyControllerTests {

    @MockBean
    RoomOccupancyService roomOccupancyService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void returnsErrorWhenNoRoomsAvailable() throws Exception {
        mockMvc.perform(post("/optimize")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{ \"premiumRooms\": 0, \"economyRooms\": 0, \"potentialGuestsValues\": [] }"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorMessage", is("at least one tipe of room must have a capacity")));
    }

    @Test
    public void returnsErrorWhenNoPotentialGuestsSent() throws Exception {
        mockMvc.perform(post("/optimize")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{ \"premiumRooms\": 10, \"economyRooms\": 5, \"potentialGuestsValues\": [] }"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorMessage", is("at least one potential guest value must be informed")));
    }

    @Test
    public void returnsOkWhenValidBodyIsSent() throws Exception {
        given(roomOccupancyService.optimizeRooms(any(RoomOccupancy.class)))
        .willAnswer((invocation) -> new OptimizedRooms(null, null));

        mockMvc.perform(post("/optimize")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{ \"premiumRooms\": 10, \"economyRooms\": 5, \"potentialGuestsValues\": [30, 50, 100, 152] }"))
        .andExpect(status().isOk());
    }

}
