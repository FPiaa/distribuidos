package server.layer.initialLayer;

import com.fasterxml.jackson.core.JsonProcessingException;
import helper.json.JsonHelper;
import helper.validation.ConstraintViolated;
import helper.validation.ValidationHelper;
import protocol.request.LoginRequest;
import protocol.response.Response;
import server.exceptions.BadRequestException;
import server.exceptions.ServerResponseException;
import server.layer.finishLayer.ProcessLogin;
import server.layer.interfaces.InitialLayer;

public class StartLogin implements InitialLayer {

    @Override
    public Response<?> startService(String jsonString) throws ServerResponseException {
        LoginRequest loginRequest;
        try {
            loginRequest = JsonHelper.fromJson(jsonString, LoginRequest.class);
            ValidationHelper.validate(loginRequest);
        } catch (JsonProcessingException e) {
            throw new BadRequestException("");
        } catch (ConstraintViolated e) {
            throw new BadRequestException(e.getMessage());
        }


        return new ProcessLogin().finish(loginRequest);
    }
}
