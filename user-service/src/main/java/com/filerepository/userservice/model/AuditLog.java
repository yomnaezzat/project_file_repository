package com.filerepository.userservice.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String action;

    @Column(nullable = false)
    private String resource;

    @Column(nullable = false)
    private String username;

    @Column(name = "class_name")
    private String className;

    @Column(name = "method_name")
    private String methodName;

    @Column(length = 1000)
    private String arguments;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "duration_ms")
    private Long durationMs;

    @Column(nullable = false)
    private boolean success;

    @Column(name = "error_message", length = 1000)
    private String errorMessage;

    public AuditLog() {
    }

    public AuditLog(Long id, String action, String resource, String username,
                    String className, String methodName, String arguments,
                    LocalDateTime timestamp, Long durationMs, boolean success,
                    String errorMessage) {
        this.id = id;
        this.action = action;
        this.resource = resource;
        this.username = username;
        this.className = className;
        this.methodName = methodName;
        this.arguments = arguments;
        this.timestamp = timestamp;
        this.durationMs = durationMs;
        this.success = success;
        this.errorMessage = errorMessage;
    }

    // Getters and setters

    // --- Builder ---

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String action;
        private String resource;
        private String username;
        private String className;
        private String methodName;
        private String arguments;
        private LocalDateTime timestamp;
        private Long durationMs;
        private boolean success;
        private String errorMessage;

        public Builder action(String action) {
            this.action = action;
            return this;
        }

        public Builder resource(String resource) {
            this.resource = resource;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder className(String className) {
            this.className = className;
            return this;
        }

        public Builder methodName(String methodName) {
            this.methodName = methodName;
            return this;
        }

        public Builder arguments(String arguments) {
            this.arguments = arguments;
            return this;
        }

        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder durationMs(Long durationMs) {
            this.durationMs = durationMs;
            return this;
        }

        public Builder success(boolean success) {
            this.success = success;
            return this;
        }

        public Builder errorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        public AuditLog build() {
            return new AuditLog(
                    null,
                    action,
                    resource,
                    username,
                    className,
                    methodName,
                    arguments,
                    timestamp,
                    durationMs,
                    success,
                    errorMessage
            );
        }
    }
}
