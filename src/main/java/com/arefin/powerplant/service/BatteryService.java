package com.arefin.powerplant.service;

import com.arefin.powerplant.dto.BatteryStatisticsResponse;
import com.arefin.powerplant.exception.BatteryServiceException;
import com.arefin.powerplant.exception.InvalidBatteryException;
import com.arefin.powerplant.model.Battery;
import com.arefin.powerplant.repository.BatteryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BatteryService {
    private static final String POSTCODE_REGEX = "\\d{4}";
    private final BatteryRepository batteryRepository;

    @Autowired
    public BatteryService(BatteryRepository batteryRepository) {
        this.batteryRepository = batteryRepository;
    }

    public void addBatteries(List<Battery> batteries) {
        if (batteries.isEmpty()) {
            throw new BatteryServiceException("No batteries provided for addition.");
        }

        List<Battery> validBatteries = new ArrayList<>();
        List<Battery> invalidBatteries = new ArrayList<>();

        for (Battery battery : batteries) {
            if (isValidBattery(battery)) {
                validBatteries.add(battery);
            } else {
                invalidBatteries.add(battery);
            }
        }

        if (!validBatteries.isEmpty()) {
            Iterable<Battery> savedBatteries = batteryRepository.saveAll(validBatteries);
            List<Battery> savedBatteryList = StreamSupport.stream(savedBatteries.spliterator(), false)
                    .collect(Collectors.toList());
            validBatteries.clear();
            validBatteries.addAll(savedBatteryList);
        }

        if (!invalidBatteries.isEmpty()) {
            throw new InvalidBatteryException("Invalid batteries detected.", invalidBatteries);
        }
    }


    public BatteryStatisticsResponse getBatteriesInPostcodeRange(String start, String end) {
        List<Battery> batteries = batteryRepository.findByPostcodeBetween(start, end);
        Collections.sort(batteries, (b1, b2) -> b1.getName().compareToIgnoreCase(b2.getName()));

        int totalWattCapacity = batteries.stream().mapToInt(Battery::getCapacity).sum();
        double averageWattCapacity = batteries.stream().mapToInt(Battery::getCapacity).average().orElse(0);
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        double formattedAverageWattCapacity = Double.parseDouble(decimalFormat.format(averageWattCapacity));

        BatteryStatisticsResponse batteryStatisticsResponse = new BatteryStatisticsResponse();
        batteryStatisticsResponse.setBatteries(batteries.stream().map(Battery::getName).collect(Collectors.toList()));
        batteryStatisticsResponse.setTotalWattCapacity(totalWattCapacity);
        batteryStatisticsResponse.setAverageWattCapacity(formattedAverageWattCapacity);
        return batteryStatisticsResponse;
    }

    private boolean isValidPostcode(String postcode) {
        return Pattern.matches(POSTCODE_REGEX, postcode);
    }

    private boolean isValidBattery(Battery battery) {
        if (battery.getName() == null || battery.getName().isEmpty()) {
            return false;
        }
        if (batteryRepository.existsByName(battery.getName())) {
            return false;
        }
        if (battery.getPostcode() == null || !isValidPostcode(battery.getPostcode())) {
            return false;
        }
        if (battery.getCapacity() <= 0) {
            return false;
        }
        return true;
    }
}

