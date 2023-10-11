package server.layer.middleware;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import jwt.JwtHelper;
import protocol.request.Request;
import protocol.response.Response;
import server.exceptions.ForbiddenAccessException;
import server.exceptions.ServerResponseException;

public class ValidateAdmin<Req extends Request<?>, Res extends Response<?>> extends LayerTemplate<Req, Res> {
    @Override
    public void check(Req request) throws ServerResponseException {
        try {
            DecodedJWT jwt = JwtHelper.decode(request.getHeader().token());
            Claim isAdmin = jwt.getClaim("isAdmin");
            Claim userId = jwt.getClaim("userId");
            if (!isAdmin.asBoolean()) {
                if (!userId.isMissing()) {
                    throw new ForbiddenAccessException(userId.asLong());
                }
                throw new ForbiddenAccessException();
            }
        } catch (JWTVerificationException e) {
            throw new ForbiddenAccessException();
        }
    }
}
