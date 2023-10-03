package server.exceptions;

public class MissingObligatoryFieldsException extends BadRequestException{
    public MissingObligatoryFieldsException() {
        super(400, "Missing obligatory fields");
    }
}
