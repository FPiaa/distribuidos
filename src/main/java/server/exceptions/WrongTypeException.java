package server.exceptions;

public class WrongTypeException extends BadRequestException{
    public WrongTypeException() {
        super("Some fields have wrong types");
    }
}
