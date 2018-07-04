package guru.springframework.aspect;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

	@Pointcut("within(@org.springframework.stereotype.Controller *)")
	public void controller() {
		System.out.println("Advice Run. controller");
	}

	@Pointcut("execution(* *(..))")
	public void method() {
		System.out.println("Advice Run. method");
	}

	@Before("controller() && method() && args(request, ID)")
	public void doAccessCheck(JoinPoint joinPoint, HttpServletRequest request, String ID) {
		System.out.println("Before > ID : " + ID + " for " + joinPoint.getSignature());
	}

	@AfterThrowing(pointcut="controller() && method() && args(request, ID)", throwing = "e")
	public void afterThrowingException(JoinPoint joinPoint, Exception e, HttpServletRequest request, String ID) {
		System.out.println("After Throw > ID : " + ID + " for " + joinPoint.getSignature());
	}

	@AfterReturning("controller() && method() && args(request, ID)")
	public void afterSuccessfulReturn(JoinPoint joinPoint, HttpServletRequest request, String ID) {
		System.out.println("After Return > ID : " + ID + " for " + joinPoint.getSignature());
	}

}