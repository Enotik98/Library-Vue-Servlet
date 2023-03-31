package servlet;

import utils.JsonUtils;
import utils.TokenManager;
import entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import service.UserService;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.util.Base64;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject jsonObject = JsonUtils.getJson(request);
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        if (username != null || password != null) {
            User user = UserService.findUserByUsername(username);
            String hashPassword = UserService.hasPassword(password);
            if (user != null && user.getHash().equals(hashPassword)) {
                //create token
                String access = TokenManager.generateAccessToken(user.getId(), String.valueOf(user.getRole()));
                String refresh = TokenManager.generateRefreshToken(user.getId(), String.valueOf(user.getRole()));

                JSONObject tokens = new JSONObject();
                tokens.put("AccessToken", access);
                tokens.put("RefreshToken", refresh);

                response.getWriter().write(tokens.toString());
                response.setContentType("application/JSON");

//                PrintWriter out = response.getWriter();
//                out.println(JsonUtils.getJsonString(user));
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        }else {
            request.setAttribute("errorMessage", "Invalid username or password!");
            response.getWriter().write("Error login ");
        }

//        if (isValidCredentials(username, password)) {
//            response.getWriter().println("Welcome user!");
////            response.sendError(HttpServletResponse.SC_OK);
//        } else {
//        }
    }

    private boolean isValidCredentials(String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        User user = UserService.findUserByUsername(username);
        String hashPassword = UserService.hasPassword(password);
        if (user != null && user.getHash().equals(hashPassword)) {
            return true;
        }
        return false;
    }
    private String generateToken(int userId, String role){
        SecretKey key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
        String secretString = Base64.getEncoder().encodeToString(key.getEncoded());

        Claims claims = Jwts.claims();
        claims.put("userId", userId);
        claims.put("role", role);

        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(new SecretKeySpec(key.getEncoded(), io.jsonwebtoken.SignatureAlgorithm.HS256.getJcaName()))
                .compact();
        return token;
    }
}
