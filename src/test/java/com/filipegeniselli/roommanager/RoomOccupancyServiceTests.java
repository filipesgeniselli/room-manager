package com.filipegeniselli.roommanager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.filipegeniselli.roommanager.dto.OptimizedRooms;
import com.filipegeniselli.roommanager.dto.RoomOccupancy;
import com.filipegeniselli.roommanager.service.RoomOccupancyService;

import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Service;

@Service
public class RoomOccupancyServiceTests {
    
    RoomOccupancyService roomOccupancyService = new RoomOccupancyService(new BigDecimal(100));

    private final BigDecimal[] standardPotentialGuestValues = new BigDecimal[] {
        new BigDecimal(23),
        new BigDecimal(45),
        new BigDecimal(155),
        new BigDecimal(374),
        new BigDecimal(22),
        new BigDecimal(99),
        new BigDecimal(100),
        new BigDecimal(101),
        new BigDecimal(115),
        new BigDecimal(209),
    };

    private final BigDecimal[] equalsPotentialGuestValues = new BigDecimal[] {
        new BigDecimal(200),
        new BigDecimal(200),
        new BigDecimal(200),
        new BigDecimal(200),
        new BigDecimal(200),
        new BigDecimal(200),
        new BigDecimal(200),
        new BigDecimal(200),
        new BigDecimal(200),
        new BigDecimal(200),
    };

    private final BigDecimal[] someEqualsPotentialGuestValues = new BigDecimal[] {
        new BigDecimal(50),
        new BigDecimal(50),
        new BigDecimal(95),
        new BigDecimal(310),
        new BigDecimal(95),
        new BigDecimal(420),
        new BigDecimal(355),
        new BigDecimal(200),
        new BigDecimal(200),
        new BigDecimal(200),
    };

    @Test
    public void scenario_3Premium3Economy() {
        OptimizedRooms result = roomOccupancyService.optimizeRooms(new RoomOccupancy(3, 3, standardPotentialGuestValues));
        assertNotNull(result);
        //Room usage
        assertEquals(3, result.getPremiumUsage().getRoomTotalUsage());
        assertEquals(3, result.getEconomyUsage().getRoomTotalUsage());
        //Total money
        assertEquals(new BigDecimal(738), result.getPremiumUsage().getRoomTotalMoney());
        assertEquals(new BigDecimal(167), result.getEconomyUsage().getRoomTotalMoney());
    }

    @Test
    public void scenario_7Premium5Economy() {
        OptimizedRooms result = roomOccupancyService.optimizeRooms(new RoomOccupancy(7, 5, standardPotentialGuestValues));
        assertNotNull(result);
        //Room usage
        assertEquals(6, result.getPremiumUsage().getRoomTotalUsage());
        assertEquals(4, result.getEconomyUsage().getRoomTotalUsage());
        //Total money
        assertEquals(new BigDecimal(1054), result.getPremiumUsage().getRoomTotalMoney());
        assertEquals(new BigDecimal(189), result.getEconomyUsage().getRoomTotalMoney());
    }

    @Test
    public void scenario_2Premium7Economy() {
        OptimizedRooms result = roomOccupancyService.optimizeRooms(new RoomOccupancy(2, 7, standardPotentialGuestValues));
        assertNotNull(result);
        //Room usage
        assertEquals(2, result.getPremiumUsage().getRoomTotalUsage());
        assertEquals(4, result.getEconomyUsage().getRoomTotalUsage());
        //Total money
        assertEquals(new BigDecimal(583), result.getPremiumUsage().getRoomTotalMoney());
        assertEquals(new BigDecimal(189), result.getEconomyUsage().getRoomTotalMoney());
    }

    @Test
    public void scenario_7Premium1Economy() {
        OptimizedRooms result = roomOccupancyService.optimizeRooms(new RoomOccupancy(7, 1, standardPotentialGuestValues));
        assertNotNull(result);
        //Room usage
        assertEquals(7, result.getPremiumUsage().getRoomTotalUsage());
        assertEquals(1, result.getEconomyUsage().getRoomTotalUsage());
        //Total money
        assertEquals(new BigDecimal(1153), result.getPremiumUsage().getRoomTotalMoney());
        assertEquals(new BigDecimal(45), result.getEconomyUsage().getRoomTotalMoney());
    }

    @Test
    public void scenario_4Premium5Economy() {
        OptimizedRooms result = roomOccupancyService.optimizeRooms(new RoomOccupancy(4, 5, someEqualsPotentialGuestValues));
        assertNotNull(result);
        //Room usage
        assertEquals(4, result.getPremiumUsage().getRoomTotalUsage());
        assertEquals(4, result.getEconomyUsage().getRoomTotalUsage());
        //Total money
        assertEquals(new BigDecimal(1285), result.getPremiumUsage().getRoomTotalMoney());
        assertEquals(new BigDecimal(290), result.getEconomyUsage().getRoomTotalMoney());
    }

    @Test
    public void scenario_3Premium2Economy() {
        OptimizedRooms result = roomOccupancyService.optimizeRooms(new RoomOccupancy(3, 2, equalsPotentialGuestValues));
        assertNotNull(result);
        //Room usage
        assertEquals(3, result.getPremiumUsage().getRoomTotalUsage());
        assertEquals(0, result.getEconomyUsage().getRoomTotalUsage());
        //Total money
        assertEquals(new BigDecimal(600), result.getPremiumUsage().getRoomTotalMoney());
        assertEquals(new BigDecimal(0), result.getEconomyUsage().getRoomTotalMoney());
    }

    @Test
    public void scenario_longArray() {
        List<BigDecimal> randomValues = new ArrayList<>();
        Random random = new Random(System.currentTimeMillis());

        for(int i = 0; i < 10000000; i++){
            randomValues.add(new BigDecimal(random.nextInt(400)));
        }

        OptimizedRooms result = roomOccupancyService.optimizeRooms(new RoomOccupancy(15000, 12000, randomValues.toArray(new BigDecimal[0])));
        assertNotNull(result);
    }

}
