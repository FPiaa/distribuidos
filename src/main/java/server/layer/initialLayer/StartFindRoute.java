package server.layer.initialLayer;

import protocol.request.FindRouteRequest;
import protocol.response.FindRouteResponse;
import protocol.response.Response;
import server.controller.GraphController;
import server.exceptions.ServerResponseException;
import server.layer.middleware.ValidateToken;

public class StartFindRoute extends StartTemplate{
    @Override
    public Response<?> startService(String jsonString) throws ServerResponseException {
        var request = buildRequest(jsonString, FindRouteRequest.class);
        var layer = new ValidateToken<FindRouteRequest, FindRouteResponse>()
                .buildService((req) -> {
                    var commands = GraphController.getInstance().findRoute(req.getPayload().pdi_inicial(), req.getPayload().pdi_final());
                    return new FindRouteResponse(commands);
                });

        return layer.next(request);
    }
}
