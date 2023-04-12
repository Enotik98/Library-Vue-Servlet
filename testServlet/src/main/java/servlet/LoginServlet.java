package servlet;

import com.auth0.client.auth.AuthAPI;
import com.auth0.exception.APIException;
import com.auth0.json.auth.TokenHolder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.apache.log4j.Logger;
import utils.JsonUtils;
import utils.TokenManager;
import entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import service.UserService;

import java.io.IOException;
import java.util.Date;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(LoginServlet.class);
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject jsonObject = JsonUtils.getJson(request);
        String email = jsonObject.getString("email");
        String password = jsonObject.getString("password");
        response.setContentType("application/JSON");
        if (email != null || password != null) {
            User user = UserService.findUserByEmail(email);
            String hashPassword = UserService.hasPassword(password);
            if (user != null && user.getHash().equals(hashPassword)) {
//                AuthAPI auth = new AuthAPI("dev-3jh7bscjgsdbuvxn.us.auth0.com", "jlob9DKeeus1Bw0Z6BTTkcYdFUCm8JDM", "005EH-3xAYKjXaGADLJx6bUqUH8-FsujoLgt56TRtRakPIJQoLRlVsUh8178bX4I");
//                try{
//                    TokenHolder holder = auth.login(user.getEmail(), user.getPassword())
//                            .execute();
//                    String access = holder.getAccessToken();
//                    Algorithm algorithm = Algorithm.HMAC256("your-secret");
//                    String token = JWT.create()
//                            .withIssuer("auth0")
//                            .withSubject(email)
//                            .withExpiresAt(new Date(System.currentTimeMillis() + 86400000))
//                            .withArrayClaim("roles", new String[] {"user"})
//                            .sign(algorithm);
//                    response.setContentType("application/json");
//                    response.setCharacterEncoding("UTF-8");
//                    JSONObject json = new JSONObject();
//                    json.put("accessToken", token);
//                    response.getWriter().write(json.toString());
//                }catch (APIException e){
//                    logger.error(e);
//                }
                //create token
                String access = TokenManager.generateAccessToken(user.getId(), String.valueOf(user.getRole()));
                String refresh = TokenManager.generateRefreshToken(user.getId(), String.valueOf(user.getRole()));

                JSONObject tokens = new JSONObject();
                tokens.put("AccessToken", access);
                tokens.put("RefreshToken", refresh);
                response.getWriter().write(tokens.toString());

            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                String errorJson = "{\"error\": \"Невірний логін або пароль\"}";
                response.getWriter().write(errorJson);
            }
        } else {
            request.setAttribute("errorMessage", "Invalid email or password!");
            response.getWriter().write("Error login ");
            logger.error("Error login");
        }
    }
}
