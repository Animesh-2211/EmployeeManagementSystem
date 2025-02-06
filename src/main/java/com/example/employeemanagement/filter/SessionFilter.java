package com.example.employeemanagement.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.service.SessionService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SessionFilter extends OncePerRequestFilter {

    @Autowired
    private SessionService sessionService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String sessionId = request.getHeader("sessionId");
        if (sessionId == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Session ID is missing");
            return;
        }

        Employee employee = sessionService.getSession(sessionId);
        if (employee == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid session");
            return;
        }

        request.setAttribute("employee", employee);
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/employees/register") || path.startsWith("/employees/login");
    }

}