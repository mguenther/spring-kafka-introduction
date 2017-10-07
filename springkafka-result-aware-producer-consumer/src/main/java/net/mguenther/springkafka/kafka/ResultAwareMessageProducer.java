package net.mguenther.springkafka.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

/**
 * @author Markus Günther (markus.guenther@gmail.com)
 * @author Boris Fresow (bfresow@gmail.com)
 */
@Component
public class ResultAwareMessageProducer {

    private static final Logger log = LoggerFactory.getLogger(ResultAwareMessageProducer.class);

    private final String topicName;

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public ResultAwareMessageProducer(@Value("${spring-kafka-introduction.topic}") final String topicName,
                                      final KafkaTemplate<String, String> kafkaTemplate) {
        this.topicName = topicName;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(final String message) {
        // the return type of send is ListenableFuture<SendResult<String, String>> where the parameterized
        // types of SendResult are the types of the key (String) and value (String) as well
        kafkaTemplate
                .send(topicName, message)
                .addCallback(this::onSuccess, this::onFailure);
    }

    private void onSuccess(final SendResult<String, String> result) {
        log.info("Message has been written to partition {} of topic {} with ingestion timestamp {}.",
                result.getRecordMetadata().partition(),
                result.getRecordMetadata().topic(),
                result.getRecordMetadata().timestamp());
    }

    private void onFailure(final Throwable t) {
        log.warn("Message has not been written to topic {}.", topicName, t);
    }
}
