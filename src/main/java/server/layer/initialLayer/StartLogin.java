package server.layer.initialLayer;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import json.JsonHelper;
import helper.json.JsonHelper;
import helper.validation.ConstraintViolated;
import helper.validation.ValidationHelper;
import protocol.request.LoginRequest;
import protocol.response.Response;
import server.exceptions.BadRequestException;
import server.exceptions.ServerResponseException;
import server.exceptions.WrongTypeException;
import server.layer.finishLayer.ProcessLogin;
import server.layer.interfaces.InitialLayer;

import java.util.Set;
import java.util.stream.Collectors;

public class StartLogin implements InitialLayer {

    @Override
    public Response<?> startService(String jsonString) throws ServerResponseException {
        LoginRequest loginRequest = null;
        try {
            loginRequest = JsonHelper.fromJson(jsonString, LoginRequest.class);
        } catch (JsonProcessingException e) {
            throw new WrongTypeException();
            throw new BadRequestException("");
        }

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);
        if (!violations.isEmpty()) {
            String fields = violations.stream().map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
            throw new BadRequestException(fields);
        }
        return new ProcessLogin().finish(loginRequest);
    }
}
