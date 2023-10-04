package server.router;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import server.exceptions.WrongTypeException;
import server.layer.interfaces.InitialLayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public class Router {

    @SuppressWarnings("unused")
    private Map<String, InitialLayer> routes;

    public static RouterBuilder builder() {
        return new RouterBuilder();
    }

    public Response<?> serve(final String string_request) throws ServerResponseException {

        EmptyRequest req = null;
        try {
            req = JsonHelper.fromJson(string_request, EmptyRequest.class);
        } catch (JsonProcessingException e) {
            throw new WrongTypeException();
            throw new BadRequestException("Invalid header");
        }
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<EmptyRequest>> violations = validator.validate(req);
        if (!violations.isEmpty()) {
            String fields = violations.stream().map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
            throw new BadRequestException(fields);
        }

        var startLayer = routes.get(req.header().operation());
        if (startLayer == null) {
            throw new MethodNotAllowedException(req.header().operation());
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
