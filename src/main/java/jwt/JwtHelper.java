package jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JwtHelper {
    public static String createJWT(boolean isAdmin, int userId) {
        Algorithm alg = Algorithm.HMAC256("hiuhi");
        return JWT.create()
                .withIssuer("me")
                .withClaim("isAdmin", false)
                .withClaim("userId", 1)
                .sign(alg);
    }

    public static DecodedJWT verify(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256("hiuhi")).build();
        return verifier.verify(token);
    }
}
