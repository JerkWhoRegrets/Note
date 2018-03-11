import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import repository.DbServiceRepository;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = DbServiceRepository.class)

public class Application {
    public static void main(String[] args){
        SpringApplication.run(Application.class,args);
    }
}
