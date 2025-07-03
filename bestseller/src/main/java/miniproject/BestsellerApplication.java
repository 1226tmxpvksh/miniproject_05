package miniproject;

import miniproject.config.kafka.KafkaProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.ApplicationContext;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableBinding(KafkaProcessor.class)
@EnableFeignClients
@EntityScan("miniproject.domain")
@EnableJpaRepositories(basePackages = {"miniproject.domain", "miniproject.infra"})
public class BestsellerApplication {

    public static ApplicationContext applicationContext;

    public static void main(String[] args) {
        applicationContext = SpringApplication.run(BestsellerApplication.class, args);
    }
}
