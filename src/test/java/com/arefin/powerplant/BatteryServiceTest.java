package com.arefin.powerplant;

import com.arefin.powerplant.dto.BatteryStatisticsResponse;
import com.arefin.powerplant.exception.BatteryServiceException;
import com.arefin.powerplant.exception.InvalidBatteryException;
import com.arefin.powerplant.model.Battery;
import com.arefin.powerplant.repository.BatteryRepository;
import com.arefin.powerplant.service.BatteryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BatteryServiceTest {

    @Mock
    private BatteryRepository batteryRepository;

    @InjectMocks
    private BatteryService batteryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addBatteries_validBatteries_savedSuccessfully() {
        // Arrange
        List<Battery> batteries = new ArrayList<>();
        Battery battery1 = new Battery("Battery 1", "1234", 1000);
        Battery battery2 = new Battery("Battery 2", "5678", 2000);
        batteries.add(battery1);
        batteries.add(battery2);

        when(batteryRepository.saveAll(batteries)).thenReturn(batteries);

        // Act
        assertDoesNotThrow(() -> batteryService.addBatteries(batteries));

        // Assert
        verify(batteryRepository, times(1)).saveAll(batteries);
    }

    @Test
    void addBatteries_emptyList_throwsBatteryServiceException() {
        // Arrange
        List<Battery> batteries = new ArrayList<>();

        // Act and Assert
        BatteryServiceException exception = assertThrows(BatteryServiceException.class,
                () -> batteryService.addBatteries(batteries));

        assertEquals("No batteries provided for addition.", exception.getMessage());
        verify(batteryRepository, never()).saveAll(anyList());
    }

    @Test
    void addBatteries_invalidBatteries_throwsInvalidBatteryException() {
        // Arrange
        List<Battery> batteries = new ArrayList<>();
        Battery battery1 = new Battery("Battery 1", "1234", 100);
        Battery battery2 = new Battery("", "5678", -200); // Invalid battery
        batteries.add(battery1);
        batteries.add(battery2);

        // Act and Assert
        InvalidBatteryException exception = assertThrows(InvalidBatteryException.class,
                () -> batteryService.addBatteries(batteries));

        assertEquals("Invalid batteries detected.", exception.getMessage());
        assertEquals(1, exception.getInvalidBatteries().size());
        assertTrue(exception.getInvalidBatteries().contains(battery2));
        //verify(batteryRepository, never()).saveAll(anyList());
    }

    @Test
    void getBatteriesInPostcodeRange_validRange_returnsBatteryStatisticsResponse() {
        // Arrange
        String startPostcode = "1000";
        String endPostcode = "2000";

        Battery battery1 = new Battery("Battery 1", "1234", 100);
        Battery battery2 = new Battery("Battery 2", "1500", 200);
        Battery battery3 = new Battery("Battery 3", "1800", 300);

        List<Battery> batteries = new ArrayList<>();
        batteries.add(battery1);
        batteries.add(battery2);
        batteries.add(battery3);

        when(batteryRepository.findByPostcodeBetween(startPostcode, endPostcode)).thenReturn(batteries);

        // Act
        BatteryStatisticsResponse response = batteryService.getBatteriesInPostcodeRange(startPostcode, endPostcode);

        // Assert
        assertNotNull(response);
        assertEquals(3, response.getBatteries().size());
        assertEquals(600, response.getTotalWattCapacity());
        assertEquals(200, response.getAverageWattCapacity());
    }

    @Test
    void addBatteries_someInvalidBatteries_throwsInvalidBatteryException() {
        // Arrange
        List<Battery> batteries = new ArrayList<>();
        Battery battery1 = new Battery("Battery 1", "1234", 100);
        Battery battery2 = new Battery("", "5678", -200); // Invalid battery
        batteries.add(battery1);
        batteries.add(battery2);

        when(batteryRepository.existsByName(battery1.getName())).thenReturn(false);
        when(batteryRepository.existsByName(battery2.getName())).thenReturn(false);

        // Act and Assert
        InvalidBatteryException exception = assertThrows(InvalidBatteryException.class,
                () -> batteryService.addBatteries(batteries));
        assertEquals("Invalid batteries detected.", exception.getMessage());
        assertEquals(List.of(battery2), exception.getInvalidBatteries());

        //verifyNoMoreInteractions(batteryRepository);
    }


    // Add more test cases for other methods if needed
}

