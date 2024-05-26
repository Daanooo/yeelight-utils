package com.daanooo.yeelightutils;

import com.daanooo.yeelightutils.discovery.SsdpDiscovery;

public class YeelightUtils {
    public static void main(String[] args) throws InterruptedException {
        SsdpDiscovery discovery = new SsdpDiscovery("239.255.255.250", 1982, 1000);
        Thread thread = new Thread(discovery);
        thread.start();
        thread.join();

        System.out.println(discovery.getDevices());
    }
}