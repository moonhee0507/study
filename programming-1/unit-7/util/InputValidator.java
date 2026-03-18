package util;

public class InputValidator {
  public static boolean isEmpty(String value) {
    return value == null || value.trim().isEmpty();
  }
}