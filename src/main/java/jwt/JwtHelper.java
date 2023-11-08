package jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JwtHelper {
    public static String createJWT(boolean isAdmin, long userId) {
        Algorithm alg = Algorithm.HMAC256("hiuhi");
        return JWT.create()
                .withIssuer("me")
                .withClaim("isAdmin", isAdmin)
                .withClaim("userId", userId)
                .sign(alg);
    }

    public static DecodedJWT verify(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256("hiuhi")).build();
        return verifier.verify(token);
    }

    public static long getId(String token) throws JWTVerificationException {
        DecodedJWT jwt = JWT.decode(token);
        Claim id = jwt.getClaim("userId");
        return id.asLong();
    }

    public static boolean getAdminStatus(String token) {
        DecodedJWT jwt = JWT.decode(token);
        Claim id = jwt.getClaim("isAdmin");
        return id.asBoolean();
    }

    public static DecodedJWT decode(String token) {
        return JWT.decode(token);
    }
}
