package helper.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.util.stream.Collectors;

public class ValidationHelper {
    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public static <T> void validate(T object) throws ConstraintViolated{
        var violations = validator.validate(object);
        if (!violations.isEmpty()) {
            String fields = violations.stream().map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
            throw new ConstraintViolated(fields);
        }
    }

}
