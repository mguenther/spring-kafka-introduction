package net.mguenther.springkafka.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * @author Markus Günther (markus.guenther@gmail.com)
 * @author Boris Fresow (bfresow@gmail.com)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
@Ignore("KafkaEmbedded as ClassRule fails for subsequent tests that use the same ClassRule. Ignored until we know how to fix this.")
public class WithExplicitContainerTest {

    private static final Logger log = LoggerFactory.getLogger(WithExplicitContainerTest.class);

    private static final String TOPIC_NAME = "test";

    @ClassRule
    public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, TOPIC_NAME);

    @Autowired
    private ResultAwareMessageProducer producer;

    private KafkaMessageListenerContainer<String, String> container;

    private BlockingQueue<ConsumerRecord<String, String>> records;

    @BeforeClass
    public static void prepareEnvironment() {
        System.setProperty("spring.kafka.bootstrap-servers", embeddedKafka.getBrokersAsString());
    }

    @Before
    public void prepareTest() throws Exception {
        Map<String, Object> consumerProperties = KafkaTestUtils.consumerProps("sender", "false", embeddedKafka); // set up Kafka consumer properties
        DefaultKafkaConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<String, String>(consumerProperties); // create a ConsumerFactory
        ContainerProperties containerProperties = new ContainerProperties(TOPIC_NAME); // set the topics that ought to be consumed
        records = new LinkedBlockingQueue<>(); // create a thread safe queue to store the received message
        container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
        container.setupMessageListener((MessageListener<String, String>) record -> {
            log.debug("test-listener received message='{}'", record.toString());
            records.add(record);
        });
        container.start();
        ContainerTestUtils.waitForAssignment(container, embeddedKafka.getPartitionsPerTopic());
    }

    @After
    public void cleanupAfterTest() {
        container.stop();
    }

    @Test
    public void producerShouldPublishMessageToKafkaTopic() throws InterruptedException {

        producer.sendMessage("message");

        ConsumerRecord<String, String> record = records.poll(10, TimeUnit.SECONDS);

        assertNotNull(record);
        assertThat(record.value(), is("message"));
    }
}
