package org.bangbanghub.deliverypersonservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class DeliveryPersonServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeliveryPersonServiceApplication.class, args);
    }

}
