package com.daanooo.yeelightutils.device;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class YeelightDevice {
    private final String location;
    private final String model;

    public YeelightDevice(String hostName, String modelName) {
        this.location = hostName;
        this.model = modelName;
    }

    public static YeelightDevice fromMessage(String message) {

        Map<String, String> deviceData = Arrays.stream(message.split("\n"))
                .map(line -> line.split(":", 2))
                .filter(parts -> parts.length == 2)
                .collect(Collectors.toMap(
                        p -> p[0].trim().toLowerCase(),
                        p -> p[1].trim()
                ));

        return new YeelightDevice(deviceData.get("location"), deviceData.get("model"));
    }

    public String getLocation() {
        return location;
    }

    public String getModel() {
        return model;
    }
}
