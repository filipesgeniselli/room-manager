package com.filipegeniselli.roommanager.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OptimizedRooms {

    private final RoomUsage premiumUsage;
    private final RoomUsage economyUsage;
    
}
