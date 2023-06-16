package com.arefin.powerplant.dto;

import com.arefin.powerplant.model.Battery;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class AddBatteryResponse {
    private String message;
    private List<Battery> invalidBatteries;
}

