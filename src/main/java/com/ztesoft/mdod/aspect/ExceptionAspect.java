package com.ztesoft.mdod.aspect;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.ztesoft.mdod.util.ConstantUtil;

/**
 * 方法执行异常切面
 * @author lyg
 *
 */
@Aspect
@Component
public class ExceptionAspect {

	/**
	 * 切面，指定package位置的方法会调用此方法
	 */
	@Pointcut("execution(* com.ztesoft.mdod.service.impl..*.*(..))")  
    public void exceptionResult(){
		
	}
	  
    /**  
     * 统计方法执行耗时Around环绕通知  
     * @param joinPoint  
     * @return  
     * @throws Throwable 
     */  
	@Around("exceptionResult()")  
    public Object exceptionAround(ProceedingJoinPoint joinPoint){
    	// 定义返回对象、得到方法需要的参数  
    	Object obj = null;  
    	Map<String, Object> resultMap = null;
        Object[] args = joinPoint.getArgs();  
        try {  
            obj = joinPoint.proceed(args);  
        } catch (Throwable e) { 
        	e.printStackTrace();
        	String messageString = "服务异常";
        	resultMap = new HashMap<String, Object>();
        	resultMap.put(ConstantUtil.RESULT_CODE, ConstantUtil.RESULT_SUCCESS_CODE);
        	resultMap.put(ConstantUtil.RESULT_MSG, messageString);
        	resultMap.put(ConstantUtil.RESULT_OBJECT, new HashMap<String, Object>());
        	// 设置事务回滚
        	TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }  
        if(resultMap == null){
            return obj;
        }else {
            return resultMap;
        }
    }  
	
}
