package servlet;

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
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject jsonObject = JsonUtils.getJson(request);
        String email = jsonObject.getString("email");
        String password = jsonObject.getString("password");
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
                response.setContentType("application/JSON");

            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        }else {
            request.setAttribute("errorMessage", "Invalid email or password!");
            response.getWriter().write("Error login ");
        }
    }

}
