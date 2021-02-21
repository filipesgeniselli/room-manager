package com.filipegeniselli.roommanager.controller;

import com.filipegeniselli.roommanager.dto.ErrorResponse;
import com.filipegeniselli.roommanager.dto.OptimizedRooms;
import com.filipegeniselli.roommanager.dto.RoomOccupancy;
import com.filipegeniselli.roommanager.service.RoomOccupancyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController("/")
public class RoomOccupancyController {

    @Autowired
    private RoomOccupancyService roomOccupancyService;

    @PostMapping("/optmize")
    public ResponseEntity<OptimizedRooms> postMethodName(@RequestBody RoomOccupancy rooms) {

        return ResponseEntity.ok(new OptimizedRooms(null, null));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse invalidResourceException(IllegalArgumentException e) {
        return new ErrorResponse(e.getMessage());
    }

}
