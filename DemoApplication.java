package com.example.demo;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class DemoApplication implements CommandLineRunner {

    @Autowired
    private SampleService sampleService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private Environment env;

    @Autowired
    private UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Spring Boot Demo Application started");
        sampleService.printProfile();

        eventPublisher.publishEvent(new CustomEvent(this, "Application started"));

        User user = new User(null, "John Doe", 25);
        userRepository.save(user);
        System.out.println("Saved user: " + user);
    }
}

// ---------- REST CONTROLLER ----------
@RestController
@RequestMapping("/api")
class SampleController {

    @Autowired
    private SampleService sampleService;

    @GetMapping("/hello")
    public String hello(@RequestParam(defaultValue = "World") String name) {
        return sampleService.greet(name);
    }

    @PostMapping("/users")
    public User createUser(@Valid @RequestBody User user) {
        return sampleService.createUser(user);
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return sampleService.getAllUsers();
    }
}

// ---------- SERVICE ----------
@Service
@Validated
class SampleService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Environment env;

    @Autowired
    private AsyncService asyncService;

    public String greet(@NotBlank String name) {
        return "Hello, " + name + "!";
    }

    @Transactional
    public User createUser(@Valid User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void printProfile() {
        String active = env.getActiveProfiles().length > 0 ? env.getActiveProfiles()[0] : "default";
        System.out.println("Active profile: " + active);
    }

    public void runAsyncTask() {
        asyncService.performAsync();
    }
}

// ---------- ASYNC SERVICE ----------
@Service
class AsyncService {

    @Async
    public CompletableFuture<String> performAsync() throws InterruptedException {
        Thread.sleep(1000);
        System.out.println("Async task completed at " + LocalDateTime.now());
        return CompletableFuture.completedFuture("Done");
    }
}

// ---------- REPOSITORY ----------
@Repository
class UserRepository {

    private final Map<Long, User> storage = new ConcurrentHashMap<>();
    private long counter = 1;

    public User save(User user) {
        if (user.getId() == null) {
            user.setId(counter++);
        }
        storage.put(user.getId(), user);
        return user;
    }

    public List<User> findAll() {
        return new ArrayList<>(storage.values());
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }
}

// ---------- ENTITY ----------
class User {

    private Long id;

    @NotBlank
    private String name;

    @NotNull
    @Min(0)
    private Integer age;

    public User() {
    }

    public User(Long id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}

// ---------- EVENTS ----------
class CustomEvent extends ApplicationEvent {
    private final String message;

    public CustomEvent(Object source, String message) {
        super(source);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

@Component
class EventListenerBean {

    @org.springframework.context.event.EventListener
    public void handleCustomEvent(CustomEvent event) {
        System.out.println("Received event: " + event.getMessage());
    }
}

// ---------- CONFIGURATION ----------
@Configuration
class AppConfig {

    @Bean
    @Primary
    public String sampleBean() {
        return "I am a sample bean";
    }

    @Bean
    @Scope("prototype")
    public PrototypeBean prototypeBean() {
        return new PrototypeBean();
    }
}

class PrototypeBean {
    public PrototypeBean() {
        System.out.println("Prototype bean created");
    }
}

// ---------- SCHEDULING ----------
@Component
class ScheduledTasks {

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        System.out.println("Current time: " + LocalDateTime.now());
    }
}

// ---------- ASPECT ----------
@Aspect
@Component
class LoggingAspect {

    @Around("execution(* com.example.demo.SampleService.*(..))")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("Before method: " + joinPoint.getSignature().getName());
        Object result = joinPoint.proceed();
        System.out.println("After method: " + joinPoint.getSignature().getName());
        return result;
    }
}

// ---------- CONFIG PROPERTIES ----------
@Component
@ConfigurationProperties(prefix = "app")
class AppProperties {
    private String name;
    private int timeout;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}

// ---------- LIFECYCLE ----------
@Component
class LifecycleBean {

    @PostConstruct
    public void init() {
        System.out.println("LifecycleBean initialized");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("LifecycleBean destroyed");
    }
}

// ---------- VALIDATION ----------
@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(javax.validation.ConstraintViolationException.class)
    public Map<String, String> handleValidationExceptions(javax.validation.ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(cv -> {
            String path = cv.getPropertyPath().toString();
            errors.put(path, cv.getMessage());
        });
        return errors;
    }
}
