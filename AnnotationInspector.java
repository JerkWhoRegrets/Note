package com.example.demo.util;

import jakarta.annotation.PostConstruct;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * AnnotationInspector - Utility for detecting and interpreting annotations.
 *
 * Demonstrates:
 *  - Reflection-based scanning
 *  - Custom annotation @Audit
 *  - Using Spring's AnnotationUtils
 *  - AOP aspect to intercept @Audit
 *  - Meta-annotation interpretation
 */
@Component
@Aspect
public class AnnotationInspector {

    private static final Logger log = LoggerFactory.getLogger(AnnotationInspector.class);

    // ----------------------------
    // Custom annotation
    // ----------------------------
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface Audit {
        String action();
        String role() default "USER";
    }

    // ----------------------------
    // Lifecycle
    // ----------------------------
    @PostConstruct
    public void init() {
        log.info("AnnotationInspector initialized");
    }

    // ----------------------------
    // Reflection scanning
    // ----------------------------

    /**
     * List annotations present on a class.
     */
    public List<Annotation> getClassAnnotations(Class<?> type) {
        return Arrays.asList(type.getAnnotations());
    }

    /**
     * List annotations present on all methods of a class.
     */
    public Map<String, List<Annotation>> getMethodAnnotations(Class<?> type) {
        Map<String, List<Annotation>> map = new HashMap<>();
        for (Method m : type.getDeclaredMethods()) {
            map.put(m.getName(), Arrays.asList(m.getAnnotations()));
        }
        return map;
    }

    /**
     * List annotations present on all fields of a class.
     */
    public Map<String, List<Annotation>> getFieldAnnotations(Class<?> type) {
        Map<String, List<Annotation>> map = new HashMap<>();
        for (Field f : type.getDeclaredFields()) {
            map.put(f.getName(), Arrays.asList(f.getAnnotations()));
        }
        return map;
    }

    /**
     * Check if annotation is present (including meta-annotations).
     */
    public boolean hasAnnotation(Class<?> type, Class<? extends Annotation> ann) {
        return AnnotationUtils.findAnnotation(type, ann) != null;
    }

    /**
     * Get attribute values of a specific annotation on a method.
     */
    public Map<String, Object> getAnnotationAttributes(Method method, Class<? extends Annotation> annClass) {
        Annotation ann = AnnotationUtils.findAnnotation(method, annClass);
        if (ann == null) return Collections.emptyMap();
        return AnnotationUtils.getAnnotationAttributes(ann);
    }

    // ----------------------------
    // Demo interpretation of @Audit
    // ----------------------------

    public void interpretAuditAnnotation(Class<?> type) {
        for (Method m : type.getDeclaredMethods()) {
            Audit audit = AnnotationUtils.findAnnotation(m, Audit.class);
            if (audit != null) {
                log.info("Method {} requires action='{}' and role='{}'",
                        m.getName(), audit.action(), audit.role());
            }
        }
    }

    // ----------------------------
    // AOP to enforce @Audit at runtime
    // ----------------------------
    @Around("@annotation(audit)")
    public Object aroundAudit(ProceedingJoinPoint pjp, Audit audit) throws Throwable {
        log.info("Intercepted method={}, requiredAction={}, role={}",
                pjp.getSignature().getName(), audit.action(), audit.role());
        // You could inject a security check here
        Object result = pjp.proceed();
        log.info("Method {} executed successfully", pjp.getSignature().getName());
        return result;
    }

    // ----------------------------
    // Example demo class to analyze
    // ----------------------------
    @Component
    public static class DemoService {

        @Audit(action = "CREATE_ORDER", role = "ADMIN")
        public String createOrder(String product) {
            return "Order created for " + product;
        }

        @Audit(action = "VIEW_ORDER")
        public String viewOrder(int id) {
            return "Order " + id;
        }

        public String noAuditMethod() {
            return "No audit";
        }
    }

    // ----------------------------
    // Utility: analyze any bean
    // ----------------------------
    public void analyzeBean(Object bean) {
        Class<?> type = bean.getClass();
        log.info("Analyzing bean class={}", type.getName());
        getClassAnnotations(type).forEach(a -> log.info("Class annotation: {}", a));
        getMethodAnnotations(type).forEach((m, anns) ->
                anns.forEach(a -> log.info("Method={} annotation={}", m, a)));
        getFieldAnnotations(type).forEach((f, anns) ->
                anns.forEach(a -> log.info("Field={} annotation={}", f, a)));
        interpretAuditAnnotation(type);
    }
}
