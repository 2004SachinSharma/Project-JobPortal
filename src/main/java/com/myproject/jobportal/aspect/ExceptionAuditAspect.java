package com.myproject.jobportal.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Slf4j
@Component
public class ExceptionAuditAspect {

@Pointcut(value = "execution( * com.myproject.jobportal..*.*(..))")
void exceptionAuditForEntireApp() {}


@AfterThrowing(pointcut = "exceptionAuditForEntireApp()", throwing = "ex")
public void afterThrowingException(JoinPoint joinPoint, Exception ex) {
	String methodName = joinPoint.getSignature().getName();
	Object[] methodArgs = joinPoint.getArgs();
	
	log.error("❌ Exception occurred in method: {}", methodName);
	log.error("📥 Arguments: {}", Arrays.toString(methodArgs));
	log.error("💥 Exception type: {}", ex.getClass().getSimpleName());
	log.error("🧾 Exception message: {}", ex.getMessage());
// Here you could also:
// - Send metrics
// - Push audit events
// - Trigger alerts
      
      }
}
