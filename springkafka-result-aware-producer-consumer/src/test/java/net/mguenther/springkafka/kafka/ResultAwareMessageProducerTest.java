package net.mguenther.springkafka.kafka;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author Markus Günther (markus.guenther@gmail.com)
 * @author Boris Fresow (bfresow@gmail.com)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
public class ResultAwareMessageProducerTest {

    private static final String TOPIC_NAME = "test";

    @ClassRule
    public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, TOPIC_NAME);

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    @Autowired
    private ResultAwareMessageProducer simpleMessageProducer;

    @Autowired
    private InstrumentableTopicListener topicListener;

    @BeforeClass
    public static void prepareEnvironment() {
        System.setProperty("spring.kafka.bootstrap-servers", embeddedKafka.getBrokersAsString());
        System.setProperty("spring.kafka.producer.bootstrap-servers", embeddedKafka.getBrokersAsString());
    }

    @Before
    public void prepareTest() throws Exception {
        for (MessageListenerContainer container : kafkaListenerEndpointRegistry.getListenerContainers()) {
            ContainerTestUtils.waitForAssignment(container, embeddedKafka.getPartitionsPerTopic());
        }
    }

    @Test
    public void simpleMessageProducerShouldPublishMessageToKafkaTopic() throws InterruptedException {

        simpleMessageProducer.sendMessage("message");

        int i = 0;
        while (topicListener.getNumberOfReceivedMessages() != 1 && i < 10) {
            Thread.sleep(1_000);
            i++;
        }

        assertThat(topicListener.getNumberOfReceivedMessages(), is(1));
        assertThat(topicListener.getReceivedMessages().get(0).value(), is("message"));
    }
}
