package com.bank.userManagement.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;

/**
 * Configuration for rate limiting implementation
 * Swagger is currently whitelisted from rate limiting
 * Allows 5 requests per minute currently
 */
@Configuration
public class RateLimitingConfig {

    @Bean
    public Bucket bucket() {
        Bandwidth limit = Bandwidth.classic(5, Refill.greedy(5, Duration.ofMinutes(1)));
        return Bucket4j.builder().addLimit(limit).build();
    }

    @Bean
    public Filter rateLimitFilter(Bucket bucket) {
        return new RateLimitFilter(bucket);
    }

    @WebFilter("/*")
    public static class RateLimitFilter extends OncePerRequestFilter {

        private final Bucket bucket;

        public RateLimitFilter(Bucket bucket) {
            this.bucket = bucket;
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {

            if (request.getRequestURI().startsWith("/swagger-ui") || request.getRequestURI().startsWith("/v3/api-docs")) {
                filterChain.doFilter(request, response);
                return;
            }

            if (bucket.tryConsume(1)) {
                filterChain.doFilter(request, response);
            } else {
                response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
                response.getWriter().write("Too many requests. Please try again later.");
            }
        }
    }
}

