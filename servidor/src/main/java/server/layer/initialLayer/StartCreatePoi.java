package server.layer.initialLayer;

import protocol.request.CreatePoiRequest;
import protocol.response.CreatePoiResponse;
import protocol.response.Response;
import server.controller.GraphController;
import server.exceptions.ServerResponseException;
import server.layer.middleware.ValidateAdmin;
import server.layer.middleware.ValidateToken;

public class StartCreatePoi extends StartTemplate{
    @Override
    public Response<?> startService(String jsonString) throws ServerResponseException {
        var req = buildRequest(jsonString, CreatePoiRequest.class);
        var layer = new ValidateToken<CreatePoiRequest, CreatePoiResponse>()
                .addLayer(new ValidateAdmin<>())
                .buildService(request -> {
                    var dto = GraphController.getInstance().createPoi(request);
                    return new CreatePoiResponse(dto);
                });
        return layer.next(req);
    }
}
