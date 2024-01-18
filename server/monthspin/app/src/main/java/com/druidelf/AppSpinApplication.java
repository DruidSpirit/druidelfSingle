package com.druidelf;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = {"druidelf","com.druidelf"})
@EntityScan(basePackages = {"druidelf.bean.security","druidelf.bean.spin"})
@EnableJpaRepositories(basePackages = {"druidelf.repository.security","druidelf.repository.spin"})
@SpringBootApplication
public class AppSpinApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppSpinApplication.class, args);
    }
}
