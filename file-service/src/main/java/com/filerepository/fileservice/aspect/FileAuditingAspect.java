package com.filerepository.fileservice.aspect;

import com.filerepository.common.annotation.Audited;
import com.filerepository.fileservice.model.FileHistory;
import com.filerepository.fileservice.repository.FileHistoryRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
public class FileAuditingAspect {

    private final FileHistoryRepository fileHistoryRepository;
    private static final Logger log = LoggerFactory.getLogger(FileAuditingAspect.class);

    public FileAuditingAspect(FileHistoryRepository fileHistoryRepository) {
        this.fileHistoryRepository = fileHistoryRepository;
    }

    @AfterReturning("@annotation(com.filerepository.common.annotation.Audited) && args(fileId, userId, ..)")
    public void logFileOperation(JoinPoint joinPoint, Long fileId, Long userId) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            Audited auditedAnnotation = method.getAnnotation(Audited.class);

            String actionType = auditedAnnotation.action();
            String username = "system"; // Ideally, get from security context or service

            FileHistory fileHistory = new FileHistory();
            fileHistory.setFileId(fileId);
            fileHistory.setActionType(actionType);
            fileHistory.setActionDescription(method.getName() + " operation");
            fileHistory.setUserId(userId);
            fileHistory.setUsername(username);
            fileHistory.setTimestamp(LocalDateTime.now());

            fileHistoryRepository.save(fileHistory);
            log.debug("File operation logged: {}", fileHistory);
        } catch (Exception e) {
            log.error("Error logging file operation", e);
        }
    }

    @AfterReturning(
            "@annotation(com.filerepository.common.annotation.Audited) && execution(* com.filerepository.fileservice.service.FileService.uploadFile(..))")
    public void logFileUpload(JoinPoint joinPoint) {
        try {
            Object[] args = joinPoint.getArgs();
            Long repositoryId = (Long) args[1];
            Long folderId = (Long) args[2];
            Long uploaderId = (Long) args[3];

            log.info("File uploaded to repository={}, folder={} by user={}",
                    repositoryId, folderId, uploaderId);
        } catch (Exception e) {
            log.error("Error logging file upload", e);
        }
    }
}
