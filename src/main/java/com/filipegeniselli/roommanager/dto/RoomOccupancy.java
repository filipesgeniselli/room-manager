package com.filipegeniselli.roommanager.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.beans.ConstructorProperties;
import java.math.BigDecimal;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class RoomOccupancy {
    
    @Getter
    private final Integer premiumRooms;

    @Getter
    private final Integer economyRooms;

    private BigDecimal[] potentialGuestsValues;

    @ConstructorProperties({"premiumRooms", "economyRooms", "potentialGuestsValues"})
    public RoomOccupancy(Integer premiumRooms, Integer economyRooms, BigDecimal[] potentialGuestsValues) {
        if(premiumRooms <= 0 && economyRooms <= 0) {
            throw new IllegalArgumentException("at least one tipe of room must have a capacity");
        }
        this.premiumRooms = premiumRooms;
        this.economyRooms = economyRooms;
        
        if(potentialGuestsValues == null || potentialGuestsValues.length == 0) {
            throw new IllegalArgumentException("at least one potential guest value must be informed");
        }
        this.potentialGuestsValues = potentialGuestsValues;
    }

    public List<BigDecimal> getPotentialGuestsValues() {
        return new ArrayList<>(Arrays.asList(potentialGuestsValues));
    }
}
