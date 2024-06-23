package org.smartfarm.udp;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Service
public class UDPServer {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final SortedMap<String, JSONObject> sensorData = Collections.synchronizedSortedMap(new TreeMap<>());

    public void start(int port, Consumer<SortedMap<String, JSONObject>> onMessage) {
        executorService.submit(() -> {
            try (DatagramSocket socket = new DatagramSocket(port)) {
                byte[] buffer = new byte[1024];
                while (true) {
                    SortedMap<String, JSONObject> newSensorData = new TreeMap<>();
                    for (int i = 0; i < 5; i++) {
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                        socket.receive(packet);
                        String msg = new String(packet.getData(), 0, packet.getLength());
                        JSONObject jsonObject = new JSONObject(msg);
                        newSensorData.put(jsonObject.getString("location"), jsonObject);
                    }
                    synchronized (sensorData) {
                        sensorData.clear();
                        sensorData.putAll(newSensorData);
                    }
                    onMessage.accept(sensorData);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void stop() {
        executorService.shutdownNow();
    }

    public SortedMap<String, String> getSensorData() {
        synchronized (sensorData) {
            SortedMap<String, String> getStringJSON = new TreeMap<>();
            for(JSONObject sensorData : sensorData.values()) {
                getStringJSON.put(sensorData.getString("location"), sensorData.toString());
            }
            return getStringJSON;
        }
    }
}
