package com.example.demo.controller;

import com.example.demo.util.AnnotationInspector;
import com.example.demo.util.AnnotationInspector.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * REST controller that exposes endpoints to analyze and interpret annotations
 * using the AnnotationInspector utility.
 */
@RestController
@RequestMapping("/annotations")
public class AnnotationInspectorController {

    @Autowired
    private AnnotationInspector inspector;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private DemoService demoService;

    /**
     * Analyze a bean by name (from Spring context).
     *
     * Example: GET /annotations/analyze?beanName=demoService
     */
    @GetMapping("/analyze")
    public Map<String, Object> analyzeBean(@RequestParam String beanName) {
        Object bean = context.getBean(beanName);
        Class<?> type = bean.getClass();

        Map<String, Object> result = new LinkedHashMap<>();
        List<String> classAnnotations = new ArrayList<>();
        for (Annotation ann : inspector.getClassAnnotations(type)) {
            classAnnotations.add(ann.annotationType().getName());
        }

        Map<String, List<String>> methodAnnotations = new LinkedHashMap<>();
        for (Map.Entry<String, List<Annotation>> e : inspector.getMethodAnnotations(type).entrySet()) {
            List<String> anns = new ArrayList<>();
            for (Annotation a : e.getValue()) {
                anns.add(a.annotationType().getName());
            }
            methodAnnotations.put(e.getKey(), anns);
        }

        result.put("beanName", beanName);
        result.put("classAnnotations", classAnnotations);
        result.put("methodAnnotations", methodAnnotations);
        return result;
    }

    /**
     * Call DemoService.createOrder to trigger @Audit interception.
     *
     * Example: POST /annotations/create-order?product=Laptop
     */
    @PostMapping("/create-order")
    public String createOrder(@RequestParam String product) {
        return demoService.createOrder(product);
    }

    /**
     * Call DemoService.viewOrder to trigger @Audit interception.
     *
     * Example: GET /annotations/view-order?id=42
     */
    @GetMapping("/view-order")
    public String viewOrder(@RequestParam int id) {
        return demoService.viewOrder(id);
    }

    /**
     * Call DemoService.noAuditMethod (no AOP interception).
     *
     * Example: GET /annotations/no-audit
     */
    @GetMapping("/no-audit")
    public String noAudit() {
        return demoService.noAuditMethod();
    }

    /**
     * Show annotations on this controller itself.
     *
     * Example: GET /annotations/controller-info
     */
    @GetMapping("/controller-info")
    public Map<String, Object> controllerInfo() throws Exception {
        Map<String, Object> info = new HashMap<>();
        Class<?> type = this.getClass();

        List<String> classAnns = new ArrayList<>();
        for (Annotation ann : inspector.getClassAnnotations(type)) {
            classAnns.add(ann.annotationType().getSimpleName());
        }

        Map<String, List<String>> methodAnns = new HashMap<>();
        for (Method m : type.getDeclaredMethods()) {
            List<String> anns = new ArrayList<>();
            for (Annotation ann : m.getAnnotations()) {
                anns.add(ann.annotationType().getSimpleName());
            }
            methodAnns.put(m.getName(), anns);
        }

        info.put("classAnnotations", classAnns);
        info.put("methodAnnotations", methodAnns);
        return info;
    }
}
