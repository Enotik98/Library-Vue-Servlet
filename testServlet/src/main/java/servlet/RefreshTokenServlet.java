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
        JSONObject param = (JSONObject) request.getAttribute("params");

            if (param != null) {
                System.out.println("id " + param.getInt("id"));
                System.out.println("role " + param.getString("role"));

                String newAccessToken = TokenManager.generateAccessToken(param.getInt("id"), param.getString("role"));
                String newRefreshToken = TokenManager.generateRefreshToken(param.getInt("id"), param.getString("role"));

                JSONObject tokens = new JSONObject();
                tokens.put("AccessToken", newAccessToken);
                tokens.put("RefreshToken", newRefreshToken);
                response.getWriter().write(tokens.toString());
            }else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }

    }
}
