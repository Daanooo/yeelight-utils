package com.daanooo;

public class YeelightUtils {
    public static void main(String[] args) {
        Thread listener = new Thread(new MulticastListener());
        listener.start();

        System.out.println("Something");
    }
}