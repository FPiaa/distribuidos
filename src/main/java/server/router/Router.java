package server.router;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import request.abstractclasses.Request;
import request.abstractclasses.RequestPayload;
import request.header.Header;
import response.LoginResponse;
import response.Response;
import response.error.ErrorResponse;
import response.payload.ResponsePayload;
import server.layer.InitialLayer;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class Router<Req extends Request<? extends RequestPayload>, Res extends Response<? extends ResponsePayload>> {

    @SuppressWarnings("unused")
    private Map<String, InitialLayer<Req, Res>> routes;

    public static RouterBuilder<?, ?> builder() {
        return new RouterBuilder<>();
    }

    @SuppressWarnings("unused")
    public Response<?> serve(Header header, String string_request) {
        String token;
        try {
            Algorithm alg = Algorithm.HMAC256("hiuhi");
            token = JWT.create()
                    .withIssuer("me")
                    .withClaim("isAdmin", false)
                    .withClaim("userId", 1)
                    .sign(alg);
            return new LoginResponse(token);
        } catch (JWTCreationException e) {
            System.err.println("oh no");
            return new ErrorResponse(123, "Failed to generate token");
        }
    }

    public static class RouterBuilder<Req extends Request<? extends RequestPayload>, Res extends Response<? extends ResponsePayload>> {
        @NonNull
        private final Map<String, InitialLayer<Req, Res>> routes = new HashMap<>();

        public RouterBuilder<Req, Res> addRoute(String operation, InitialLayer<Req, Res> handler) {
            routes.put(operation, handler);
            return this;
        }

        public Router<Req, Res> build() {
            return new Router<>(routes);
        }
    }
}
