package com.example.employeemanagement.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.employeemanagement.entity.Employee;

@Service
public class SessionService {

    private final Map<String, Employee> activeSessions = new HashMap<>();

    public String createSession(Employee employee) {
        String sessionId = UUID.randomUUID().toString();
        activeSessions.put(sessionId, employee);
        return sessionId;
    }

    public Employee getSession(String sessionId) {
        return activeSessions.get(sessionId);
    }

}