package server.router;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import protocol.request.header.Header;
import protocol.response.ErrorResponse;
import protocol.response.Response;
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

    @SuppressWarnings("unused")
    public Response<?> serve(Header header, String string_request) {

        try {
            return routes.get(header.operation()).startService(string_request);
        } catch (Exception e) {
            return new ErrorResponse(123, e.getMessage());
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
