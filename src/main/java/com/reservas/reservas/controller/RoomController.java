package com.reservas.reservas.controller;

import com.reservas.reservas.service.RoomService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;


@RestController
@RequestMapping("/rooms")
@CrossOrigin(origins = "http://localhost:5173")
public class RoomController {
    private RoomService roomService;

    //Constructor con Servicio como parámetro
    public RoomController (RoomService roomService){
        this.roomService =roomService;
    }

    @GetMapping
    public List <Map<String, Object>> getRooms(){
      return roomService.getRooms();
    }

    @PostMapping
    public Map<String, Object> createRooms(@RequestBody Map<String,Object> room){
        return roomService.createRoom(room);
    }

}
