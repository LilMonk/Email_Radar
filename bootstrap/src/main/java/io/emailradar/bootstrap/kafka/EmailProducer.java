package io.emailradar.bootstrap.kafka;

import io.emailradar.commons.email.model.EmailPayload;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Kafka producer for email objects.
 */
@AllArgsConstructor
@Slf4j
@Component
public class EmailProducer {
    @Autowired
    private final KafkaTemplate<String, EmailPayload> kafkaTemplate;
    private static final String KEY = "key";

    /**
     * Send {@link EmailPayload} to the given topic.
     *
     * @param message message to be sent.
     * @param topic   in which the {@link EmailPayload} instance to be sent.
     */
    public void sendMessage(EmailPayload message, String topic) {
        sendMessage(KEY, message, topic);
    }

    /**
     * Send {@link EmailPayload} to the given topic.
     *
     * @param key     message key.
     * @param message message to be sent.
     * @param topic   in which the {@link EmailPayload} instance to be sent.
     */
    public void sendMessage(String key, EmailPayload message, String topic) {
        log.info("Producing message: {}", message.get_id());
        kafkaTemplate.send(topic, key, message)
                .addCallback(
                        result -> log.info("Message sent to topic({}) : {}", topic, message.get_id()),
                        ex -> log.error("Failed to send message to topic({}) : {} \n Error message : {}",
                                topic, message.get_id(), ex)
                );
    }
}
