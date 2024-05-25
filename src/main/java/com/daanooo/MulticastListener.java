package com.daanooo;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class MulticastListener implements Runnable {
    @Override
    public void run() {
        int port = 1982;

        try (MulticastSocket socket = new MulticastSocket(port)) {
            InetSocketAddress group = new InetSocketAddress("239.255.255.250", 1982);
            NetworkInterface netIf = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());

            socket.joinGroup(group, netIf);

            byte[] msg = new byte[1024];
            DatagramPacket packet = new DatagramPacket(msg, msg.length);

            boolean running = true;
            while (running) {
                socket.receive(packet);
                System.out.println(new String(msg, StandardCharsets.UTF_8));
                packet.setLength(1024);

                if (Thread.interrupted()) running = false;
            }

            System.out.println("Thread exited");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
