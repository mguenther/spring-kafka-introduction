package net.mguenther.springkafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Markus Günther (markus.guenther@gmail.com)
 * @author Boris Fresow (bfresow@gmail.com)
 */
@SpringBootApplication
public class TransactionsApp {

    public static void main(String[] args) {
        SpringApplication.run(TransactionsApp.class, args);
    }
}
