package com.arefin.powerplant.repository;

import com.arefin.powerplant.model.Battery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BatteryRepository extends JpaRepository<Battery, Long> {
    List<Battery> findByPostcodeBetween(String startPostcode, String endPostcode);
    boolean existsByName(String name);
}
