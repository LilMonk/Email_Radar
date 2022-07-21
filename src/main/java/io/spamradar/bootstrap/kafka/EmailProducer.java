package io.spamradar.bootstrap.kafka;

import io.spamradar.bootstrap.email.model.CivilisedEmail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Kafka producer for email objects.
 */
@AllArgsConstructor
@Slf4j
@Component
public class EmailProducer {
    private final KafkaTemplate<String, CivilisedEmail> kafkaTemplate;

    /**
     * Send {@link CivilisedEmail} to the given topic.
     *
     * @param civilisedEmail instance to be sent.
     * @param topic          in which the {@link CivilisedEmail} instance to be sent.
     */
    public void sendMessage(CivilisedEmail civilisedEmail, String topic) {
        log.info("Producing message: {}", civilisedEmail.getSubject());
        kafkaTemplate.send(topic, "key", civilisedEmail)
                .addCallback(
                        result -> log.info("Message sent to topic({}) : {}", topic, civilisedEmail.getSubject()),
                        ex -> log.error("Failed to send message", ex)
                );
    }
}
