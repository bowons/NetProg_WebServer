package org.smartfarm;

import org.smartfarm.udp.UDPServer;
import org.smartfarm.websocket.WebSocketHandler;
import org.json.JSONObject;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SmartFarmServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartFarmServerApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(UDPServer udpServer, WebSocketHandler webSocketHandler) {
        return args -> {
            udpServer.start(12345, sortedMap -> {
                for (JSONObject jsonObject : sortedMap.values()) {
                    System.out.println("Received UDP message: " + jsonObject);
                    webSocketHandler.broadcast(jsonObject.toString());
                }
            });
        };
    }
}
