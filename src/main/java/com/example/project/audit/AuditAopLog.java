package com.example.project.audit;

import com.example.project.model.audit.AuditLog;
import com.example.project.service.AuditService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.sql.Timestamp;
import java.util.*;


import com.alibaba.fastjson.JSON;

@Aspect
@Component
@AllArgsConstructor
public class AuditAopLog {
    private final AuditService auditService;

    @Pointcut("execution(public * com.example.project.controller..*.*(..))")
    private void controllerAspect() {
    }

    @Around("controllerAspect()")
    public Object process(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        Object result = joinPoint.proceed();
        Timestamp timestamp = new Timestamp(new Date().getTime());
        if (authentication.getName() != null) {
            AuditLog requestLog = new AuditLog();
            requestLog.setUrl(request.getRequestURL().toString());
            requestLog.setRequestType(request.getMethod());
            requestLog.setRequestParam(JSON.toJSONString(filterParamList(joinPoint)));
            requestLog.setResponseParam(JSON.toJSONString(result));
            requestLog.setCreatedBy(authentication.getName());
            requestLog.setCreatedByRole(authentication.getAuthorities().toString());
            requestLog.setCreatedDate(timestamp.toLocalDateTime());
            auditService.save(requestLog);
        }
        return result;
    }

    private List<Object> filterParamList(ProceedingJoinPoint joinPoint) {
        List<Object> paramsList = new ArrayList<>();
        Object[] arrays = joinPoint.getArgs();
        for (Object array : arrays) {
            if (!(array instanceof HttpServletRequest)) {
                paramsList.add(array);
            }
        }
        return paramsList;
    }
}