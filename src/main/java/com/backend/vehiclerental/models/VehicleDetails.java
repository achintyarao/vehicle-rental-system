package com.backend.vehiclerental.models;

import com.backend.vehiclerental.enums.VehicleType;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDetails {
    private String vehicleId;
    private VehicleType vehicleType;
    private Integer costPerHour;
    private Boolean isBooked = Boolean.FALSE;
}
