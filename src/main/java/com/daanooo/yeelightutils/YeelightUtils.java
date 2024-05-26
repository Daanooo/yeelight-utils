package com.daanooo.yeelightutils;

import com.daanooo.yeelightutils.discovery.SsdpDiscovery;

public class YeelightUtils {
    public static void main(String[] args) {
        new Thread(new SsdpDiscovery("239.255.255.250", 1982, 1000)).start();
    }
}