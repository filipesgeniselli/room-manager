package com.filipegeniselli.roommanager.controller;

import com.filipegeniselli.roommanager.dto.ErrorResponse;
import com.filipegeniselli.roommanager.dto.OptimizedRooms;
import com.filipegeniselli.roommanager.dto.RoomOccupancy;
import com.filipegeniselli.roommanager.service.RoomOccupancyService;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController("/")
public class RoomOccupancyController {

    @Autowired
    private RoomOccupancyService roomOccupancyService;

    @PostMapping(value = "/optimize", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public OptimizedRooms postMethodName(@RequestBody RoomOccupancy rooms) {
        return roomOccupancyService.optimizeRooms(rooms);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse invalidResourceException(IllegalArgumentException e) {
        return new ErrorResponse(e.getMessage());
    }

}
