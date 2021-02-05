package net.mguenther.springkafka.kafka;

import net.mguenther.springkafka.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * @author Markus Günther (markus.guenther@gmail.com)
 * @author Boris Fresow (bfresow@gmail.com)
 */
@Component
public class AcknowledgingMessageConsumer {

    private static final Logger log = LoggerFactory.getLogger(AcknowledgingMessageConsumer.class);

    private final MessageService messageService;

    @Autowired
    public AcknowledgingMessageConsumer(final MessageService messageService) {
        this.messageService = messageService;
    }

    @KafkaListener(topics = "${spring-kafka-introduction.topic}", groupId = "spring-kafka-transactional-example")
    public void listen(final String message, final Acknowledgment acknowledgment) {
        log.info("Received (yet unacknowledged) message {}.", message);
        messageService.onMessageReceived(message);
        acknowledgment.acknowledge();
        log.info("Acknowledged processing of message {}.", message);
    }
}
