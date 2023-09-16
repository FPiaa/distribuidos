package server.router;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import request.Request;
import request.payload.RequestPayload;
import response.LoginResponse;
import response.Response;
import response.error.ErrorResponse;
import response.payload.ResponsePayload;
import server.layer.Layer;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class Router<Req extends Request<? extends RequestPayload>, Res extends Response<? extends ResponsePayload>> {
    @NonNull
    private Map<String, Layer<Req, Res>> routes;


    public static class RouterBuilder<Req extends Request<? extends RequestPayload>, Res extends Response<? extends ResponsePayload>> {
        @NonNull
        private final Map<String, Layer<Req, Res>> routes = new HashMap<>();

        public RouterBuilder<Req, Res> addRoute(String operation, Layer<Req, Res> handler) {
            routes.put(operation, handler);
            return this;
        }

        public Router<Req, Res> build() {
            return new Router<>(routes);
        }
    }

    public static RouterBuilder<?, ?> builder() {
        return new RouterBuilder<>();
    }

    public Response<?> serve(String operation) {
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
}
