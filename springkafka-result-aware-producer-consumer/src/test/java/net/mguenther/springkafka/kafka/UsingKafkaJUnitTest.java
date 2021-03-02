package net.mguenther.springkafka.kafka;

import net.mguenther.kafka.junit.ExternalKafkaCluster;
import net.mguenther.kafka.junit.KeyValue;
import net.mguenther.kafka.junit.ObserveKeyValues;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author Markus Günther (markus.guenther@gmail.com)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
public class UsingKafkaJUnitTest {

    private static final String TOPIC_NAME = "test";

    @ClassRule
    public static EmbeddedKafkaRule kafkaRule = new EmbeddedKafkaRule(1, true, TOPIC_NAME);

    @Autowired
    private ResultAwareMessageProducer producer;

    @BeforeClass
    public static void prepareEnvironment() {
        System.setProperty("spring.kafka.bootstrap-servers", kafkaRule.getEmbeddedKafka().getBrokersAsString());
    }

    @Test
    public void producerShouldPublishMessageToKafkaTopic() throws InterruptedException {

        ExternalKafkaCluster kafka = ExternalKafkaCluster.at(kafkaRule.getEmbeddedKafka().getBrokersAsString());

        producer.sendMessage("message");

        List<KeyValue<String, String>> records = kafka.observe(ObserveKeyValues.on(TOPIC_NAME, 1));

        assertThat(records.get(0).getValue(), is("message"));
    }
}
