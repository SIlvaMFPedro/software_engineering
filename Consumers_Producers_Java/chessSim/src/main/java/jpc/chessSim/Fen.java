/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpc.chessSim;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
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


@Service
public class Fen {
    static Random rand = new Random();
    
    private static final Logger log = LoggerFactory.getLogger(Fen.class);
    
    @Value("${jsa.kafka.topic}")
    static String kafkaTopic = "chess-moves";
    
    @Autowired
    private static KafkaTemplate<String, String> kafkaTemplate;
    
    
    public static void main(String[] args) throws InterruptedException {
        
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
        for (int i = 0; i < 10000; i++){
            figure = generateRandomChessMove();
            System.out.println(figure);
            Thread.sleep(2000);
            ProducerRecord<String, String> producer_record = new ProducerRecord<String, String>("chess-moves", figure, figure);
            System.out.print("QUESTOES LOGISTICAS: " + producer_record.toString());
            producer.send(producer_record);
            send(producer_record.toString());
            
        }

        producer.close();
        
    }
    
    public static String generateRandomChessMove(){
        
        String[] pawns = {"Pawn1_black", "Pawn2_black", "Pawn3_black", "Pawn4_black", "Pawn5_black", "Pawn6_black", "Pawn7_black", "Pawn8_black"};
        String[] figures = {"Rook1_black", "Rook2_black", "Knight1_black", "Knight2_black", "Bishop1_black", "Bishop2_black", "King_black", "Queen_black"};

        Random rand = new Random();
        int figure_index = rand.nextInt(8);
        int list = rand.nextInt(2) + 1;
        String figure;
        
        if(list==1){
            //System.out.println(figure_index);
            figure = pawns[figure_index];
        }
        else{
            figure = figures[figure_index];
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