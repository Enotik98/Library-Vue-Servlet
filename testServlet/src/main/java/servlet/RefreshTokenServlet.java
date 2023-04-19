package servlet;

import utils.TokenManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;

@WebServlet("/refresh-token")
public class RefreshTokenServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject params = (JSONObject) request.getAttribute("params");
            if (params != null) {
                String newAccessToken = TokenManager.generateAccessToken(params.getInt("id"), params.getString("role"));
                String newRefreshToken = TokenManager.generateRefreshToken(params.getInt("id"), params.getString("role"));

                JSONObject tokens = new JSONObject();
                tokens.put("AccessToken", newAccessToken);
                tokens.put("RefreshToken", newRefreshToken);
                response.getWriter().write(tokens.toString());
            }else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }
    }
}
