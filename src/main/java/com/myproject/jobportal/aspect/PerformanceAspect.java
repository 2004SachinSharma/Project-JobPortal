package com.myproject.jobportal.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class PerformanceAspect {

@Pointcut(value = "execution(* com.myproject.jobportal..*.*(..))")
void performanceAspectForEntireApp() {
}

@Around(value = "performanceAspectForEntireApp()")
public Object performanceAspect(ProceedingJoinPoint joinpoint) throws Throwable {
	String methodName = joinpoint.getSignature().getName();
//	Before The execution of business logic
	Long startTime = System.currentTimeMillis();
	log.info("[Info] ⏱\uFE0F Starting execution of: {}", methodName);
	
	Object result;
	try {
		result = joinpoint.proceed(); //.proceed() - it runs and captures the result of the original method call
	} /*catch (Throwable ex) {
		Long endTime = System.currentTimeMillis();
		log.info("[Info] ⏱\uFE0F Ending execution of: {}", methodName);
		throw ex;
	}*///We don't even need to catch it here the exception automatically be propagated to the GlobalExceptionHandler
	// (Don't get confused, this for sure not the concept of AOP but the exceptionHandling. if you have learnt it, you know this)
	finally{
		//After the execution of the business logic
		Long endTime = System.currentTimeMillis();
		log.info("[Info] ⏱\uFE0F Ending execution of: {}", methodName);
	}
	
	return result;
}


}
