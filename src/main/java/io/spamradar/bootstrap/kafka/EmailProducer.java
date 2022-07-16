package io.spamradar.bootstrap.kafka;

import io.spamradar.bootstrap.model.CivilisedEmail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Slf4j
@Component
public class EmailProducer {
    private final KafkaTemplate<String, CivilisedEmail> kafkaTemplate;

    public void sendMessage(CivilisedEmail civilisedEmail, String topic) {
        log.info("Producing message: {}", civilisedEmail);
        kafkaTemplate.send(topic, "key", civilisedEmail)
                .addCallback(
                        result -> log.info("Message sent to topic({}) : {}", topic, civilisedEmail),
                        ex -> log.error("Failed to send message", ex)
                );
    }
}
