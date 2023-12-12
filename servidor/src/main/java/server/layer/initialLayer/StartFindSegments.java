package server.layer.initialLayer;

import protocol.request.FindSegmentsRequest;
import protocol.response.FindSegmentsResponse;
import protocol.response.Response;
import server.controller.GraphController;
import server.exceptions.ServerResponseException;
import server.layer.middleware.ValidateToken;

public class StartFindSegments extends StartTemplate{
    @Override
    public Response<?> startService(String jsonString) throws ServerResponseException {
        var req = buildRequest(jsonString, FindSegmentsRequest.class);
        var layer = new ValidateToken<FindSegmentsRequest, FindSegmentsResponse>()
                .buildService(request -> {
                    var dto = GraphController.getInstance().findSegments();
                    return new FindSegmentsResponse(dto);
                });
        return layer.next(req);
    }
}
