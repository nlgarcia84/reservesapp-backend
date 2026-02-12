package com.reservas.reservas.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RoomService {


    public List<Map<String, Object>> getRooms(){
        return List.of(
                Map.of(
                        "id",1,
                        "name", "Sala A",
                        "capacity", 10
                ),
                Map.of(
                        "id", 2,
                        "name", "Sala B",
                        "capacity", 20
                ),
                Map.of(
                        "id", 3,
                        "name", "Sala C",
                        "capacity", 30
                )
        );
    }


    public Map<String, Object> createRoom(@RequestBody Map<String,Object> room){
        System.out.println(room);
        return room;
    }
}
