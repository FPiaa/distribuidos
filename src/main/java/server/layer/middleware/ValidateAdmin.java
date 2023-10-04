package server.layer.middleware;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import jwt.JwtHelper;
import protocol.request.Request;
import protocol.response.Response;
import server.exceptions.ForbiddenAccessException;
import server.exceptions.ServerResponseException;
import server.exceptions.UnauthorizedAccessException;

public class ValidateAdmin<Req extends Request<?>, Res extends Response<?>> extends LayerTemplate<Req, Res> {
    @Override
    public void check(Req request) throws ServerResponseException {
        String token = request.header().token();
        if (token == null) {
            throw new UnauthorizedAccessException();
        }

        try {
            var jwt = JwtHelper.verify(token);
            Claim isAdmin = jwt.getClaim("isAdmin");
            Claim userId = jwt.getClaim("userId");
            if (isAdmin.isMissing() || isAdmin.isNull() || !isAdmin.asBoolean()) {
                if (!userId.isMissing()) {
                    throw new ForbiddenAccessException(userId.asString());
                }
                throw new ForbiddenAccessException();
            }
        } catch (JWTVerificationException e) {
            throw new ForbiddenAccessException();
        }
    }
}
