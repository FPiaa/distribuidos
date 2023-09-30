package server.interfaces;

import response.Response;

public interface InitialLayer {
    Response<?> startService(String jsonString);
}
