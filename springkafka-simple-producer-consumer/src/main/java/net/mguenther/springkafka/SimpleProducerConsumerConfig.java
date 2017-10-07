package net.mguenther.springkafka;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author Markus Günther (markus.guenther@gmail.com)
 * @author Boris Fresow (bfresow@gmail.com)
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "net.mguenther.springkafka")
public class SimpleProducerConsumerConfig {
}
