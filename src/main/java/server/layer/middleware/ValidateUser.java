package server.layer.middleware;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jwt.JwtHelper;
import protocol.request.Request;
import protocol.response.Response;
import server.exceptions.ServerResponseException;
import server.exceptions.UnauthorizedAccessException;
import server.layer.interfaces.FinishLayer;
import server.layer.interfaces.Layer;

public class ValidateUser<Req extends Request<?>, Res extends Response<?>> implements Layer<Req, Res> {
    private Layer<Req, Res> next;

    @Override
    public void check(Req request) throws ServerResponseException {
        String token = request.header().token();
        try {
            @SuppressWarnings("unused")
            DecodedJWT _jwt = JwtHelper.verify(token);
        } catch (JWTVerificationException ex) {
             throw new UnauthorizedAccessException();
        }
    }

    @Override
    public Res next(Req request) throws ServerResponseException{
        check(request);
        if (next instanceof FinishLayer<Req, Res>) {
            return ((FinishLayer<Req, Res>) next).finish(request);
        }
        return next.next(request);
    }

    @Override
    public Layer<Req, Res> addLayer(Layer<Req, Res> newLayer) {
        if (next == null) {
            next = newLayer;
            return next;
        }
        return next.addLayer(newLayer);
    }

    @Override
    public void buildService(FinishLayer<Req, Res> consumer) {
        addLayer(consumer);
    }
}
