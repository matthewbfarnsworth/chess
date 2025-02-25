package service;

public record RegisterResult(
        int code,
        String username,
        String authToken) {

}
