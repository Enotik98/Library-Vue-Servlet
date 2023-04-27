package utils;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.Key;
import java.security.MessageDigest;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Date;
import java.util.List;

public class TokenManager {
    public static final String AUTH0_AUDIENCE = "https://dev-3jh7bscjgsdbuvxn.us.auth0.com/userinfo";
    public static final String AUTH0_ISSUER = "https://dev-3jh7bscjgsdbuvxn.us.auth0.com/";

    public static final Logger log = Logger.getLogger(TokenManager.class);
    private static final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 1 * 60 * 1000;//1 min
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 5 * 24 * 60 * 60 * 1000; //5 day

    public static String generateAccessToken(int id, String role) {
        return Jwts.builder()
                .claim("userId", id)
                .claim("role", role)
                .setExpiration(new Date(ACCESS_TOKEN_EXPIRATION_TIME + System.currentTimeMillis()))
                .signWith(secretKey)
                .compact();
    }

    public static String generateRefreshToken(int id, String role) {
        return Jwts.builder()
                .claim("userId", id)
                .claim("role", role)
                .setExpiration(new Date(REFRESH_TOKEN_EXPIRATION_TIME + System.currentTimeMillis()))
                .signWith(secretKey)
                .compact();
    }

    public static JSONObject getParametersToken(String token) {
        try {
            JSONObject jsonObject = new JSONObject();
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);

            int id = claims.getBody().get("userId", Integer.class);
            String role = claims.getBody().get("role", String.class);
            jsonObject.put("id", id);
            jsonObject.put("role", role);
            return jsonObject;
        } catch (Exception e) {
            log.info("ErrorToken " + e);
            return null;
        }
    }

    public static JSONObject getValidToken(String token) {
        JSONObject jsonObject = new JSONObject();
        DecodedJWT decodedJWT = JWT.decode(token);
        String kid = decodedJWT.getKeyId();
        Algorithm algorithm = Algorithm.RSA256(getPublicKey(kid), null);
        try {
            JWT.require(algorithm)
                    .withIssuer(AUTH0_ISSUER)
                    .withAudience(AUTH0_AUDIENCE)
                    .build()
                    .verify(token);
            String email = decodedJWT.getClaim("user-email").asString();
            jsonObject.put("email", email);
            return jsonObject;
        } catch (Exception e) {
            log.error(e);
            return null;
        }

    }

    private static RSAPublicKey getPublicKey(String kid) {
        try {
            System.out.println(kid);
            JwkProvider jwkProvider = new JwkProviderBuilder(AUTH0_ISSUER).build();
            Jwk jwk = jwkProvider.get(kid);
            return (RSAPublicKey) jwk.getPublicKey();
        } catch (Exception e) {
            System.out.println(e);
            log.error(e);
            return null;
        }
    }
}
