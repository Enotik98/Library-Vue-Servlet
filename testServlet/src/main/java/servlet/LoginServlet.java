package servlet;

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
