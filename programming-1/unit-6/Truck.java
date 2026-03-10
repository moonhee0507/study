public class Truck implements Vehicle, TruckVehicle {
  private String make;
  private String model;
  private int year;

  private double cargoCapacity;
  private String transmissionType;

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

  public void setCargoCapacity(double capacity) {
      this.cargoCapacity = capacity;
  }

  public double getCargoCapacity() {
      return cargoCapacity;
  }

  public void setTransmissionType(String type) {
      this.transmissionType = type;
  }

  public String getTransmissionType() {
      return transmissionType;
  }
}