package com.backend.vehiclerental.service;

import com.backend.vehiclerental.enums.VehicleType;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Arrays;

class VehicleRentalServiceTest {
    @Test
    @Order(1)
    void addBranchIntoRentalService() {
        VehicleRentalService rentalService = new VehicleRentalService();
        Assertions.assertTrue(rentalService.addBranchIntoRentalService(
                "B1", new ArrayList<VehicleType>(Arrays.asList(
                        VehicleType.CAR, VehicleType.BIKE, VehicleType.VAN
                ))
        ));
    }

    @Test
    @Order(2)
    void addVehicleToBranch() {
        VehicleRentalService rentalService = new VehicleRentalService();
        Assertions.assertTrue(rentalService.addVehicleToBranch(
                "B1", VehicleType.CAR, "V1", 500
        ));
    }

    @Test
    @Order(3)
    void bookVehicleFromBranch() {
        VehicleRentalService rentalService = new VehicleRentalService();
        Assertions.assertEquals(-1, rentalService.bookVehicleFromBranch(
                "B1", VehicleType.CAR, 1, 5));
    }

    @Test
    @Order(4)
    void addVehicleToBranchInvalidType() {
        VehicleRentalService rentalService = new VehicleRentalService();
        Assertions.assertFalse(rentalService.addVehicleToBranch(
                "B1", VehicleType.BUS, "V5", 1000
        ));
    }
}