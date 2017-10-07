package net.mguenther.springkafka.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Markus Günther (markus.guenther@gmail.com)
 * @author Boris Fresow (bfresow@gmail.com)
 */
@Component
@Profile("test")
public class InstrumentableTopicListener {

    private static final Logger log = LoggerFactory.getLogger(InstrumentableTopicListener.class);

    private final List<ConsumerRecord<?, ?>> receivedMessages = new ArrayList<>();

    private int numberOfReceivedMessages = 0;

    @KafkaListener(topics = "test", group = "test-group")
    public void listen(final ConsumerRecord<?, ?> record) throws Exception {
        log.info("Received message: {}", record);
        numberOfReceivedMessages++;
        receivedMessages.add(record);
    }

    public int getNumberOfReceivedMessages() {
        return numberOfReceivedMessages;
    }

    public List<ConsumerRecord<?, ?>> getReceivedMessages() {
        final List<ConsumerRecord<?, ?>> copyOfReceivedMessages = new ArrayList<>();
        copyOfReceivedMessages.addAll(receivedMessages);
        return copyOfReceivedMessages;
    }
}
