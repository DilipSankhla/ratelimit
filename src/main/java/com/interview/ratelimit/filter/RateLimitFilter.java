//package com.interview.ratelimit.filter;
//
//import com.interview.ratelimit.exception.LimitExceededException;
//import com.interview.ratelimit.service.RateLimitService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.annotation.Order;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.HttpClientErrorException;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//
//@Component
//@Order(1)
//public class RateLimitFilter implements Filter {
//
//    private static final String CLIENT_ID = "clientid";
//
//    @Autowired
//    RateLimitService singleNodeApiRateLimitService;
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
//            throws IOException, ServletException {
////        try {
////
////      System.out.println("servletRequest = " + servletRequest);
////            HttpServletRequest request = (HttpServletRequest) servletRequest;
////
////            rateLimitService.check(request.getRequestURI(), request.getHeader(CLIENT_ID));
////        } catch (LimitExceededException e) {
////            throw new HttpClientErrorException(HttpStatus.TOO_MANY_REQUESTS);
////        }
//
//        filterChain.doFilter(servletRequest,servletResponse);
//    }
//
//}
