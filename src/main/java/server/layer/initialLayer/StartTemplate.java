package server.layer.initialLayer;

import com.fasterxml.jackson.core.JsonProcessingException;
import helper.json.JsonHelper;
import helper.validation.ConstraintViolated;
import helper.validation.ValidationHelper;
import server.exceptions.BadRequestException;
import server.exceptions.ServerResponseException;
import server.layer.interfaces.InitialLayer;

public abstract class StartTemplate<T> implements InitialLayer<T> {

    @Override
    public T buildRequest(String jsonString, Class<T> clazz) throws ServerResponseException {
        try {
             var request = JsonHelper.fromJson(jsonString, clazz);
             ValidationHelper.validate(request);
             return request;
        } catch (JsonProcessingException e) {
            throw new BadRequestException("");
        } catch (ConstraintViolated e) {
            throw new BadRequestException(e.getMessage());
        }
    }

}
