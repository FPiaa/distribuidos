package server.layer.middleware;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jwt.JwtHelper;
import protocol.request.Request;
import protocol.response.Response;
import server.exceptions.ServerResponseException;
import server.exceptions.UnauthorizedAccessException;
import server.layer.interfaces.Layer;

public class ValidateUser<Req extends Request<?>, Res extends Response<?>> extends LayerTemplate<Req, Res> {
    private Layer<Req, Res> next;

    @Override
    public void check(Req request) throws ServerResponseException {
        String token = request.getHeader().token();
        if (token == null) {
            throw new UnauthorizedAccessException();
        }

        try {
            JwtHelper.verify(token);
        } catch (JWTVerificationException ex) {
             throw new UnauthorizedAccessException();
        }
    }

}
