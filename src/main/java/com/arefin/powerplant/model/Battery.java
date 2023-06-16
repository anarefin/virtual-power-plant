package com.arefin.powerplant.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.util.Objects;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Battery {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @NotEmpty(message = "Postcode cannot be empty")
    private String postcode;

    @Positive(message = "Watt capacity must be a positive value")
    private int capacity;

    public Battery(String name, String postcode, int capacity) {
        this.name = name;
        this.postcode = postcode;
        this.capacity = capacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Battery battery = (Battery) o;
        return Objects.equals(id, battery.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
