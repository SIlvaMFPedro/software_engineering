/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.consumer;

import chess.sim.Fen;
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

/**
 *
 * @author pedro
 */
public class ChessConsumerWhite {
    static private ArrayList<String> white_moves;
    static boolean flag = false;
 
    private static void consume(){
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
        consumer.subscribe(Arrays.asList("chess-white-moves"));
        //infinite poll loop
        int cnt = 0;
        ArrayList<String> str = new ArrayList<>();
        String play = new String();
        System.out.println(cnt);
        while (cnt < 12) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            System.out.println("Waiting for white move...");
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("offset = %d, key = %s, value = %s\n", record.offset(), record.key(), record.value());
                play = record.key() + " " + record.value();
                //str.add(record.key());
                str.add(play);
                setWhiteMoves(str);
                cnt++;
                if(cnt == 12){
                    System.out.print("DONE CONSUMING....\n");
                    break;
                }
            }
        }
        cnt = 0;
    }
    
    private static void setWhiteMoves(ArrayList<String> moves){
        System.out.print("Setting White moves!\n");
        flag = true;
        white_moves = new ArrayList<String>(moves);
        for(int i = 0; i < white_moves.size(); i++){
            System.out.println(white_moves.get(i));
        }
    }
    
    public static void produceWhiteMoves(ArrayList<String> str){
        System.out.println("PRODUCING WHITE MOVES(FREQUENCY)");
        //Producer code starts here
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:29092"); //before 9092
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("request.required.acks", "1");
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        
        Producer<String, String> producer = new KafkaProducer<>(props);
        
        for(String item : str){
            int frequency = Collections.frequency(str, item);
            producer.send(new ProducerRecord<String, String>("chess-moves-processed", item, String.valueOf(frequency)));
        }
        producer.close();
    }
    
    public static ArrayList<String> retrieveMoves(){
        System.out.println(flag);
        System.out.print("Retrieving white moves!\n");
        System.out.print("Calling producer!\n");
        System.out.print("Consuming...\n");
        consume();
        System.out.println("White Moves...\n");
        for(int i = 0; i < white_moves.size(); i++){
            System.out.println(white_moves.get(i));
        }
        return white_moves;
    }
    
}
