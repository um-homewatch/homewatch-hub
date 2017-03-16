package server.controllers;

@SuppressWarnings("SameParameterValue")
public class ErrorMessage {
  private final String message;

  ErrorMessage(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}