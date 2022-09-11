package com.backend.vehiclerental.service;

import com.backend.vehiclerental.enums.SupportedInstructions;
import com.backend.vehiclerental.enums.VehicleType;
import lombok.Data;
import lombok.NonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ProcessFileInputService{
    @NonNull
    private String filePath;

    public void extractInstructionsFromFile() throws Exception {
        List<String> inputLines = new ArrayList<>();
        File inputFile = new File(filePath);
        try {
            if (!inputFile.exists()){
                throw new FileNotFoundException();
            }
            inputLines = Files.readAllLines(inputFile.toPath(), Charset.defaultCharset());
            if (inputLines.isEmpty()){
                throw new NullPointerException("Could not parse the file.");
            }
            executeInstructions(inputLines);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void executeInstructions(List<String> inputLines){
        VehicleRentalService vehicleRentalService = new VehicleRentalService();
        for (String lineDetail : inputLines){
            String[] instructions = lineDetail.split(" ");
            SupportedInstructions instructionType = SupportedInstructions.valueOf(instructions[0]);

            switch (instructionType){
                case ADD_BRANCH:
                    Boolean branchAdded = vehicleRentalService.addBranchIntoRentalService(
                            instructions[1] ,
                            Arrays.stream(instructions[2].split(",")).map(VehicleType::valueOf)
                                    .collect(Collectors.toList())
                    );
                    System.out.println((branchAdded) ? "TRUE" : "FALSE");
                    break;
                case ADD_VEHICLE:
                    Boolean vehicleAdded = vehicleRentalService.addVehicleToBranch(instructions[1],
                            VehicleType.valueOf(instructions[2]), instructions[3], Integer.parseInt(instructions[4]));
                    System.out.println((vehicleAdded) ? "TRUE" : "FALSE");
                    break;
                case BOOK:
                    Integer costOfBooking = vehicleRentalService.bookVehicleFromBranch(
                            instructions[1], VehicleType.valueOf(instructions[2]), Integer.parseInt(instructions[3]),
                                    Integer.parseInt(instructions[4]));
                    System.out.println(costOfBooking);
                    break;
                case DISPLAY_VEHICLES:
                    List<String> availableVehicles = vehicleRentalService.printVehicleIdsForAvailableVehicles(
                            instructions[1], Integer.parseInt(instructions[2]), Integer.parseInt(instructions[3])
                    );
                    System.out.println(String.join(",", availableVehicles));
                    break;
            }
        }
    }
}
