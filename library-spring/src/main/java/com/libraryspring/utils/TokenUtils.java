package com.libraryspring.utils;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.json.JSONObject;

import java.security.Key;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenUtils {
    public static final String AUTH0_AUDIENCE = "https://dev-sx5kw23vuznwkzbt.us.auth0.com/userinfo";
    public static final String AUTH0_ISSUER = "https://dev-sx5kw23vuznwkzbt.us.auth0.com/";
    private static final String SECRET_KEY = "my-secret-key";
    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 1 * 60 * 1000;//1 min
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 5 * 24 * 60 * 60 * 1000; //5 day

    public static String generateAccessToken(int id, String role) {
        return Jwts.builder()
                .claim("userId", id)
                .claim("role", role)
                .setExpiration(new Date(ACCESS_TOKEN_EXPIRATION_TIME + System.currentTimeMillis()))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public static String generateRefreshToken(int id, String role) {
        return Jwts.builder()
                .claim("userId", id)
                .claim("role", role)
                .setExpiration(new Date(REFRESH_TOKEN_EXPIRATION_TIME + System.currentTimeMillis()))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
    public static Map<String, String> getParametersToken(String token) {
        try {
            Map<String, String> paramsMap = new HashMap<>();
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token);

            int id = claims.getBody().get("userId", Integer.class);
            String role = claims.getBody().get("role", String.class);
            paramsMap.put("id", String.valueOf(id));
            paramsMap.put("role", role);
            return paramsMap;
        } catch (Exception e) {
            System.out.println("ErrorToken " + e);
            return null;
        }
    }
    public static String getValidToken(String token) {
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
            return email;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }

    }
    private static RSAPublicKey getPublicKey(String kid) {
        try {
            JwkProvider jwkProvider = new JwkProviderBuilder(AUTH0_ISSUER).build();
            Jwk jwk = jwkProvider.get(kid);
            return (RSAPublicKey) jwk.getPublicKey();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}
