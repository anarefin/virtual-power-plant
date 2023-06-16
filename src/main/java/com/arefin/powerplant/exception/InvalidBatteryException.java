package com.arefin.powerplant.exception;

import com.arefin.powerplant.model.Battery;

import java.util.List;

public class InvalidBatteryException extends RuntimeException {
    private final List<Battery> invalidBatteries;

    public InvalidBatteryException(String message, List<Battery> invalidBatteries) {
        super(message);
        this.invalidBatteries = invalidBatteries;
    }

    public List<Battery> getInvalidBatteries() {
        return invalidBatteries;
    }
}

