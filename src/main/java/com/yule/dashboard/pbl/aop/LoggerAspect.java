package com.yule.dashboard.pbl.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
@Profile("local")
public class LoggerAspect {

    @Around("execution(* com.yule.dashboard..*Controller*.*(..))")
    public Object logger(ProceedingJoinPoint joinPoint) throws Throwable {
        Object target = joinPoint.getTarget();
        Signature signature = joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();
        log.info("\n***** request: {}, {}, {} *****", target, signature, args);
        Object proceed = joinPoint.proceed();
        log.info("\n***** response: {} *****", proceed);
        return proceed;
    }

}
