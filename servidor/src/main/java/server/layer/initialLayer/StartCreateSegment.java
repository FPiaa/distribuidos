package server.layer.initialLayer;

import protocol.request.CreateSegmentRequest;
import protocol.response.CreateSegmentResponse;
import protocol.response.Response;
import server.controller.GraphController;
import server.exceptions.ServerResponseException;
import server.layer.middleware.ValidateAdmin;
import server.layer.middleware.ValidateToken;

public class StartCreateSegment extends StartTemplate {
    @Override
    public Response<?> startService(String jsonString) throws ServerResponseException {
        var req = buildRequest(jsonString, CreateSegmentRequest.class);
        var layer = new ValidateToken<CreateSegmentRequest, CreateSegmentResponse>()
                .addLayer(new ValidateAdmin<>())
                .buildService(request -> {
                    var dto = GraphController.getInstance().createSegment(request);
                    return new CreateSegmentResponse(dto);
                });
        return layer.next(req);
    }
}
