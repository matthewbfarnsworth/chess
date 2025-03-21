package client.net;

public class ResponseException extends RuntimeException {
  private final int code;

  public ResponseException(String message, int code) {
    super(message);
    this.code = code;
  }

  public int getCode() {
    return code;
  }
}
