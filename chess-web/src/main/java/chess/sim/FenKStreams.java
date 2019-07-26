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
public class FenKStreams {
    static Random rand = new Random();
    static boolean switch_topic = false;
    
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
        hash_props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        hash_props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        hash_props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        
        ProducerFactory<String, String> producer_factory = new DefaultKafkaProducerFactory<>(hash_props);
        kafkaTemplate = new KafkaTemplate<>(producer_factory);
        
        
        Producer<String, String> producer = new KafkaProducer<>(props);
        String figure = "";
        int cnt = 0;
        while(cnt<20){
            figure = generateRandomChessMove(cnt);
            cnt++;
            System.out.println(figure);
            Thread.sleep(9000);
            //String[] origin_destiny = figure.split(" ");
            //String origin = origin_destiny[0];
            //String destiny = origin_destiny[1];
            //ProducerRecord<String, String> producer_record = new ProducerRecord<String, String>("chess-moves", origin, destiny);
            ProducerRecord<String, String> producer_record;
            String player = new String();
            //Player White Move
            if(switch_topic){
                player = "white";
                producer_record = new ProducerRecord<String, String>("chess-white-moves", player, figure);
            }
            //Player Black Move
            else{
                producer_record = new ProducerRecord<String, String>("chess-black-moves", player, figure);
            }
            System.out.print("QUESTOES LOGISTICAS: " + producer_record.toString());
            producer.send(producer_record);
            send(producer_record.toString());
            //Thread.sleep(9000);
            //TimeUnit.SECONDS.sleep(10);
        }
        producer.close();
        
        
    }
    
    public static String generateRandomChessMove(int x){
        String[] moves = {"c2 c4", "g1 e4", "e4 h6", "e2 h4", "e1 e2", "f1 e1", "b2 b4"};
        String[] pawns = {"Pawn1_black", "Pawn2_black", "Pawn3_black", "Pawn4_black", "Pawn5_black", "Pawn6_black", "Pawn7_black", "Pawn8_black"};
        String[] figures = {"Rook1_black", "Rook2_black", "Knight1_black", "Knight2_black", "Bishop1_black", "Bishop2_black", "King_black", "Queen_black"};
        
        String[] white_moves = {"c2 c4", "g1 f3", "a2 a4", "b2 b4", "g2 g3", "e2 e4", "f3 h4", "a1 a3", "a3 f3", "h1 g1"};
        String[] black_moves = {"h7 h6", "b7 b6", "g7 g6", "f7 f6", "d7 d6", "c8 b7", "a7 a6", "e7 e6", "d8 e7", "c7 c5"};
        
        String figure = new String();

//        Random rand = new Random();
//        int figure_index = rand.nextInt(8);
//        int list = rand.nextInt(2) + 1;
//        String figure = new String();
//        
//        if(list==1){
//            //System.out.println(figure_index);
//            figure = pawns[figure_index];
//        }
//        else{
//            figure = figures[figure_index];
//        }
        
        if(x%2 == 0){
            switch_topic = true;
            switch(x){
                case 0: figure = white_moves[0];
                        break;
                case 2: figure = white_moves[1];
                        break;
                case 4: figure = white_moves[2];
                        break;
                case 6: figure = white_moves[3];
                        break;
                case 8: figure = white_moves[4];
                        break;
                case 10: figure = white_moves[5];
                        break;
                case 12: figure = white_moves[6];
                        break;
                case 14: figure = white_moves[7];
                        break;
                case 16: figure = white_moves[8];
                        break;
                case 18: figure = white_moves[9];
                        break;
                default: break;
            }
        }
        else{
            switch_topic = false;
            switch(x){
                case 1: figure = black_moves[0];
                        break;
                case 3: figure = black_moves[1];
                        break;
                case 5: figure = black_moves[2];
                        break;
                case 7: figure = black_moves[3];
                        break;
                case 9: figure = black_moves[4];
                        break;
                case 11: figure = black_moves[5];
                         break;
                case 13: figure = black_moves[6];
                         break;
                case 15: figure = black_moves[7];
                         break;
                case 17: figure = black_moves[8];
                         break;
                case 19: figure = black_moves[9];
                         break;
                default: break;
            }
        }
        
        return figure;
        
        
        //return moves[x];
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
