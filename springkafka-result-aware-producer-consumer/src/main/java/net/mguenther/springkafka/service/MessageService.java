package net.mguenther.springkafka.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Markus Günther (markus.guenther@gmail.com)
 * @author Boris Fresow (bfresow@gmail.com)
 */
@Service
public class MessageService {

    private static final Logger log = LoggerFactory.getLogger(MessageService.class);

    private final List<String> receivedMessages = Collections.synchronizedList(new ArrayList<>());

    public void onMessageReceived(final String message) {
        receivedMessages.add(message);
        log.info("Stored message {} for future reference.", message);
    }

    public List<String> getReceivedMessages() {
        final List<String> receivedMessages = new ArrayList<>();
        receivedMessages.addAll(this.receivedMessages);
        return Collections.unmodifiableList(receivedMessages);
    }
}

