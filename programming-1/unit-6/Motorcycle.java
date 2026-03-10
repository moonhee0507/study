public class Motorcycle implements Vehicle, MotorVehicle {
  private String make;
  private String model;
  private int year;

  private int numberOfWheels;
  private String motorcycleType;

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

  public void setNumberOfWheels(int wheels) {
      this.numberOfWheels = wheels;
  }

  public int getNumberOfWheels() {
      return numberOfWheels;
  }

  public void setMotorcycleType(String type) {
      this.motorcycleType = type;
  }

  public String getMotorcycleType() {
      return motorcycleType;
  }
}