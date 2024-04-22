package com.yule.dashboard.pbl.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
@Slf4j
public class RetryAspect {

    @Around("@annotation(retry)")
    public Object retry(ProceedingJoinPoint joinPoint, Retry retry) throws Throwable {
        RuntimeException ex = null;
        for (int cur = 1; cur <= retry.value(); cur++) {
            try {
                return joinPoint.proceed();
            } catch (RuntimeException e) {
                log.error("error", e);
                ex = e;
            }
        }
        if (retry.isThrowable()) throw ex;
        return null;
    }

}
