package server.layer.initialLayer;

import protocol.request.DeletePoiRequest;
import protocol.response.DeletePoiResponse;
import protocol.response.Response;
import server.controller.GraphController;
import server.exceptions.ServerResponseException;
import server.layer.middleware.ValidateAdmin;
import server.layer.middleware.ValidateToken;

public class StartDeletePoi extends StartTemplate{
    @Override
    public Response<?> startService(String jsonString) throws ServerResponseException {
        var req = buildRequest(jsonString, DeletePoiRequest.class);
        var layer = new ValidateToken<DeletePoiRequest, DeletePoiResponse>()
                .addLayer(new ValidateAdmin<>())
                .buildService(request -> {
                    var dto = GraphController.getInstance().deletePoi(request.getPayload().id());
                    var mensagem = "Poi com id %d foi deletado.".formatted(request.getPayload().id());
                    return new DeletePoiResponse(mensagem);
                });
        return layer.next(req);
    }
}
