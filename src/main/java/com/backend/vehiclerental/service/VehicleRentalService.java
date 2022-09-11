package com.backend.vehiclerental.service;

import com.backend.vehiclerental.enums.Pricing;
import com.backend.vehiclerental.enums.VehicleType;
import com.backend.vehiclerental.models.Branch;
import com.backend.vehiclerental.models.RentalCity;
import com.backend.vehiclerental.models.VehicleDetails;

import java.util.*;
import java.util.stream.Collectors;

public class VehicleRentalService {
    public Boolean addBranchIntoRentalService(String branchId, List<VehicleType> supportedVehicles){
        Boolean onBoardingSuccessful = Boolean.TRUE;
        Map<String, Branch> branches = RentalCity.getInstance().getBranches();
        if (branches.get(branchId) != null){
            /*System.out.println("This branch has already been onboarded");*/
            onBoardingSuccessful = Boolean.FALSE;
            return onBoardingSuccessful;
        }
        branches.put(branchId, Branch.builder().branchId(branchId)
                .supportedVehicleTypes(supportedVehicles)
                .build());
        /*System.out.println("The list of branches are:" + branches);*/
        return onBoardingSuccessful;
    }

    public Boolean addVehicleToBranch(String branchId, VehicleType vehicleType, String vehicleId, Integer costPerHour){
        Boolean additionOfVehicleSuccessful = Boolean.TRUE;
        Map<String, Branch> branches = RentalCity.getInstance().getBranches();
        if (branches.get(branchId) == null){
            /*System.out.println("This branch has doesn't exist to onboard new vehicle");*/
            additionOfVehicleSuccessful = Boolean.FALSE;
            return additionOfVehicleSuccessful;
        }

        Branch currentBranchDetails = branches.get(branchId);
        if (!currentBranchDetails.getSupportedVehicleTypes().contains(vehicleType)){
            /*System.out.println("The oboarding vehicle type, is not supported by Branch");*/
            additionOfVehicleSuccessful = Boolean.FALSE;
            return additionOfVehicleSuccessful;
        }

        VehicleDetails vehicleToBeOnboarded = VehicleDetails.builder()
                .vehicleId(vehicleId)
                .vehicleType(vehicleType)
                .costPerHour(costPerHour)
                .isBooked(Boolean.FALSE)
                .build();

        List<VehicleDetails> existingVehicleList = currentBranchDetails.getListOfVehiclesOnboarded();
        if(existingVehicleList == null){
            existingVehicleList = new ArrayList<>();
        }
        Boolean onboardedVehicle = existingVehicleList.add(vehicleToBeOnboarded);
        if (!onboardedVehicle){
            /*System.out.println("The operation for onboarding has failed");*/
            additionOfVehicleSuccessful = Boolean.FALSE;
            return additionOfVehicleSuccessful;
        }
        currentBranchDetails.setListOfVehiclesOnboarded(existingVehicleList);
        branches.put(branchId, currentBranchDetails);
        /*System.out.println("Branches details:" + branches);*/
        return additionOfVehicleSuccessful;
    }

    public Integer bookVehicleFromBranch(String branchId, VehicleType vehicleType, Integer startTime, Integer endTime){
        Integer totalCost = -1;
        Map<String, Branch> branches = RentalCity.getInstance().getBranches();
        List<VehicleDetails> vehicleDetails = availableVehicles(branchId, startTime, endTime);
        if (vehicleDetails.isEmpty()){
            return totalCost;
        }

        List<VehicleDetails> availableByVehicleTypeList = vehicleDetails.stream()
                .filter(e -> e.getVehicleType().equals(vehicleType))
                .collect(Collectors.toList());

        if (availableByVehicleTypeList.isEmpty()){
            return totalCost;
        }

        VehicleDetails vehicleToBeBooked = availableByVehicleTypeList.get(0);
        Branch branchToBeUpdated = branches.get(branchId);
        totalCost = generatePrice(branches.get(branchId).getListOfVehiclesOnboarded(), vehicleToBeBooked.getCostPerHour(),
                endTime - startTime);
        List<VehicleDetails> listOfVehiclesOnboarded = branches.get(branchId).getListOfVehiclesOnboarded();
        Integer indexToBeUpdated = listOfVehiclesOnboarded.indexOf(vehicleToBeBooked);
        vehicleToBeBooked.setIsBooked(Boolean.TRUE);
        listOfVehiclesOnboarded.set(indexToBeUpdated, vehicleToBeBooked);
        branchToBeUpdated.setListOfVehiclesOnboarded(listOfVehiclesOnboarded);
        branches.put(branchId, branchToBeUpdated);
        return totalCost;
    }

    private Integer generatePrice(List<VehicleDetails> listOfAllVehicles, Integer costPerHour, Integer noOfHours) {
        Integer costOfBooking = -1;
        List<VehicleDetails> allCars = listOfAllVehicles.stream()
                .filter(e -> e.getVehicleType().equals(VehicleType.CAR))
                .collect(Collectors.toList());
        List<VehicleDetails> bookedCars = allCars
                .stream().filter(e -> e.getIsBooked())
                .collect(Collectors.toList());

        Double percentageBooked = Double.valueOf((bookedCars.size()/allCars.size()) * 100);
        if (percentageBooked > 80){
            costOfBooking = (int) Math.round(noOfHours*costPerHour* Pricing.DYNAMIC.getIncrease());
        } else {
            costOfBooking = (int) Math.round(noOfHours*costPerHour* Pricing.FLAT.getIncrease());
        }
        return costOfBooking;
    }

    public List<VehicleDetails> availableVehicles(String branchId, Integer startTime, Integer endTime){
        List<VehicleDetails> availableVehicles = new ArrayList<>();
        Map<String, Branch> branches = RentalCity.getInstance().getBranches();
        if (branches.get(branchId) == null){
            /*System.out.println("This branch has doesn't exist to onboard new vehicle");*/
            return availableVehicles;
        }

        Branch currentBranchDetails = branches.get(branchId);
        availableVehicles = currentBranchDetails.getListOfVehiclesOnboarded()
                .stream().filter(e -> !e.getIsBooked())
                .collect(Collectors.toList());

        Collections.sort(availableVehicles, Comparator.comparing(VehicleDetails::getCostPerHour));

        return availableVehicles;
    }

    public List<String> printVehicleIdsForAvailableVehicles(String branchId, Integer startTime, Integer endTime){
        List<String> vehicleIdsAvailable =  new ArrayList<>();
        List<VehicleDetails> vehicleDetails = availableVehicles(branchId, startTime, endTime);
        if (vehicleDetails.isEmpty()){
            /*System.out.println("No available vehicles");*/
            return vehicleIdsAvailable;
        }
        vehicleIdsAvailable = vehicleDetails.stream()
                .map(VehicleDetails::getVehicleId)
                .collect(Collectors.toList());

        return vehicleIdsAvailable;
    }
}
