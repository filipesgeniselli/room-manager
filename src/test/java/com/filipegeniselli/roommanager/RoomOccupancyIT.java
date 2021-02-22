package com.filipegeniselli.roommanager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest(classes = RoomManagerApplication.class)
@WebAppConfiguration
public class RoomOccupancyIT {
    
    @Autowired
    WebApplicationContext webApplicationContext;

    MockMvc mockMvc;

    @BeforeEach
    public void setUpMockMvc() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void scenario_3Premium3Economy() throws Exception {

        mockMvc.perform(post("/optimize")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"premiumRooms\": 3, \"economyRooms\": 3, \"potentialGuestsValues\": [23, 45, 155, 374, 22, 99, 100, 101, 115, 209] }"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.premiumUsage.roomTotalUsage", is(3)))
                .andExpect(jsonPath("$.premiumUsage.roomTotalMoney", is(738)))
                .andExpect(jsonPath("$.economyUsage.roomTotalUsage", is(3)))
                .andExpect(jsonPath("$.economyUsage.roomTotalMoney", is(167)));
    }

    @Test
    public void scenario_7Premium5Economy() throws Exception {

        mockMvc.perform(post("/optimize")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"premiumRooms\": 7, \"economyRooms\": 5, \"potentialGuestsValues\": [23, 45, 155, 374, 22, 99, 100, 101, 115, 209] }"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.premiumUsage.roomTotalUsage", is(6)))
                .andExpect(jsonPath("$.premiumUsage.roomTotalMoney", is(1054)))
                .andExpect(jsonPath("$.economyUsage.roomTotalUsage", is(4)))
                .andExpect(jsonPath("$.economyUsage.roomTotalMoney", is(189)));
    }

    @Test
    public void scenario_2Premium7Economy() throws Exception {

        mockMvc.perform(post("/optimize")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"premiumRooms\": 2, \"economyRooms\": 7, \"potentialGuestsValues\": [23, 45, 155, 374, 22, 99, 100, 101, 115, 209] }"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.premiumUsage.roomTotalUsage", is(2)))
                .andExpect(jsonPath("$.premiumUsage.roomTotalMoney", is(583)))
                .andExpect(jsonPath("$.economyUsage.roomTotalUsage", is(4)))
                .andExpect(jsonPath("$.economyUsage.roomTotalMoney", is(189)));
    }

    @Test
    public void scenario_7Premium1Economy() throws Exception {

        mockMvc.perform(post("/optimize")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"premiumRooms\": 7, \"economyRooms\": 1, \"potentialGuestsValues\": [23, 45, 155, 374, 22, 99, 100, 101, 115, 209] }"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.premiumUsage.roomTotalUsage", is(7)))
                .andExpect(jsonPath("$.premiumUsage.roomTotalMoney", is(1153)))
                .andExpect(jsonPath("$.economyUsage.roomTotalUsage", is(1)))
                .andExpect(jsonPath("$.economyUsage.roomTotalMoney", is(45)));
    }
}
