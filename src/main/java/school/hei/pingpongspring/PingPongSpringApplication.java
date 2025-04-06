package school.hei.pingpongspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"school.hei.pingpongspring.controller","school.hei.pingpongspring.entity","school.hei.pingpongspring.repository","school.hei.pingpongspring.service"})
public class PingPongSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(PingPongSpringApplication.class, args);
    }

}
