package org.smartfarm.controller;

import org.smartfarm.udp.UDPServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.json.JSONObject;

import java.util.SortedMap;

@RestController
@RequestMapping("/sensors")
public class SensorDataController {

    private final UDPServer udpServer;

    @Autowired
    public SensorDataController(UDPServer udpServer) {
        this.udpServer = udpServer;
    }

    @GetMapping
    public SortedMap<String, String> getSensors() {
        SortedMap<String, String> sensorData = udpServer.getSensorData();
        return sensorData;
    }
}
