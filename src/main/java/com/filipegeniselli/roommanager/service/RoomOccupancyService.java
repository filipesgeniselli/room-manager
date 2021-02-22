package com.filipegeniselli.roommanager.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.filipegeniselli.roommanager.dto.OptimizedRooms;
import com.filipegeniselli.roommanager.dto.RoomOccupancy;
import com.filipegeniselli.roommanager.dto.RoomUsage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RoomOccupancyService {
    
    private final BigDecimal maximumAcceptedToEconomy;

    @Autowired
    public RoomOccupancyService(@Value("${app.maximum-accepted-economy}") BigDecimal maximumAcceptedToEconomy) {
        this.maximumAcceptedToEconomy = maximumAcceptedToEconomy;
    }

    public OptimizedRooms optimizeRooms(RoomOccupancy roomOccupancy) {
        List<BigDecimal> potentialGuestValues = roomOccupancy.getPotentialGuestsValues();
        Collections.sort(potentialGuestValues, Collections.reverseOrder());
        
        List<BigDecimal> premiumUsage = createPremiumUsage(potentialGuestValues, roomOccupancy.getPremiumRooms());
        List<BigDecimal> economyUsage = createEconomyUsage(potentialGuestValues, roomOccupancy.getEconomyRooms(),
            premiumUsage, roomOccupancy.getPremiumRooms() - premiumUsage.size());

        return new OptimizedRooms(
            createRoomUsage(premiumUsage), 
            createRoomUsage(economyUsage));
    }

    private List<BigDecimal> createPremiumUsage(List<BigDecimal> potentialGuests, Integer availabeRooms) {
        return potentialGuests
            .stream()
            .filter(value -> value.compareTo(maximumAcceptedToEconomy) >= 0)
            .limit(availabeRooms)
            .collect(Collectors.toList());
    }

    private List<BigDecimal> createEconomyUsage(List<BigDecimal> potentialGuestValues, Integer availabeEconomyRooms,
        List<BigDecimal> premiumUsage, Integer availablePremiumRooms) {

        List<BigDecimal> potentialEconomyGuests = potentialGuestValues
            .stream()
            .filter(value -> value.compareTo(maximumAcceptedToEconomy) < 0)
            .collect(Collectors.toList());
        List<BigDecimal> economyUsage = new ArrayList<>();
        addUsageEconomy(economyUsage, potentialEconomyGuests.stream(), availabeEconomyRooms);
        
        potentialEconomyGuests = potentialEconomyGuests.stream().skip(economyUsage.size()).collect(Collectors.toList());

        if(!potentialEconomyGuests.isEmpty() && availablePremiumRooms > 0) { 
            performUpgrade(economyUsage, premiumUsage, availablePremiumRooms);

            addUsageEconomy(economyUsage, 
                potentialEconomyGuests.stream().skip(economyUsage.size()), 
                availabeEconomyRooms - economyUsage.size());
        }

        return economyUsage;
    }

    private void addUsageEconomy(List<BigDecimal> usage, Stream<BigDecimal> potentialGuestValues, Integer availableRooms) {
        if(availableRooms < 1){
            return;
        }

        usage.addAll(potentialGuestValues
            .limit(availableRooms)
            .collect(Collectors.toList()));
    }

    private void performUpgrade(List<BigDecimal> economyUsage, List<BigDecimal> premiumUsage, Integer availablePremiumRooms) {
        for(int i = economyUsage.size() - 1; i >= 0; i--) {
            if(availablePremiumRooms > 0) {
                premiumUsage.add(economyUsage.remove(i));
                availablePremiumRooms -= 1;
            } else {
                break;
            }
        }
    }

    private RoomUsage createRoomUsage(List<BigDecimal> roomUsage) {
        return new RoomUsage(roomUsage.size(), roomUsage.stream().reduce(BigDecimal.ZERO, BigDecimal::add));
    }

}
