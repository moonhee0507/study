import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    try {
      System.out.println("Select vehicle type:");
      System.out.println("1. Car");
      System.out.println("2. Motorcycle");
      System.out.println("3. Truck");

      int choice = scanner.nextInt();
      scanner.nextLine();

      Vehicle vehicle = null;

      if (choice == 1) {
          Car car = new Car();
          System.out.print("Enter make: ");
          car.setMake(scanner.nextLine());

          System.out.print("Enter model: ");
          car.setModel(scanner.nextLine());

          System.out.print("Enter year: ");
          car.setYear(scanner.nextInt());

          System.out.print("Enter number of doors: ");
          car.setNumberOfDoors(scanner.nextInt());
          scanner.nextLine();

          System.out.print("Enter fuel type: ");
          car.setFuelType(scanner.nextLine());

          vehicle = car;

          System.out.println("\nCar Details:");
          System.out.println(car.getMake() + " " + car.getModel());
          System.out.println("Year: " + car.getYear());
          System.out.println("Doors: " + car.getNumberOfDoors());
          System.out.println("Fuel: " + car.getFuelType());
      }

      else if (choice == 2) {
          Motorcycle moto = new Motorcycle();

          System.out.print("Enter make: ");
          moto.setMake(scanner.nextLine());

          System.out.print("Enter model: ");
          moto.setModel(scanner.nextLine());

          System.out.print("Enter year: ");
          moto.setYear(scanner.nextInt());

          System.out.print("Enter number of wheels: ");
          moto.setNumberOfWheels(scanner.nextInt());
          scanner.nextLine();

          System.out.print("Enter motorcycle type: ");
          moto.setMotorcycleType(scanner.nextLine());

          vehicle = moto;

          System.out.println("\nMotorcycle Details:");
          System.out.println(moto.getMake() + " " + moto.getModel());
          System.out.println("Year: " + moto.getYear());
          System.out.println("Wheels: " + moto.getNumberOfWheels());
          System.out.println("Type: " + moto.getMotorcycleType());
      }

      else if (choice == 3) {
          Truck truck = new Truck();

          System.out.print("Enter make: ");
          truck.setMake(scanner.nextLine());

          System.out.print("Enter model: ");
          truck.setModel(scanner.nextLine());

          System.out.print("Enter year: ");
          truck.setYear(scanner.nextInt());

          System.out.print("Enter cargo capacity (tons): ");
          truck.setCargoCapacity(scanner.nextDouble());
          scanner.nextLine();

          System.out.print("Enter transmission type: ");
          truck.setTransmissionType(scanner.nextLine());

          vehicle = truck;

          System.out.println("\nTruck Details:");
          System.out.println(truck.getMake() + " " + truck.getModel());
          System.out.println("Year: " + truck.getYear());
          System.out.println("Cargo Capacity: " + truck.getCargoCapacity());
          System.out.println("Transmission: " + truck.getTransmissionType());
      }

      else {
          System.out.println("Invalid vehicle type.");
      }
    } catch (Exception e) {
      System.out.println("Invalid input. Please try again.");
    }

    scanner.close();
  }
}