package server.router;

import com.google.gson.JsonSyntaxException;
import json.JsonHelper;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import protocol.request.EmptyRequest;
import protocol.response.Response;
import server.exceptions.BadRequestException;
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

    public Response<?> serve(@NonNull final String string_request) {

        try {
            EmptyRequest req = JsonHelper.fromJson(string_request, EmptyRequest.class);
            return routes.get(req.header().operation()).startService(string_request);
        } catch (ServerResponseException e) {
            return e.intoResponse();
        } catch (JsonSyntaxException e) {
            return new BadRequestException().intoResponse();
        }
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
