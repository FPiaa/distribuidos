package server.layer.interfaces;

import protocol.response.Response;
import server.exceptions.ServerResponseException;

public interface InitialLayer {
    <T> T buildRequest(String jsonString, Class<T> clazz) throws ServerResponseException;

    Response<?> startService(String jsonString) throws ServerResponseException;
}
