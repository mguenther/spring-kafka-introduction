package net.mguenther.springkafka.kafka;

import net.mguenther.kafka.junit.EmbeddedKafkaCluster;
import net.mguenther.kafka.junit.KeyValue;
import net.mguenther.kafka.junit.ObserveKeyValues;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static net.mguenther.kafka.junit.EmbeddedKafkaCluster.provisionWith;
import static net.mguenther.kafka.junit.EmbeddedKafkaClusterConfig.defaultClusterConfig;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author Markus Günther (markus.guenther@gmail.com)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
public class UsingKafkaJUnitOnlyTest {

    private static final String TOPIC_NAME = "test";

    private static EmbeddedKafkaCluster kafka = provisionWith(defaultClusterConfig());

    @Autowired
    private ResultAwareMessageProducer producer;

    @BeforeClass
    public static void prepareEnvironment() {
        kafka.start();
        System.setProperty("spring.kafka.bootstrap-servers", kafka.getBrokerList());
    }

    @AfterClass
    public static void tearDown() {
        if (kafka != null) kafka.stop();
    }

    @Test
    public void producerShouldPublishMessageToKafkaTopic() throws InterruptedException {

        producer.sendMessage("message");

        List<KeyValue<String, String>> records = kafka.observe(ObserveKeyValues.on(TOPIC_NAME, 1));

        assertThat(records.get(0).getValue(), is("message"));
    }
}
