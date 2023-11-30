package server.layer.initialLayer;

import protocol.request.UpdatePoiRequest;
import protocol.response.Response;
import protocol.response.UpdatePoiResponse;
import server.controller.GraphController;
import server.exceptions.ServerResponseException;
import server.layer.middleware.ValidateAdmin;
import server.layer.middleware.ValidateToken;

public class StartUpdatePoi extends StartTemplate{
    @Override
    public Response<?> startService(String jsonString) throws ServerResponseException {
        var req = buildRequest(jsonString, UpdatePoiRequest.class);
        var layer = new ValidateToken<UpdatePoiRequest, UpdatePoiResponse>()
                .addLayer(new ValidateAdmin<>())
                .buildService(request -> {
                    var dto = GraphController.getInstance().updatePoi(request);
                    return new UpdatePoiResponse(dto);
                });
        return layer.next(req);
    }
}
