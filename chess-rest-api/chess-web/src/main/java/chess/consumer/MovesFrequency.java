/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.consumer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStreamBuilder;

/**
 *
 * @author pedro
 */
public class MovesFrequency {
    public static ArrayList<String> retrieveProcessedMoves(){
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
        consumer.subscribe(Arrays.asList("chess-moves-processed"));
        //infinite poll loop
        ArrayList<String> str = new ArrayList<>();
        ConsumerRecords<String, String> records = consumer.poll(100);
        System.out.println("yellw");
        int cnt = 0;
        while(cnt<10){
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("offset = %d, key = %s, value = %s\n", record.offset(), record.key(), record.value());
                str.add(record.key()+" was played "+record.value());
                cnt++;
                if(cnt==10)
                    break;
            }
            cnt=10;
        }
        consumer.close();
        return str;
        
        /*Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "movesfrequency");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        
        KStreamBuilder builder = new KStreamBuilder();
        
        KStream<String, String> freqInput = builder.stream("chess-moves");
        
        freqInput.mapValues(value -> value.toLowerCase())
                .flatMapValues(value -> )*/
        
    }
}
