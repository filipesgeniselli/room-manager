package com.filipegeniselli.roommanager.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RoomUsage {
    
    private final Integer roomTotalUsage;
    private final BigDecimal roomTotalMoney;

}
