/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.sim;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Service;


/**
 *
 * @author pedro
 */
@Service
public class Fen {
    static Random rand = new Random();
    static boolean switch_topic = false;
    
    static boolean produce_white = false;
    static boolean produce_black = false;
    static boolean keep_producing = true;
    
    private static final Logger log = LoggerFactory.getLogger(Fen.class);
    
    @Value("${jsa.kafka.topic}")
    static String kafkaTopic = "chess-moves";
    
    @Autowired
    private static KafkaTemplate<String, String> kafkaTemplate;
    
    public static void produceWhite(){
        produce_white = true;
    }
    
    public static void produceBlack(){
        produce_black = true;
    }
    
    
    public static void main(String[] args) throws InterruptedException{
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:29092"); //before 9092
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        
        Map<String, Object> hash_props = new HashMap<>();
        hash_props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        hash_props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        hash_props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        
        ProducerFactory<String, String> producer_factory = new DefaultKafkaProducerFactory<>(hash_props);
        kafkaTemplate = new KafkaTemplate<>(producer_factory);
        
        
        Producer<String, String> producer = new KafkaProducer<>(props);
        String figure = "";
        int count1 = 0;
        int count2 = 0;
        while(keep_producing){
            System.out.print("Waiting...\n");
            if(switch_topic){
                System.out.println("Going to sleep for a bit...\n");
                TimeUnit.SECONDS.sleep(300);
                while(count2 < 11){
                    figure = generateBlackChessMove(count2);
                    count2++;
                    System.out.println(figure);
                    Thread.sleep(9000);
                    String[] origin_destiny = figure.split(" ");
                    String origin = origin_destiny[0];
                    String destiny = origin_destiny[1];
                    //ProducerRecord<String, String> producer_record = new ProducerRecord<String, String>("chess-moves", origin, destiny);
                    ProducerRecord<String, String> producer_record;
                    //Player Black Move
                    producer_record = new ProducerRecord<String, String>("chess-black-moves", origin, destiny);
                    System.out.print("PRODUCER BLACK: " + producer_record.toString());
                    producer.send(producer_record);
                    send(producer_record.toString());
                    //Thread.sleep(9000);
                    //TimeUnit.SECONDS.sleep(10)
                }
                switch_topic = false;
                keep_producing = false;
            }
            else{
                while(count1 < 12){
                    figure = generateWhiteChessMove(count1);
                    count1++;
                    System.out.println(figure);
                    Thread.sleep(9000);
                    String[] origin_destiny = figure.split(" ");
                    String origin = origin_destiny[0];
                    String destiny = origin_destiny[1];
                    //ProducerRecord<String, String> producer_record = new ProducerRecord<String, String>("chess-moves", origin, destiny);
                    ProducerRecord<String, String> producer_record;
                    //Player White Move
                    producer_record = new ProducerRecord<String, String>("chess-white-moves", origin, destiny);
                    System.out.print("PRODUCER WHITE: " + producer_record.toString());
                    producer.send(producer_record);
                    send(producer_record.toString());
                    //Thread.sleep(9000);
                    //TimeUnit.SECONDS.sleep(10);
                }
                switch_topic = true;
            }
        }
        producer.close();
    }
    
    public static String generateWhiteChessMove(int x){
        //String[] white_moves = {"c2 c4", "g1 f3", "a2 a4", "b2 b4", "g2 g3", "e2 e4", "f3 h4", "a1 a3", "a3 f3", "h1 g1"};
        //String[] white_moves = {"c2 c4", "d2 d4", "b1 c3", "c1 f4", "e2 e3", "a1 c1", "f1 e2", "g1 f3", "c4 d5", "f4 g3", "b2 c3", "d1 e2", "e1 g1", "e3 e4", "g3 d6", "h2 h3", "g1 h2", "h1 d1", "f3 d2", "h2 g1", "c3 c4", "d2 f1", "f2 f3", "f1 e3", "c1 b1", "g1 h1", "d4 d5", "e3 f5", "d1 c1", "c4 c5", "f5 h4", "h4 f5", "f5 e3", "e2 a6", "e3 c4", "c4 b6", "a6 b6", "b6 c7", "c7 e5", "b1 b7", "e5 e6", "b7 f7", "e6 c8", "c1 c3", "c3 c6", "c8 c7", "c7 d6", "d6 g6", "c6 c7", "c7 c8", "c8 f8"};
        String[] white_moves = {"c2 c4", "h2 h4", "h1 h3", "c4 c5", "f2 f3", "b2 b3", "h3 h1", "d2 d4", "c1 f4", "b1 a3", "a3 b5", "g2 g3"};
        String figure = new String();
        switch(x){
                case 0: figure = white_moves[0];
                        break;
                case 1: figure = white_moves[1];
                        break;
                case 2: figure = white_moves[2];
                        break;
                case 3: figure = white_moves[3];
                        break;
                case 4: figure = white_moves[4];
                        break;
                case 5: figure = white_moves[5];
                        break;
                case 6: figure = white_moves[6];
                        break;
                case 7: figure = white_moves[7];
                        break;
                case 8: figure = white_moves[8];
                        break;
                case 9: figure = white_moves[9];
                        break;
                case 10: figure = white_moves[10];
                         break;
                case 11: figure = white_moves[11];
                         break;
                default: break;
        }
        return figure;   
    }
    
    public static String generateBlackChessMove(int x){
        //String[] black_moves = {"h7 h6", "b7 b6", "g7 g6", "f7 f6", "d7 d6", "c8 b7", "a7 a6", "e7 e6", "d8 e7", "c7 c5"};
        //String[] black_moves = {"a7 a5", "e7 e6", "d7 d5", "g8 f6", "f8 b4", "b7 b6", "e8 g8", "c8 a6", "f6 d5", "d5 c3", "a6 e2", "b4 d6", "h7 h5", "h5 h4", "c7 d6", "b8 d7", "a8 c8", "d8 c7", "h8 h6", "c7 c6", "e6 e5", "h6 g6", "c6 c7", "g6 g3", "c7 d8", "g3 g6", "e6 e5", "d8 f6", "g6 g5", "d6 e5", "g5 h5", "g7 g6", "c8 d8", "g8 g7", "h5 h8", "d7 b6", "c5 c4", "d8 c8", "g7 h7", "h8 f8", "h7 h6", "f8 f7", "c4 c3", "h6 g7", "f7 f6", "f6 f7", "g7 h8", "f7 f8", "f8 f7", "f7 f8"};
        String[] black_moves = {"h7 h6", "b8 a6", "b7 b6", "d7 d5", "e8 d7", "h8 h7", "c7 c6", "e7 e5", "a6 b4", "a7 a5", "d8 h4"};
        String figure = new String();
        switch(x){
                case 0: figure = black_moves[0];
                        break;
                case 1: figure = black_moves[1];
                        break;
                case 2: figure = black_moves[2];
                        break;
                case 3: figure = black_moves[3];
                        break;
                case 4: figure = black_moves[4];
                        break;
                case 5: figure = black_moves[5];
                         break;
                case 6: figure = black_moves[6];
                         break;
                case 7: figure = black_moves[7];
                         break;
                case 8: figure = black_moves[8];
                         break;
                case 9: figure = black_moves[9];
                         break;
                case 10: figure = black_moves[10];
                         break;
                default: break;
        }
        return figure;
    }
    
    @Autowired
    public static void send(String message){
        log.info("sending data = '{}'", message);
        System.out.print("\n");
        System.out.println("SENDING DATA I REPEAT SENDING DATA!");
        System.out.print("\n");
        kafkaTemplate.send(kafkaTopic, message);
    }
}
