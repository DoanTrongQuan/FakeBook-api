package com.example.profileservice.filters;


import com.example.profileservice.utils.JwtTokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter{
    @Value("${api.prefix}")
    private String apiPrefix;

    @Value("${jwt.expiration}")
    private int expiration;

    private final JwtTokenUtils jwtTokenUtil;

    @Override
    protected void doFilterInternal(@NonNull  HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {

            final String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")){
                filterChain.doFilter(request, response);
                return;
            }

            final String token = authHeader.substring(7);
            final String email = jwtTokenUtil.extractEmail(token);

//            User user = userRepo.findByEmail(email).orElse(null);
//            if (email != null
//                    && SecurityContextHolder.getContext().getAuthentication() == null) {
//                User userDetails = (User) userDetailsService.loadUserByUsername(email);
//                if(jwtTokenUtil.validateToken(token, userDetails)) {
//                    UsernamePasswordAuthenticationToken authenticationToken =
//                            new UsernamePasswordAuthenticationToken(
//                                    userDetails,
//                                    null,
//                                    userDetails.getAuthorities()
//                            );
//                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//
//                }
//            }

            filterChain.doFilter(request, response); //enable bypass
        }catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        }
    }
    private boolean isBypassToken(@NonNull  HttpServletRequest request) {

        final List<Pair<String, String>> bypassTokens = Arrays.asList(
                Pair.of("/auth/create-user", "POST"),
                Pair.of("/auth/login", "POST")
//                Pair.of(String.format("/seat/info"), "POST")

//                Pair.of(String.format("%s/seat/update-seat-status", apiPrefix), "PUT")
        );
        for(Pair<String, String> bypassToken: bypassTokens) {
            if (request.getServletPath().contains(bypassToken.getFirst()) &&
                    request.getMethod().equals(bypassToken.getSecond())) {
                return true;
            }
        }
        return false;
    }
}
