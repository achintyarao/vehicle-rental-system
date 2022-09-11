package com.backend.vehiclerental;
import com.backend.vehiclerental.service.ProcessFileInputService;

public class VehicleRentalApp {
    public static void main(String[] args){
        try {
            if (args.length != 1) {
                throw new Exception("File path is not provided to be read from.");
            }
            String filePath = args[0];
            /*System.out.println("The path provided is:" + filePath);*/
            ProcessFileInputService fileInputService = new ProcessFileInputService(filePath);
            fileInputService.extractInstructionsFromFile();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
