package server.exceptions;


public class BadRequestException extends ServerResponseException {

    public BadRequestException(final String message) {
        super(400, message);
    }

    public BadRequestException(int code, final String message) {
        super(code, message);
    }
}
