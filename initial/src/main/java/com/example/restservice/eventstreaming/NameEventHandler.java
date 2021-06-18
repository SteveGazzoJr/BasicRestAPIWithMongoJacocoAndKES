package com.example.restservice.eventstreaming;

import com.example.restservice.dataaccess.daos.NameDAO;
import com.example.restservice.managers.NamesManager;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@ComponentScan("com.example.restservice.managers")
public class NameEventHandler implements
        ApplicationListener<ContextRefreshedEvent> {

    private ConsumerFactory consumerFactory;

    private NamesManager namesManager;

    @Autowired
    public NameEventHandler(NamesManager namesManager,
                            ConsumerFactory consumerFactory){
        this.namesManager = namesManager;
        this.consumerFactory = consumerFactory;
    }

    public void listenForKafkaEvent() {

        KafkaConsumer consumer = consumerFactory.getConsumer();
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

        while(true) {
            for (ConsumerRecord<String, String> record : records) {
                namesManager.save(new NameDAO(null, record.value()));
                System.out.println(record.value());
            }
            records = consumer.poll(Duration.ofMillis(100));
        }
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        listenForKafkaEvent();
    }
}
