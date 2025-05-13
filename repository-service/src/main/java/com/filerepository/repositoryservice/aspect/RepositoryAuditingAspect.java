package com.filerepository.repositoryservice.aspect;

import com.filerepository.common.annotation.Audited;
import com.filerepository.repositoryservice.aop.RepositoryLoggingAspect;
//import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

@Aspect
@Component

public class RepositoryAuditingAspect {
    private static final Logger log = LoggerFactory.getLogger(RepositoryLoggingAspect.class);

    @AfterReturning("@annotation(com.filerepository.common.annotation.Audited)")
    public void logRepositoryOperation(JoinPoint joinPoint) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            Audited auditedAnnotation = method.getAnnotation(Audited.class);
            
            String actionType = auditedAnnotation.action();
            String resource = auditedAnnotation.resource();
            
            Object[] args = joinPoint.getArgs();
            StringBuilder argsStr = new StringBuilder();
            for (Object arg : args) {
                if (arg != null) {
                    argsStr.append(arg.toString().length() > 100 
                            ? arg.toString().substring(0, 100) + "..." 
                            : arg.toString());
                    argsStr.append(", ");
                } else {
                    argsStr.append("null, ");
                }
            }
            
            if (argsStr.length() > 2) {
                argsStr.setLength(argsStr.length() - 2);
            }
            
            log.info("Repository operation - Action: {}, Resource: {}, Method: {}.{}, Args: [{}]", 
                    actionType, 
                    resource, 
                    joinPoint.getTarget().getClass().getSimpleName(), 
                    method.getName(),
                    argsStr);
        } catch (Exception e) {
            log.error("Error in repository auditing aspect", e);
        }
    }
}
