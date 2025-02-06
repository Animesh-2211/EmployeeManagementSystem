package com.example.employeemanagement.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.entity.Role;
import com.example.employeemanagement.service.EmployeeService;
import com.example.employeemanagement.service.SessionService;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private EmployeeService employeeService;

    // Register a new employee (anyone)
    @PostMapping("/register")
    public Employee register(@RequestBody Employee employee) {
        return employeeService.registerEmployee(employee);
    }

    // Login (anyone)
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestParam String email, @RequestParam String password) {
        Employee employee = employeeService.login(email, password);
        String sessionId = sessionService.createSession(employee);
        Map<String, String> response = new HashMap<>();
        response.put("sessionId", sessionId);
        return ResponseEntity.ok(response);
    }

    // Get all employees (Admin only)
    @GetMapping
    public ResponseEntity<Page<Employee>> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(required = false) String search,
            @RequestHeader String sessionId) {
        Employee loggedInEmployee = sessionService.getSession(sessionId);
        if (loggedInEmployee == null || !loggedInEmployee.getRole().equals(Role.ADMIN)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        if (search != null) {
            return ResponseEntity.ok(employeeService.searchEmployees(search, pageable));
        }
        return ResponseEntity.ok(employeeService.getAllEmployees(pageable));
    }

    // Get employee by ID (Admin can see any employee, Employee can see only their
    // own data)
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id, @RequestHeader String sessionId) {
        Employee loggedInEmployee = sessionService.getSession(sessionId);
        if (loggedInEmployee == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Employee employee = employeeService.getEmployeeById(id);
        if (loggedInEmployee.getRole().equals(Role.ADMIN) || loggedInEmployee.getId().equals(id)) {
            return ResponseEntity.ok(employee);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    // Update employee (Admin only)
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable Long id,
            @RequestBody Employee employeeDetails,
            @RequestHeader String sessionId) {
        Employee loggedInEmployee = sessionService.getSession(sessionId);
        if (loggedInEmployee == null || !loggedInEmployee.getRole().equals(Role.ADMIN)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(employeeService.updateEmployee(id, employeeDetails));
    }

    // Delete employee (Admin only)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id, @RequestHeader String sessionId) {
        Employee loggedInEmployee = sessionService.getSession(sessionId);
        if (loggedInEmployee == null || !loggedInEmployee.getRole().equals(Role.ADMIN)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        employeeService.deleteEmployee(id);
        return ResponseEntity.ok().build();
    }

}