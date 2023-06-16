package com.arefin.powerplant;

import com.arefin.powerplant.controller.BatteryController;
import com.arefin.powerplant.dto.AddBatteryResponse;
import com.arefin.powerplant.dto.BatteryStatisticsResponse;
import com.arefin.powerplant.exception.BatteryServiceException;
import com.arefin.powerplant.exception.InvalidBatteryException;
import com.arefin.powerplant.model.Battery;
import com.arefin.powerplant.service.BatteryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.el.*;
//import javax.validation.Validator;
//import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BatteryControllerTest {

    @Mock
    private BatteryService batteryService;

    @InjectMocks
    private BatteryController batteryController;

    //private Validator validator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
//        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//        validator = factory.getValidator();

//        MockitoAnnotations.openMocks(this);
//        batteryController = new BatteryController(batteryService);
//        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void addBatteries_validBatteries_returnsOkResponse() {
        // Arrange
        List<Battery> batteries = new ArrayList<>();
        Battery battery1 = new Battery("Battery 1", "1234", 100);
        Battery battery2 = new Battery("Battery 2", "5678", 200);
        batteries.add(battery1);
        batteries.add(battery2);

        doNothing().when(batteryService).addBatteries(batteries);

        // Act
        ResponseEntity<AddBatteryResponse> response = batteryController.addBatteries(batteries);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Batteries added successfully.", response.getBody().getMessage());
        assertNull(response.getBody().getInvalidBatteries());
    }

    @Test
    void addBatteries_invalidBatteries_returnsBadRequestResponse() {
        // Arrange
        List<Battery> batteries = new ArrayList<>();
        Battery battery1 = new Battery("Battery 1", "1234", 100);
        Battery battery2 = new Battery("", "5678", -200); // Invalid battery
        batteries.add(battery1);
        batteries.add(battery2);

        List<Battery> invalidBatteries = Collections.singletonList(battery2);

        InvalidBatteryException exception = new InvalidBatteryException("Some batteries are invalid.", invalidBatteries);
        doThrow(exception).when(batteryService).addBatteries(batteries);

        // Act
        ResponseEntity<AddBatteryResponse> response = batteryController.addBatteries(batteries);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Some batteries are invalid.", response.getBody().getMessage());
        assertEquals(invalidBatteries, response.getBody().getInvalidBatteries());
    }


    @Test
    void addBatteries_serviceError_returnsInternalServerErrorResponse() {
        // Arrange
        List<Battery> batteries = new ArrayList<>();
        Battery battery1 = new Battery("Battery 1", "1234", 100);
        Battery battery2 = new Battery("Battery 2", "5678", 200);
        batteries.add(battery1);
        batteries.add(battery2);

        BatteryServiceException exception = new BatteryServiceException("Failed to add batteries.");
        doThrow(exception).when(batteryService).addBatteries(batteries);
        // Act
        ResponseEntity<AddBatteryResponse> response = batteryController.addBatteries(batteries);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Failed to add batteries.", response.getBody().getMessage());
        assertNull(response.getBody().getInvalidBatteries());
    }

    @Test
    void getBatteriesInPostcodeRange_validRange_returnsOkResponse() {
        // Arrange
        String startPostcode = "1000";
        String endPostcode = "2000";

        Battery battery1 = new Battery("Battery 1", "1234", 100);
        Battery battery2 = new Battery("Battery 2", "1500", 200);
        Battery battery3 = new Battery("Battery 3", "1800", 300);

        BatteryStatisticsResponse statisticsResponse = new BatteryStatisticsResponse();
        statisticsResponse.setBatteries(List.of("Battery 1", "Battery 2", "Battery 3"));
        statisticsResponse.setTotalWattCapacity(600);
        statisticsResponse.setAverageWattCapacity(200);

        when(batteryService.getBatteriesInPostcodeRange(startPostcode, endPostcode)).thenReturn(statisticsResponse);

        // Act
        ResponseEntity<BatteryStatisticsResponse> response = batteryController.getBatteriesInPostcodeRange(startPostcode, endPostcode);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(statisticsResponse, response.getBody());
    }

    // Add more test cases for other methods if needed
}
