package helper.validation;

public class ConstraintViolated extends Exception {
    public ConstraintViolated(String message) {
        super(message);
    }
}
