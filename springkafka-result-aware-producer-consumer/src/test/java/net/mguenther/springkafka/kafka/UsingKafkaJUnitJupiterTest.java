package net.mguenther.springkafka.kafka;

import net.mguenther.kafka.junit.EmbeddedKafkaCluster;
import net.mguenther.kafka.junit.KeyValue;
import net.mguenther.kafka.junit.ObserveKeyValues;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static net.mguenther.kafka.junit.EmbeddedKafkaCluster.provisionWith;
import static net.mguenther.kafka.junit.EmbeddedKafkaClusterConfig.defaultClusterConfig;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
public class UsingKafkaJUnitJupiterTest {

    private static final String TOPIC_NAME = "test";

    private static EmbeddedKafkaCluster kafka = provisionWith(defaultClusterConfig());

    @Autowired
    private ResultAwareMessageProducer producer;

    @BeforeAll
    static void prepareEnvironment() {
        kafka.start();
        System.setProperty("spring.kafka.bootstrap-servers", kafka.getBrokerList());
    }

    @AfterAll
    static void tearDown() {
        if (kafka != null) kafka.stop();
    }

    @Test
    void producerShouldPublishMessageToKafkaTopic() throws InterruptedException {

        producer.sendMessage("message");

        List<KeyValue<String, String>> records = kafka.observe(ObserveKeyValues.on(TOPIC_NAME, 1));

        assertThat(records.get(0).getValue(), is("message"));
    }
}
