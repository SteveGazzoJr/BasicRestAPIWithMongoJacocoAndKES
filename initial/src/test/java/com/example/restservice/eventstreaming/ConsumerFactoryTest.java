package com.example.restservice.eventstreaming;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConsumerFactoryTest {

    private ConsumerFactory consumerFactory = new ConsumerFactory();

    @Test
    void testGetConsumerReturnsConsumer() {
        //this is just jacoco food
        KafkaConsumer consumer = consumerFactory.getConsumer();

        assertTrue(consumer instanceof KafkaConsumer);
    }
}