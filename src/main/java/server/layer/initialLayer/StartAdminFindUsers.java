package server.layer.initialLayer;

import protocol.request.FindUsersRequest;
import protocol.response.FindUsersResponse;
import protocol.response.Response;
import server.exceptions.ServerResponseException;
import server.layer.finishLayer.ProcessFindUsers;
import server.layer.interfaces.Layer;
import server.layer.middleware.ValidateAdmin;
import server.layer.middleware.ValidateUser;

public class StartAdminFindUsers extends StartTemplate<FindUsersRequest>{
    @Override
    public Response<?> startService(String jsonString) throws ServerResponseException {
        FindUsersRequest request = buildRequest(jsonString, FindUsersRequest.class);

        Layer<FindUsersRequest, FindUsersResponse> validateUser = new ValidateUser<FindUsersRequest, FindUsersResponse>()
                .addLayer(new ValidateAdmin<>()).buildService(new ProcessFindUsers());
        return validateUser.next(request);


    }
}
