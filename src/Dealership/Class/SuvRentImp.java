package Dealership.Class;

import Dealership.Interface.RentService;

import java.util.Date;

public class SuvRentImp implements RentService {
    private static final double SUV_RATE = 200.0;
    private static final double SSN_DISCOUNT = 0.05;
    private static final double EIN_DISCOUNT = 0.10;

    @Override
    public void rentVehicle(Dealership dealership, String plateNumber, String clientId, Date rentDate) {
        Vehicle vehicle = dealership.searchVehicleByPlate(plateNumber);
        Client client = dealership.searchClientById(clientId);

        if (vehicle == null) {
            System.out.println("Vehicle not found.");
        } else if (client == null) {
            System.out.println("Client not found.");
        } else if (vehicle.isRented()) {
            System.out.println("Vehicle is already rented.");
        } else {
            vehicle.vehicleRent(rentDate);
            System.out.println("SUV Vehicle rented successfully.");
        }
    }
    @Override
    public double returnVehicle(Dealership dealership, String plateNumber, String clientId, Date returnDate) {
        Vehicle vehicle = dealership.searchVehicleByPlate(plateNumber);

        if (vehicle == null) {
            System.out.println("Vehicle not found.");
            return 0;
        } else if (!vehicle.isRented) {
            System.out.println("Vehicle is not currently rented.");
            return 0;
        } else {
            double rentalPrice = rentPrice(dealership, plateNumber, clientId, returnDate);
            System.out.println("Vehicle returned successfully.");
            return rentalPrice;
        }
    }
    @Override
    public double rentPrice(Dealership dealership, String plateNumber, String clientId, Date returnDate) {
        Client client = dealership.searchClientById(clientId);
        Vehicle vehicle = dealership.searchVehicleByPlate(plateNumber);

        if (vehicle.isRented) {
            double rentalDuration = calculateRentTime(dealership, plateNumber, returnDate);
            double rentalPrice = SUV_RATE * rentalDuration;

            switch (client.getType()) {
                case SSN -> {
                    if (rentalDuration >= 5) {
                        rentalPrice *= 1 - SSN_DISCOUNT;
                    }
                }
                case EIN -> {
                    if (rentalDuration >= 3) {
                        rentalPrice *= 1 - EIN_DISCOUNT;
                    }
                }
            }

            vehicle.setRented();
            vehicle.setLastRentedDate();
            return rentalPrice;
        }
        return 0;
    }

    @Override
    public double calculateRentTime(Dealership dealership, String plateNumber, Date returnDate) {
        Vehicle vehicle = dealership.searchVehicleByPlate(plateNumber);

        long dateDifferenceInMilliseconds = returnDate.getTime() - vehicle.lastRentedDate.getTime();
        float millisecondsInADay = 24 * 60 * 60 * 1000;

        int rentalDays =  (int) Math.floor(dateDifferenceInMilliseconds / (millisecondsInADay));
        int rentalHours = (int) Math.floor(dateDifferenceInMilliseconds / ((millisecondsInADay / 24) - (24 * rentalDays)));

        System.out.printf("Vehicle rented for: %d day(s) and %d hour(s). \n", rentalDays, rentalHours);

        return Math.ceil(dateDifferenceInMilliseconds / (millisecondsInADay));
    }
}