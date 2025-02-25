package service;

/**
 * Indicates the service failed.
 */
public class ServiceException extends RuntimeException {
  private final int code;

  public ServiceException(String message, int code) {
    super(message);
    this.code = code;
  }

  public int getCode() {
    return code;
  }
}
