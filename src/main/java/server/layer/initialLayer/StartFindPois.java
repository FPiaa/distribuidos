package server.layer.initialLayer;

import protocol.request.FindPoisRequest;
import protocol.response.FindPoisResponse;
import protocol.response.Response;
import server.controller.GraphController;
import server.exceptions.ServerResponseException;
import server.layer.middleware.ValidateToken;

public class StartFindPois extends StartTemplate{
    @Override
    public Response<?> startService(String jsonString) throws ServerResponseException {
        var req = buildRequest(jsonString, FindPoisRequest.class);
        var layer = new ValidateToken<FindPoisRequest, FindPoisResponse>()
                .buildService(request -> {
                    var dto = GraphController.getInstance().findPois();
                    return new FindPoisResponse(dto);
                });
        return layer.next(req);
    }
}
