package com.coronation.captr.userservice.security;

import com.coronation.captr.userservice.util.JsonWebTokenUtil;
import com.sun.istack.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Stream;

/**
 * @author toyewole
 */
@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JsonWebTokenUtil jsonWebTokenUtil;

    @Autowired
    private RequestBean requestBean;


    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private final String[] whitelistedPaths = {
            "/actuator/**",
            "/sign-up"
    };

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.isBlank(authorization) || !authorization.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }


        String token = authorization.split(" ")[1];
        String userName = jsonWebTokenUtil.getUsernameFromToken(token);
        if (!jsonWebTokenUtil.validateToken(token)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        requestBean.setPrincipal(userName);

        log.debug("principal {} ", userName);
        filterChain.doFilter(request, response);


    }

    @Override
    protected boolean shouldNotFilter(@NotNull HttpServletRequest request) throws ServletException {
        return Stream.of(whitelistedPaths).anyMatch(p -> pathMatcher.match(p, request.getServletPath()));
    }


}
