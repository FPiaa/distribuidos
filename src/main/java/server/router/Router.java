package server.router;

import com.google.gson.JsonSyntaxException;
import helper.json.JsonHelper;
import helper.validation.ConstraintViolated;
import helper.validation.ValidationHelper;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import protocol.request.EmptyRequest;
import protocol.response.Response;
import server.exceptions.BadRequestException;
import server.exceptions.MethodNotAllowedException;
import server.exceptions.ServerResponseException;
import server.layer.interfaces.InitialLayer;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class Router {

    @SuppressWarnings("unused")
    private Map<String, InitialLayer> routes;

    public static RouterBuilder builder() {
        return new RouterBuilder();
    }

    public Response<?> serve(final String string_request) throws ServerResponseException {

        EmptyRequest req;
        try {
            req = JsonHelper.fromJson(string_request, EmptyRequest.class);
            ValidationHelper.validate(req);
        } catch (JsonSyntaxException e) {
            throw new BadRequestException("Invalid header");
        } catch (ConstraintViolated e) {
            throw new BadRequestException(e.getMessage());
        }

        var startLayer = routes.get(req.getHeader().operation());
        if (startLayer == null) {
            throw new MethodNotAllowedException(req.getHeader().operation());
        }

        return startLayer.startService(string_request);
    }

    public static class RouterBuilder {
        @NonNull
        private final Map<String, InitialLayer> routes = new HashMap<>();

        public RouterBuilder addRoute(String operation, InitialLayer handler) {
            routes.put(operation, handler);
            return this;
        }

        public Router build() {
            return new Router(routes);
        }
    }
}
