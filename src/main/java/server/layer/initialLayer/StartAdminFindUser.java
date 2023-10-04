package server.layer.initialLayer;

import protocol.request.FindUserRequest;
import protocol.response.FindUserResponse;
import protocol.response.Response;
import server.exceptions.ServerResponseException;
import server.layer.finishLayer.ProcessFindUser;
import server.layer.interfaces.Layer;
import server.layer.middleware.ValidateAdmin;
import server.layer.middleware.ValidateUser;

public class StartAdminFindUser extends StartTemplate<FindUserRequest>{
    @Override
    public Response<?> startService(String jsonString) throws ServerResponseException {
        FindUserRequest request = buildRequest(jsonString, FindUserRequest.class);

        Layer<FindUserRequest, FindUserResponse> layer = new ValidateUser<FindUserRequest, FindUserResponse>()
                .addLayer(new ValidateAdmin<>())
                .buildService(new ProcessFindUser());

        return layer.next(request);
    }
}
