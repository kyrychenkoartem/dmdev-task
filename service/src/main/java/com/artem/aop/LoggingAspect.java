package com.artem.aop;

import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Before("execution(public * com.artem.service.*.*(..)) && !@annotation(org.springframework.security.access.prepost.PreAuthorize)")
    public void logBefore(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();
        log.info("Before invoked method [{}] in class [{}] with args: [{}]", methodName, className, args);
    }

    @After("execution(public void com.artem.service.*.*(..)) && !@annotation(org.springframework.security.access.prepost.PreAuthorize)")
    public void logAfter(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        log.info("After invoked  method [{}] in class [{}]", methodName, className);
    }

    @AfterReturning(pointcut = "execution(public * com.artem.service.*.*(..)) && !@annotation(org.springframework.security.access.prepost.PreAuthorize)", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        log.info("After invoked method [{}] in class [{}] returned [{}]", methodName, className, result);
    }
}
