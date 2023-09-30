package server.layer.middleware;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jwt.JwtHelper;
import request.Request;
import response.Response;
import response.ErrorResponse;
import server.interfaces.FinishLayer;
import server.interfaces.Layer;

public class ValidateUser<Req extends Request<?>, Res extends Response<?>> implements Layer<Req, Res> {
    private Layer<Req, Res> next ;

    @Override
    public boolean check(Req request) {
        String token = request.header().token();
        try {
            DecodedJWT _jwt = JwtHelper.verify(token);
            return true;
        }catch (JWTVerificationException ex) {
            return false;
        }
    }

    @Override
    public Response<?> next(Req request) {
        if(!check(request)) {
            return new ErrorResponse(321, "Unauthorized");
        }
        if(next instanceof FinishLayer<Req, Res>) {
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
