package server.layer.initialLayer;

import protocol.request.UpdateSegmentRequest;
import protocol.response.Response;
import protocol.response.UpdateSegmentResponse;
import server.controller.GraphController;
import server.exceptions.ServerResponseException;
import server.layer.middleware.ValidateAdmin;
import server.layer.middleware.ValidateToken;

public class StartUpdateSegment extends StartTemplate{
    @Override
    public Response<?> startService(String jsonString) throws ServerResponseException {
        var req = buildRequest(jsonString, UpdateSegmentRequest.class);
        var layer = new ValidateToken<UpdateSegmentRequest, UpdateSegmentResponse>()
                .addLayer(new ValidateAdmin<>())
                .buildService(request -> {
                    var dto = GraphController.getInstance().updateSegment(request);
                    return new UpdateSegmentResponse(dto);
                });
        return layer.next(req);
    }
}
