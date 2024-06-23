package org.smartfarm.udp;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

@Component
public class UDPSender {
    private static final String SENSOR_HOST = "127.0.0.1";
    private static final int SENSOR_PORT = 8888;

    public void sendCommand(String location, Double temperature, Double humidity, Double light) {
        try(DatagramSocket socket = new DatagramSocket()) {
            JSONObject json = new JSONObject();
            json.put("location", location);
            if (temperature != null) {
                json.put("temperature", temperature);
            }
            if (humidity != null) {
                json.put("humidity", humidity);

            }
            if (light != null) {
                json.put("light", light);
            }

            byte[] buffer = json.toString().getBytes();
            InetAddress address = InetAddress.getByName(SENSOR_HOST);
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, SENSOR_PORT);
            socket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
