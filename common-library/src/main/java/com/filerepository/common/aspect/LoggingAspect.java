package com.filerepository.common.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);


    
    @Before("execution(* com.filerepository.*.service.*.*(..))")
    public void logBeforeMethodExecution(JoinPoint joinPoint) {
        log.info("Executing method: {}.{} with arguments: {}", 
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                joinPoint.getArgs());
    }
    
    @AfterReturning(pointcut = "execution(* com.filerepository.*.service.*.*(..))", returning = "result")
    public void logAfterMethodSuccess(JoinPoint joinPoint, Object result) {
        log.info("Method {}.{} completed successfully", 
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName());
    }
    
    @AfterThrowing(pointcut = "execution(* com.filerepository.*.service.*.*(..))", throwing = "exception")
    public void logAfterMethodException(JoinPoint joinPoint, Exception exception) {
        log.error("Method {}.{} threw exception: {}", 
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), 
                exception.getMessage(), exception);
    }
}
