package server.layer.initialLayer;

import com.fasterxml.jackson.core.JsonProcessingException;
import helper.json.JsonHelper;
import protocol.request.FindUsersRequest;
import protocol.response.FindUsersResponse;
import protocol.response.Response;
import server.exceptions.BadRequestException;
import server.exceptions.ServerResponseException;
import server.layer.finishLayer.ProcessFindUsers;
import server.layer.interfaces.InitialLayer;
import server.layer.interfaces.Layer;
import server.layer.middleware.ValidateAdmin;
import server.layer.middleware.ValidateUser;

public class StartFindUsers implements InitialLayer {
    @Override
    public Response<?> startService(String jsonString) throws ServerResponseException {
        FindUsersRequest request;
        try {
            request = JsonHelper.fromJson(jsonString, FindUsersRequest.class);
        } catch (JsonProcessingException e) {
            throw new BadRequestException("Invalid Json");
        }


        Layer<FindUsersRequest, FindUsersResponse> validateUser = new ValidateUser<FindUsersRequest, FindUsersResponse>()
                .addLayer(new ValidateAdmin<>()).buildService(new ProcessFindUsers());
        return validateUser.next(request);


    }
}
