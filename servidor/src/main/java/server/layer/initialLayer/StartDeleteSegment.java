package server.layer.initialLayer;

import protocol.request.DeleteSegmentRequest;
import protocol.response.DeleteSegmentResponse;
import protocol.response.Response;
import server.controller.GraphController;
import server.exceptions.ServerResponseException;
import server.layer.middleware.ValidateAdmin;
import server.layer.middleware.ValidateToken;

public class StartDeleteSegment extends StartTemplate{
    @Override
    public Response<?> startService(String jsonString) throws ServerResponseException {
        var req = buildRequest(jsonString, DeleteSegmentRequest.class);
        var layer = new ValidateToken<DeleteSegmentRequest, DeleteSegmentResponse>()
                .addLayer(new ValidateAdmin<>())
                .buildService(request -> {
                    var dto = GraphController.getInstance().deleteSegment(request.getPayload().pdi_inicial(), request.getPayload().pdi_final());
                    var mensagem = "Segmento entre os pontos %d e %d foi deletado".formatted(request.getPayload().pdi_inicial(), request.getPayload().pdi_final());
                    return new DeleteSegmentResponse(mensagem);
                });
        return layer.next(req);
    }
}
