package com.lsx.service.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class TestAspect {

    Logger logger = LoggerFactory.getLogger(TestAspect.class);

    //第一个*表示方法返回任意值
    //第二个*表示包下任意类
    //第三个*表示任意方法
    //（**） 表示任意参数
    @Pointcut("execution(* com.lsx.service.controller.*.*(..))")
    public void pc1(){

    }

    //前置通知
    //可以通过jp获取方法名 修饰符信息等
    @Before(value = "pc1()")
    public void before(JoinPoint jp){
        String name = jp.getSignature().getName();
        logger.info(name + "方法开始执行");
    }

    //后置通知 ， 方法执行后执行
    @After(value = "pc1()")
    public void after(JoinPoint jp){
        String name = jp.getSignature().getName();
        logger.info(name + "方法执行结束");
    }

    //返回通知， 可以获取当前的返回值，
    @AfterReturning(value = "pc1()", returning = "result")
    public void afterReturning(JoinPoint jp, Object result){
        String name = jp.getSignature().getName();
        logger.info(name + "方法的返回值为：" +result);
    }

    @AfterThrowing(value = "pc1()", throwing = "e")
    public void afterThrowing(JoinPoint jp, Exception e){
        String name = jp.getSignature().getName();
        logger.info(name + "方法抛出的异常为：" + e.getMessage());
    }


    //环绕通知， 可以实现 前置通知/后置通知/异常通知/返回通知 等功能
    @Around("pc1()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable{
        return pjp.proceed();
    }

}
