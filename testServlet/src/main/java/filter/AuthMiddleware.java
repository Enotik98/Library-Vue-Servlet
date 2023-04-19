package filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import utils.TokenManager;

import java.io.IOException;

@WebFilter(urlPatterns = {"/*"}, filterName = "authFilter")
public class AuthMiddleware implements Filter {
    private final static String AUTHORIZATION_HEADER = "Authorization";
    private final static String BEARER_PREFIX = "Bearer ";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String authorizationHeader  = request.getHeader(AUTHORIZATION_HEADER);
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)){
            String token = authorizationHeader.substring(BEARER_PREFIX.length());
            JSONObject jsonObject = TokenManager.getParametersToken(token);
            if (jsonObject != null) {
                request.setAttribute("params", jsonObject);
                filterChain.doFilter(servletRequest, servletResponse);
            }else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }else {
            request.setAttribute("params", null);
            filterChain.doFilter(servletRequest, servletResponse);
        }

    }
}
