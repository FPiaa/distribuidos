package server.layer.middleware;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import jwt.JwtHelper;
import protocol.request.Request;
import protocol.response.Response;
import server.exceptions.InvalidTokenException;
import server.exceptions.ServerResponseException;
import server.layer.interfaces.Layer;

public class ValidateTokenField<Req extends Request<?>, Res extends Response<?>> extends LayerTemplate<Req, Res> {
    private Layer<Req, Res> next;

    @Override
    public void check(Req request) throws ServerResponseException {
        String token = request.getHeader().token();
        if (token == null) {
            throw new InvalidTokenException();
        }
        DecodedJWT jwt;
        try {
            // checando a validade do token
            jwt = JwtHelper.verify(token);
        } catch (JWTVerificationException ex) {
            throw new InvalidTokenException();
        }

        // checando se o token possui os campos obrigatórios
        Claim userId = jwt.getClaim("userId");
        Claim isAdmin = jwt.getClaim("isAdmin");
        if (userId.isMissing() || userId.isNull() || isAdmin.isMissing() || isAdmin.isNull()) {
            throw new InvalidTokenException();
        }
    }

}
