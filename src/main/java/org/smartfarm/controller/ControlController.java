package org.smartfarm.controller;

import org.smartfarm.udp.UDPSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/control")
public class ControlController {

    private final UDPSender udpSender;

    @Autowired
    public ControlController(UDPSender udpSender) {
        this.udpSender = udpSender;
    }

    @PostMapping
    public Map<String, String> controlSensor(@RequestBody Map<String, Object> request) {
        String location = (String) request.get("location");
        Double temperature = (Double) request.get("temperature");
        Double humidity = (Double) request.get("humidity");
        Double light = (Double) request.get("light");

        udpSender.sendCommand(location, temperature, humidity, light);
        return Map.of("status", "success");
    }
}
