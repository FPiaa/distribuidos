package server.layer.interfaces;

import com.google.gson.JsonSyntaxException;
import protocol.response.Response;
import server.exceptions.ServerResponseException;

public interface InitialLayer {
    Response<?> startService(String jsonString) throws ServerResponseException, JsonSyntaxException;
}
