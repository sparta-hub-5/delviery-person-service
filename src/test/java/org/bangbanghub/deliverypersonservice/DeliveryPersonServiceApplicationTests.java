package org.bangbanghub.deliverypersonservice;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration"
})
class DeliveryPersonServiceApplicationTests {

    @Test
    @Disabled("Context load test disabled due to missing msa-common Kafka configuration")
    void contextLoads() {
    }

}
