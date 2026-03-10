public class Car implements Vehicle, CarVehicle {
  private String make;
  private String model;
  private int year;

  private int numberOfDoors;
  private String fuelType;

  public void setMake(String make) {
      this.make = make;
  }

  public String getMake() {
      return make;
  }

  public void setModel(String model) {
      this.model = model;
  }

  public String getModel() {
      return model;
  }

  public void setYear(int year) {
      this.year = year;
  }

  public int getYear() {
      return year;
  }

  public void setNumberOfDoors(int doors) {
      this.numberOfDoors = doors;
  }

  public int getNumberOfDoors() {
      return numberOfDoors;
  }

  public void setFuelType(String fuelType) {
      this.fuelType = fuelType;
  }

  public String getFuelType() {
      return fuelType;
  }
}