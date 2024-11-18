
import java.util.ArrayList;

public class CarOwner extends User{
    private ArrayList<CarRentals> rentals;

    public CarOwner(String username, String fname, String lname, String dateOfBirth, String emailAddress, String password) {
        super(username, fname, lname, dateOfBirth, emailAddress, password);
        rentals = new ArrayList<CarRentals>();
    }

    public CarOwner(String username, String password){
        super(username, password);
        rentals = new ArrayList<CarRentals>();
    }

    public void rentCar(CarRentals carRentals){
        rentals.add(carRentals);
    }

    public void removeCar(CarRentals carRentals){
        rentals.remove(carRentals);
    }

    public String getCarRentals() {
        String carRentals = "";

        for(CarRentals c: rentals){
            carRentals = carRentals.concat(String.valueOf(c));
        }

        return carRentals;
    }

    @Override
    public String toString() {
        return "Car Owner ID: " + this.getUsername() + getCarRentals();
    }
}
