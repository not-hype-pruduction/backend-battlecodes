package com.battlecodes.backend.configurations.jwt;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import com.battlecodes.backend.configurations.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {

  private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

  @Autowired
  private JwtUtils jwtUtils;

  @Autowired
  private UserDetailsServiceImpl userDetailsService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
          throws ServletException, IOException {
    try {
      logger.info("AuthTokenFilter: filtering request for URI: {}", request.getRequestURI());
      String jwt = parseJwt(request);
      logger.info("AuthTokenFilter: JWT token parsed: {}", jwt);
      if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
        logger.info("AuthTokenFilter: JWT token is valid");
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        logger.info("AuthTokenFilter: Username from JWT token: {}", username);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        logger.info("AuthTokenFilter: Authentication set in security context");
      } else {
        logger.info("AuthTokenFilter: JWT token is not valid or is null");
      }
    } catch (Exception e) {
      logger.error("AuthTokenFilter: Cannot set user authentication: {}", e.getMessage(), e);
    }

    filterChain.doFilter(request, response);
  }

  private String parseJwt(HttpServletRequest request) {
    String headerAuth = request.getHeader("Authorization");
    logger.info("AuthTokenFilter: Authorization header: {}", headerAuth);

    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
      return headerAuth.substring(7);
    }

    return null;
  }
}


