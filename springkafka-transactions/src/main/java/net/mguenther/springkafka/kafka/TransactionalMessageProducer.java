package net.mguenther.springkafka.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * This {@code TransactionalMessageProducer} showcases how to use a local transaction
 * via {@code KafkaTemplate}.
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 * @author Boris Fresow (bfresow@gmail.com)
 */
@Component
public class TransactionalMessageProducer {

    private static final Logger log = LoggerFactory.getLogger(TransactionalMessageProducer.class);

    private final String topicName;

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public TransactionalMessageProducer(@Value("${spring-kafka-introduction.topic}") final String topicName,
                                        final KafkaTemplate<String, String> kafkaTemplate) {
        this.topicName = topicName;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(final String message) {
        kafkaTemplate.executeInTransaction(t -> {
            final String key = UUID.randomUUID().toString();
            t.send(topicName, key, message);
            log.info("Submitted message {} to topic {}.", message, topicName);
            return true;
        });
    }
}
