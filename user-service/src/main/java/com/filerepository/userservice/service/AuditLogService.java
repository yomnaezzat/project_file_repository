package com.filerepository.userservice.service;

import com.filerepository.userservice.model.AuditLog;
import com.filerepository.userservice.repository.AuditLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

@Service


public class AuditLogService {
    private static final Logger log = LoggerFactory.getLogger(AuditLogService.class);

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }


    @Transactional
    public AuditLog saveAuditLog(AuditLog auditLog) {
        log.debug("Saving audit log: {}", auditLog);
        return auditLogRepository.save(auditLog);
    }

    public List<AuditLog> findByUsername(String username) {
        return auditLogRepository.findByUsername(username);
    }

    public List<AuditLog> findByResource(String resource) {
        return auditLogRepository.findByResource(resource);
    }

    public List<AuditLog> findByTimestampBetween(LocalDateTime start, LocalDateTime end) {
        return auditLogRepository.findByTimestampBetween(start, end);
    }

    public List<AuditLog> findByAction(String action) {
        return auditLogRepository.findByAction(action);
    }

    public List<AuditLog> findAll() {
        return auditLogRepository.findAll();
    }
}
