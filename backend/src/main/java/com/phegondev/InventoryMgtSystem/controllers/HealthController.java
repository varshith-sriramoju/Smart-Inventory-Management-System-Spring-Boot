package com.phegondev.InventoryMgtSystem.controllers;

import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.SQLException;

@RestController
@RequestMapping("/api/health")
@RequiredArgsConstructor
public class HealthController {

    private final DataSource dataSource;

    @GetMapping("/db")
    public ResponseEntity<String> checkDb() {
        try (Connection conn = dataSource.getConnection()) {
            // Simple validation: ensure we can get a connection
            boolean valid = conn.isValid(2);
            return valid ? ResponseEntity.ok("ok") : ResponseEntity.status(500).body("db-not-valid");
        } catch (SQLException e) {
            return ResponseEntity.status(500).body("db-error: " + e.getMessage());
        }
    }
}
