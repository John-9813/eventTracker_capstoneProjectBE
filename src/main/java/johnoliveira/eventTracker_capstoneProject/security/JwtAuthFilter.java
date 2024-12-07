package johnoliveira.eventTracker_capstoneProject.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import johnoliveira.eventTracker_capstoneProject.dto.UserDTO;
import johnoliveira.eventTracker_capstoneProject.entities.User;
import johnoliveira.eventTracker_capstoneProject.exceptions.UnauthorizedException;
import johnoliveira.eventTracker_capstoneProject.services.UserService;
import johnoliveira.eventTracker_capstoneProject.tools.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JWT jwt;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        // Escludi gli endpoint pubblici
        if (requestURI.startsWith("/auth/") || requestURI.equals("/events/external")) {
            chain.doFilter(request, response); // Salta l'autenticazione per questi endpoint
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                // Verifica il token
                jwt.verifyToken(token);

                // Estrai l'ID utente dal token
                String userId = jwt.extractUserIdFromToken(token);

                // Recupera l'utente dal database
                UserDTO user = userService.getUserById(UUID.fromString(userId));

                // Imposta l'utente autenticato nel contesto di Spring Security
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token non valido");
                return;
            }
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token mancante o non valido");
            return;
        }

        chain.doFilter(request, response);
    }
}


