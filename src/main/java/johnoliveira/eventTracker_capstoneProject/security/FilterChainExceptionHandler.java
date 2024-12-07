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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

@Component
public class FilterChainExceptionHandler extends OncePerRequestFilter {
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;
    @Autowired
    private JWT jwt;
    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {

                jwt.verifyToken(token);
                String userId = jwt.extractUserIdFromToken(token);

                // recupera l'utente dal database come UserDTO
                UserDTO userDTO = userService.getUserById(UUID.fromString(userId));
                if (userDTO == null) {
                    throw new UnauthorizedException("Utente non trovato con ID: " + userId);
                }

                // log dell'utente autenticato
                System.out.println("Utente autenticato: " + userDTO.email());

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDTO,
                                null,
                                new ArrayList<>()
                        );
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (UnauthorizedException e) {
                System.err.println("Errore JWT: " + e.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
                return;
            }
        }

        chain.doFilter(request, response);
    }
}
