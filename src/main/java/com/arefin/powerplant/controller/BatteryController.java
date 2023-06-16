package com.arefin.powerplant.controller;

import com.arefin.powerplant.dto.AddBatteryResponse;
import com.arefin.powerplant.dto.BatteryStatisticsResponse;
import com.arefin.powerplant.exception.BatteryServiceException;
import com.arefin.powerplant.exception.InvalidBatteryException;
import com.arefin.powerplant.model.Battery;
import com.arefin.powerplant.service.BatteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
public class BatteryController {

    private final BatteryService batteryService;

    @Autowired
    public BatteryController(BatteryService batteryService) {
        this.batteryService = batteryService;
    }

    @PostMapping("/batteries")
    public ResponseEntity<AddBatteryResponse> addBatteries(@RequestBody @Valid List<Battery> batteries) {
        try {
            batteryService.addBatteries(batteries);
            return ResponseEntity.ok(new AddBatteryResponse("Batteries added successfully.", null));
        } catch (InvalidBatteryException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new AddBatteryResponse("Some batteries are invalid.", e.getInvalidBatteries()));
        } catch (BatteryServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AddBatteryResponse("Failed to add batteries.", null));
        }
    }

    @GetMapping("/batteries/statistics")
    public ResponseEntity<BatteryStatisticsResponse> getBatteriesInPostcodeRange(
            @RequestParam("start") @Pattern(regexp = "\\d{4}", message = "Invalid start postcode") String start,
            @RequestParam("end") @Pattern(regexp = "\\d{4}", message = "Invalid end postcode") String end) {
        try {
            BatteryStatisticsResponse response = batteryService.getBatteriesInPostcodeRange(start, end);
            return ResponseEntity.ok(response);
        } catch (BatteryServiceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body((BatteryStatisticsResponse) Collections.singletonMap("error", e.getMessage()));
        }
    }
}
