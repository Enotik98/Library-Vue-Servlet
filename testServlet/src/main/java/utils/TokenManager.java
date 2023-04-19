package utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.security.Key;
import java.util.Date;

public class TokenManager {
    public static final Logger log = Logger.getLogger(TokenManager.class);
    private static final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 1 * 60 * 1000;//1 min
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 5 * 24 * 60 * 60 * 1000; //5 day

    public static String generateAccessToken(int id, String role){
        return Jwts.builder()
                .claim("userId", id)
                .claim("role", role)
                .setExpiration(new Date(ACCESS_TOKEN_EXPIRATION_TIME + System.currentTimeMillis()))
                .signWith(secretKey)
                .compact();
    }
    public static String generateRefreshToken(int id, String role){
        return Jwts.builder()
                .claim("userId", id)
                .claim("role", role)
                .setExpiration(new Date(REFRESH_TOKEN_EXPIRATION_TIME + System.currentTimeMillis()))
                .signWith(secretKey)
                .compact();
    }
    public static JSONObject getParametersToken(String token){
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
        }catch (Exception e){
            log.info("ErrorToken " + e);
            return null;
        }
    }
}
