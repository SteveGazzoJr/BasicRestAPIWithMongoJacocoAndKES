package com.example.restservice.eventstreaming;

import com.example.restservice.managers.NamesManager;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class NameEventHandlerTest {

    KafkaConsumer kafkaConsumer = Mockito.mock(KafkaConsumer.class);

    ConsumerFactory consumerFactory = Mockito.mock(ConsumerFactory.class);

    NamesManager namesManager = Mockito.mock(NamesManager.class);

    NameEventHandler nameEventHandler = new NameEventHandler(namesManager, consumerFactory);

    @Test
    public void when_EventsExistInStream_Then_SaveIsCalled() {
        ConsumerRecord<String, String> record =
                new ConsumerRecord<String, String>("filler", 0, 0, "testKey", "testValue");
        List<ConsumerRecord<String, String>> recordList = new ArrayList<>();
        recordList.add(record);
        Map<TopicPartition, List<ConsumerRecord<String, String>>> recordMap = new HashMap<>();
        recordMap.put(new TopicPartition("test", 0), recordList);
        ConsumerRecords<String, String> consumerRecords = new ConsumerRecords<>(recordMap);

        when(kafkaConsumer.poll(any())).thenReturn(consumerRecords).thenReturn(null);
        doReturn(kafkaConsumer).when(consumerFactory).getConsumer();

        nameEventHandler.listenForEvent();
        verify(namesManager, times(1)).save(any());
    }

    @Test
    public void when_NoEventsInStream_Then_SaveIsNotCalled() {
        ConsumerRecord<String, String> record =
                new ConsumerRecord<String, String>("filler", 0, 0, "testKey", "testValue");
        List<ConsumerRecord<String, String>> recordList = new ArrayList<>();
        recordList.add(record);
        Map<TopicPartition, List<ConsumerRecord<String, String>>> recordMap = new HashMap<>();
        recordMap.put(new TopicPartition("test", 0), recordList);
        ConsumerRecords<String, String> consumerRecords = new ConsumerRecords<>(recordMap);

        when(kafkaConsumer.poll(any())).thenReturn(null);
        doReturn(kafkaConsumer).when(consumerFactory).getConsumer();

        nameEventHandler.listenForEvent();
        verify(namesManager, times(0)).save(any());
    }
}