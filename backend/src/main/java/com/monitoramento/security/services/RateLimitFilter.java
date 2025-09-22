package com.monitoramento.security.services;//package com.estoque.security.services;
//
//import io.github.bucket4j.Bandwidth;
//import io.github.bucket4j.Bucket;
//import io.github.bucket4j.Refill;
//import jakarta.servlet.*;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.time.Duration;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Component
//public class RateLimitFilter implements Filter {
//
//    private static final int TOO_MANY_REQUESTS = 429;
//    private final Map<String, Bucket> generalRateLimit = new ConcurrentHashMap<>();
//    private final Map<String, Bucket> loginRateLimit = new ConcurrentHashMap<>();
//
//    private Bucket createBucket(int capacity, int refillTokens, Duration refillDuration) {
//        return Bucket.builder()
//                .addLimit(Bandwidth.classic(capacity, Refill.intervally(refillTokens, refillDuration)))
//                .build();
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//
//        HttpServletRequest httpRequest = (HttpServletRequest) request;
//        String ip = request.getRemoteAddr();
//        String path = httpRequest.getRequestURI();
//
//        if (path.startsWith("/api/webhook/pix")) {
//            chain.doFilter(request, response);
//            return;
//        }
//
//        Bucket bucket;
//        if (path.startsWith("/api/auth/public")) {
//            // 🔐 Limite mais baixo para /public/** (5 requisições por minuto)
//            bucket = loginRateLimit.computeIfAbsent(ip, k -> createBucket(5, 5, Duration.ofMinutes(1)));
//        } else {
//            // 🚀 Limite mais alto para outros endpoints (50 requisições por minuto)
//            bucket = generalRateLimit.computeIfAbsent(ip, k -> createBucket(50, 50, Duration.ofMinutes(1)));
//        }
//
//        if (bucket.tryConsume(1)) {
//            chain.doFilter(request, response);
//        } else {
//            HttpServletResponse httpResponse = (HttpServletResponse) response;
//            httpResponse.setStatus(TOO_MANY_REQUESTS);
//            httpResponse.getWriter().write("Limite de requisições excedido!");
//        }
//    }
//}
