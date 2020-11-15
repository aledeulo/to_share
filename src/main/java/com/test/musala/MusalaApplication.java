package com.test.musala;

import com.test.musala.model.Gateway;
import com.test.musala.repository.GatewayRepository;
import com.test.musala.support.SupportTools;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.stream.LongStream;

@SpringBootApplication
@ComponentScan(basePackages = {"com.test.musala"})
public class MusalaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MusalaApplication.class, args);
    }

    @Bean
    CommandLineRunner init(GatewayRepository repository) {
        StringBuilder builder = new StringBuilder();
        return args -> {
            repository.deleteAll();
            LongStream.range(1, 20)
                    .mapToObj(index -> {
                        String name = "Gateway " + index + " test";
                        String ipAddress = "192.168.0.1" + index;
                        String serial = "";
                        try {
                            serial = SupportTools.toHash(name + ipAddress);
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                        return new Gateway(serial, name, ipAddress, new ArrayList<>());
                    }).map(repository::save)
                    .forEach(System.out::println);
        };
    }
}