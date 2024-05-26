package com.daanooo.yeelightutils.discovery;

import com.daanooo.yeelightutils.device.YeelightDevice;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class SsdpDiscovery implements Runnable {
    private final String hostName;
    private final int port;
    private final int timeout;
    private volatile Map<String, YeelightDevice> devices = new HashMap<>();

    public SsdpDiscovery(String hostName, int port, int timeout) {
        this.hostName = hostName;
        this.port = port;
        this.timeout = timeout;
    }

    public SsdpDiscovery(String hostName, int port) {
        this(hostName, port, 5000);
    }

    public Map<String, YeelightDevice> doDiscovery() throws InterruptedException {
        Thread thread = new Thread(this);
        thread.start();
        thread.join();

        return this.devices;
    }

    @Override
    public void run() {
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(timeout);

            InetSocketAddress addr = new InetSocketAddress(InetAddress.getByName(this.hostName), this.port);

            String msg = """
                    M-SEARCH * HTTP/1.1\r
                    HOST: 239.255.255.250:1982\r
                    MAN: "ssdp:discover"\r
                    ST: wifi_bulb\r
                    """;

            byte[] msgBytes = msg.getBytes();
            DatagramPacket sendingPacket = new DatagramPacket(msgBytes, msgBytes.length, addr);

            socket.send(sendingPacket);

            byte[] responseBytes = new byte[1024];
            DatagramPacket receivingPacket = new DatagramPacket(responseBytes, responseBytes.length);

            while (!Thread.currentThread().isInterrupted()) {
                socket.receive(receivingPacket);

                // TODO write devices to a file or DB here so the thread is fully independent
                YeelightDevice device = YeelightDevice.fromMessage(new String(responseBytes));
                this.devices.put(device.getLocation(), device);

                receivingPacket.setLength(1024);
            }
        } catch (SocketTimeoutException e) {
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, YeelightDevice> getDevices() {
        return this.devices;
    }
}
