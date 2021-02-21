package com.filipegeniselli.roommanager.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
        potentialGuestValues.sort((v1, v2) -> v1.compareTo(v2));
        
        List<BigDecimal> premiumUsage = new ArrayList<>();
        List<BigDecimal> economyUsage = new ArrayList<>();

        for(int i = potentialGuestValues.size() - 1; i >= 0; i--) {
            if(premiumUsage.size() < roomOccupancy.getPremiumRooms() 
               && potentialGuestValues.get(i).compareTo(maximumAcceptedToEconomy) >= 0){
                premiumUsage.add(potentialGuestValues.remove(i));
            } else if(economyUsage.size() < roomOccupancy.getEconomyRooms() 
               && potentialGuestValues.get(i).compareTo(maximumAcceptedToEconomy) < 0){
                economyUsage.add(potentialGuestValues.remove(i));
            }
        }

        if(!potentialGuestValues.isEmpty() 
           && premiumUsage.size() < roomOccupancy.getPremiumRooms()) { 
            for(int i = economyUsage.size() - 1; i >= 0; i--) {
                if(premiumUsage.size() < roomOccupancy.getPremiumRooms()) {
                    premiumUsage.add(economyUsage.remove(i));
                } else {
                    break;
                }
            }
        }

        for(int i = potentialGuestValues.size() - 1; i >= 0; i--) {
            if(economyUsage.size() < roomOccupancy.getEconomyRooms() 
               && potentialGuestValues.get(i).compareTo(maximumAcceptedToEconomy) < 0){
                economyUsage.add(potentialGuestValues.remove(i));
            }
        }

        return new OptimizedRooms(
            new RoomUsage(premiumUsage.size(), premiumUsage.stream().reduce(BigDecimal.ZERO, BigDecimal::add)), 
            new RoomUsage(economyUsage.size(), economyUsage.stream().reduce(BigDecimal.ZERO, BigDecimal::add)));
    }

}
