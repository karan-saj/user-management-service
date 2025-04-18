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
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;

@Configuration
public class RateLimitingConfig {

    // Bean to define the rate-limiting bucket
    @Bean
    public Bucket bucket() {
        Bandwidth limit = Bandwidth.classic(5, Refill.greedy(5, Duration.ofMinutes(1)));
        return Bucket4j.builder().addLimit(limit).build();
    }

    // Define the rate-limiting filter
    @Bean
    public Filter rateLimitFilter(Bucket bucket) {
        return new RateLimitFilter(bucket);
    }

    // The custom rate-limiting filter
    @WebFilter("/*")  // Apply to all requests
    public static class RateLimitFilter extends OncePerRequestFilter {

        private final Bucket bucket;

        public RateLimitFilter(Bucket bucket) {
            this.bucket = bucket;
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {

            // Check if the request URL is for Swagger UI or API docs
            if (request.getRequestURI().startsWith("/swagger-ui") || request.getRequestURI().startsWith("/v3/api-docs")) {
                // If it's Swagger, skip the rate limiting
                filterChain.doFilter(request, response);
                return;
            }

            // Otherwise, apply rate limiting
            if (bucket.tryConsume(1)) {
                filterChain.doFilter(request, response);
            } else {
                // Return HTTP 429 if rate limit is exceeded
                response.setStatus(429);
                response.getWriter().write("Too many requests. Please try again later.");
            }
        }
    }
}

