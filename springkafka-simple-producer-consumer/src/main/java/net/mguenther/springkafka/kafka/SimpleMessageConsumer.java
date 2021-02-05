package net.mguenther.springkafka.kafka;

import net.mguenther.springkafka.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author Markus Günther (markus.guenther@gmail.com)
 * @author Boris Fresow (bfresow@gmail.com)
 */
@Component
public class SimpleMessageConsumer {

    private static final Logger log = LoggerFactory.getLogger(SimpleMessageConsumer.class);

    private final MessageService messageService;

    @Autowired
    public SimpleMessageConsumer(final MessageService messageService) {
        this.messageService = messageService;
    }

    @KafkaListener(topics = "${spring-kafka-introduction.topic}", groupId = "simple-kafka-example")
    public void listen(final String message) {
        log.info("Received message: {}.", message);
        messageService.onMessageReceived(message);
    }
}
