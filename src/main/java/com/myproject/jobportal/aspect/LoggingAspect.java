package com.myproject.jobportal.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

// @Around("@annotation(com.eazybytes.jobportal.aspects.LogAspect)")
@Around("execution(* com.myproject.jobportal..*.*(..))")
public Object logAndMeasureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
	String methodName = joinPoint.getSignature().toShortString();
	Object[] methodArgs = joinPoint.getArgs();
	log.info("➡️ Entering method: {}", methodName);
	log.info("📥 Arguments passed: {}", Arrays.toString(methodArgs));
	// Proceed with actual business method
	Object result = null;
	try {
		result = joinPoint.proceed(); //represents actual method call for e.g. if its String title = jobService.getJobTitle(101L);
		//if we call jobService.getJobTitle(101L), Proxy Object intercepts that call and grab the full metadata of the method which is supposed to be call
		//then in the bean of this aspect class i,e. LoggingAndPerformanceAspect
		//Proxy object calls the method and passes full data of the call to the ProceedingJoinPoint joinPoint
		//and this same line joinPoint.proceed(); will run actual jobService.getJobTitle(101L); and get back the result
		//and then from here the result be returned as response.
	} finally {
		log.info("✅ Method executed successfully: {}", methodName);
		log.info("[Info] Method {} returned {} ", methodName);
	}
	
	return result;
	
}
}

