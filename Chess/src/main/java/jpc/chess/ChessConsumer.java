package jpc.chess;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Predicate;
/**
 *
 * @author jpc
 */
public class ChessConsumer {
    public static ArrayList<String> retrieveMoves(){
        //CONSUMER CODE STARTS HERE
        //consumer properties
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:29092");
        props.put("group.id", "test-group");
        //using auto commit
        props.put("enable.auto.commit", "true");
        //string inputs and outputs
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        //kafka consumer object
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        //subscribe to topic
        consumer.subscribe(Arrays.asList("chess-moves"));
        //infinite poll loop
        int cnt = 0;
        ArrayList<String> str = new ArrayList<>();
        System.out.println(cnt);
        while (cnt<10) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            System.out.println("got here y'all");
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("offset = %d, key = %s, value = %s\n", record.offset(), record.key(), record.value());
                str.add(record.key());
                cnt++;
                if(cnt == 10)
                    break;
            }
        }
        cnt = 0;
        
        //PRODUCER CODE STARTS HERE
        Properties props2 = new Properties();
        props2.put("bootstrap.servers", "localhost:29092"); //before 9092
        props2.put("acks", "all");
        props2.put("retries", 0);
        props2.put("batch.size", 16384);
        props2.put("linger.ms", 1);
        props2.put("request.required.acks", "1");
        props2.put("buffer.memory", 33554432);
        props2.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props2.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(props2);
        
        for(String item : str) {
            int frequency = Collections.frequency(str, item);
            producer.send(new ProducerRecord<String, String>("chess-moves-processed", item, String.valueOf(frequency)));
        }

        producer.close();
        
        
        /*Properties streamsConfiguration = new Properties();
        streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, "chess-moves-processed");
        streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        streamsConfiguration.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        streamsConfiguration.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        KStreamBuilder builder = new KStreamBuilder();
        
        KStream<String, String> moves = builder.stream("chess-moves");
        moves.*/
        
        consumer.close();     
        return str;
    }
}