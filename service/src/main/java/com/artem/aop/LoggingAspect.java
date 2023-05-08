package com.artem.aop;

import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
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

        String argsString = Arrays.stream(args)
                .map(Object::toString)
                .collect(Collectors.joining(", "));

        log.info("Invoked method [{}] in class [{}] with args: [{}]", methodName, className, argsString);
    }
}
