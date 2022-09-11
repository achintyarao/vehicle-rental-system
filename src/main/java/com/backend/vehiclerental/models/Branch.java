package com.backend.vehiclerental.models;

import com.backend.vehiclerental.enums.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Branch {
    private String branchId;
    private List<VehicleType> supportedVehicleTypes;
    private List<VehicleDetails> listOfVehiclesOnboarded;
}
