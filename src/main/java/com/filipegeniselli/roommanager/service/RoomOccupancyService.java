package com.filipegeniselli.roommanager.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        potentialGuestValues.sort((v1, v2) -> v2.compareTo(v1));
        
        List<BigDecimal> premiumUsage = new ArrayList<>();
        List<BigDecimal> economyUsage = new ArrayList<>();

        addUsagePremium(premiumUsage, potentialGuestValues, roomOccupancy.getPremiumRooms());
        addUsageEconomy(economyUsage, potentialGuestValues, roomOccupancy.getEconomyRooms());
        
        if(!potentialGuestValues.isEmpty() && 
                premiumUsage.size() < roomOccupancy.getPremiumRooms()) { 
            performUpgrade(economyUsage, premiumUsage, roomOccupancy);
        }

        addUsageEconomy(economyUsage, potentialGuestValues, roomOccupancy.getEconomyRooms() - economyUsage.size());

        return new OptimizedRooms(
            new RoomUsage(premiumUsage.size(), premiumUsage.stream().reduce(BigDecimal.ZERO, BigDecimal::add)), 
            new RoomUsage(economyUsage.size(), economyUsage.stream().reduce(BigDecimal.ZERO, BigDecimal::add)));
    }

    private void addUsagePremium(List<BigDecimal> usage, List<BigDecimal> potentialGuestValues, Integer availableRooms) {
        usage.addAll(potentialGuestValues
            .stream()
            .filter(value -> value.compareTo(maximumAcceptedToEconomy) >= 0)
            .limit(availableRooms)
            .collect(Collectors.toList()));
        potentialGuestValues.removeAll(usage);
    }

    private void addUsageEconomy(List<BigDecimal> usage, List<BigDecimal> potentialGuestValues, Integer availableRooms) {
        usage.addAll(potentialGuestValues
            .stream()
            .filter(value -> value.compareTo(maximumAcceptedToEconomy) < 0)
            .limit(availableRooms)
            .collect(Collectors.toList()));
        potentialGuestValues.removeAll(usage);
    }

    private void performUpgrade(List<BigDecimal> economyUsage, List<BigDecimal> premiumUsage, RoomOccupancy roomOccupancy) {
        for(int i = economyUsage.size() - 1; i >= 0; i--) {
            if(premiumUsage.size() < roomOccupancy.getPremiumRooms()) {
                premiumUsage.add(economyUsage.remove(i));
            } else {
                break;
            }
        }
    }

}
