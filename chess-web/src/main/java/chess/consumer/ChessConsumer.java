/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.consumer;

import java.util.ArrayList;
import java.util.Iterator;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import java.util.Properties;
import java.util.regex.Pattern;



/**
 *
 * @author pedro
 */
public class ChessConsumer {
    static ArrayList<String> white_moves = new ArrayList<>();
    static ArrayList<String> black_moves = new ArrayList<>();
    
    private static String last_white_move;
    private static String last_black_move;
    
    public static void main(final String[] args) throws Exception{
        //Start Kafka Streams consumer
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "ChessConsumer"); //id of config.
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); //server
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        
        KStreamBuilder builder = new KStreamBuilder();
        KStream<String, String> chess_moves = builder.stream("chess-moves"); //topic name
        
        chess_moves.foreach((String key, String value) -> {
            switch (key) {
                case "white":
                    white_moves.add(value);
                    last_white_move = getLastMoveWhite();
                    System.out.print("Last move from Player \"White\": " + last_white_move + "\n");
                    break;
                    
                case "black":
                    black_moves.add(value);
                    last_black_move = getLastMoveBlack();
                    System.out.print("Last move from Player \"Black\": " + last_black_move + "\n");
                    break;
                          
                default:
                    System.out.print("Invalid move!\n");
                    break;
            }
        });
        
        KafkaStreams streams = new KafkaStreams(builder, props);
        streams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
    
    public static ArrayList<String> retrieveWhiteMoves(){
        return white_moves;
    }
    
    public static ArrayList<String> retrieveBlackMoves(){
        return black_moves;
    }
    
    public static String getLastMoveWhite(){
        return white_moves.get( white_moves.size() - 1);
    }
    
    public static String getLastMoveBlack(){
        return black_moves.get( black_moves.size() - 1);
    }
}
