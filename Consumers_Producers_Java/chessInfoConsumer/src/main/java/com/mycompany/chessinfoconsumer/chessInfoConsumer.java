/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.chessinfoconsumer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

/**
 *
 * @author franciscoteixeira
 */
public class chessInfoConsumer {
    public static int kingPawn = 0;
    public static int queenPawn = 0;
    public static int king=0,queen=0,bishop=0,knight=0,rook=0,pawn=0,total=0;
    private static File file = new File("the-file-name.txt");
    
    private static final org.slf4j.Logger log = (org.slf4j.Logger) LoggerFactory.getLogger(chessInfoConsumer.class);
    
    @Autowired
    private static MessageStorage storage;
    
    public static void main(String[] args) {
        boolean b=true;
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
        
        storage = new MessageStorage();

        
        
        //infinite poll loop
        while (b) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                writeToFile(record.value());
                writeStats();
            }
        }
        
        
    }
    
    //@KafkaListener(topics="${jsa.kafka.topic}")
    public static void processMessage(String message) {
            log.info("received content = '{}'", message);
            System.out.print("\n");
            System.out.print("PUTTING MESSAGE ( ͡° ͜ʖ ͡°) I REPEAT PUTTING MESSAGE ( ͡° ͜ʖ ͡°)\n");
            System.out.print("\n");
            storage.put(message);
    }
    
    private static void writeToFile(String s){
        
        //String template = "aaa";
        FileWriter wr = null;
        try {
            wr = new FileWriter(file,true);
        } catch (IOException ex) {
            Logger.getLogger(chessInfoConsumer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (consumeAndStat(s)) {
            try {
                wr.write(s+"\n");
                
            } catch (IOException ex) {
                Logger.getLogger(chessInfoConsumer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }       
        try {
            wr.close();
        } catch (IOException ex) {
            Logger.getLogger(chessInfoConsumer.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    private static void countKQPawn(String s){
        
        String templateK = "aaa";     
        String templateQ = "bbb"; 
        if (s.equals(templateK)) {
            kingPawn++;
        }
        else if (s.equals(templateQ)) {
            queenPawn++;
        }
        
    }
    
    private static void writeStats(){
        FileWriter wr = null;
        try {
            wr = new FileWriter(file,true);
        } catch (IOException ex) {
            Logger.getLogger(chessInfoConsumer.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            wr.write("Stats: Pawns - " + (double)pawn/(double)total + ";  Rooks - " + (double)rook/(double)total + ";  Knights - " + (double)knight/(double)total + ";  Bishops - " + (double)bishop/(double)total + "; Queens - " + (double)queen/(double)total + ";  Kings - " + (double)king/(double)total + "\n");
        } catch (IOException ex) {
                Logger.getLogger(chessInfoConsumer.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            wr.close();
        } catch (IOException ex) {
            Logger.getLogger(chessInfoConsumer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private static boolean consumeAndStat(String move) {
        boolean b = false;
        switch(move) {
            case "Pawn1_black":
                total++;
                pawn++;   
                b=true;
                break;
            case "Pawn2_black":
                total++;
                pawn++;
                b=true;
                break;
            case "Pawn3_black":
                total++;
                pawn++;
                b=true;
                break;
            case "Pawn4_black":
                total++;
                pawn++;
                b=true;
                break;
            case "Pawn5_black":
                total++;
                pawn++;
                b=true;
                break;
            case "Pawn6_black":
                total++;
                pawn++;
                b=true;
                break;
            case "Pawn7_black":
                total++;
                pawn++;
                b=true;
                break;
            case "Pawn8_black":
                total++;
                pawn++;
                b=true;
                break;
            case "Rook1_black":
                total++;
                rook++;
                b=true;
                break;
            case "Rook2_black":
                total++;
                rook++;
                b=true;
                break;
            case "Knight1_black":
                total++;
                knight++;
                b=true;
                break;
            case "Knight2_black":
                total++;
                knight++;
                b=true;
                break;
            case "Bishop1_black":
                total++;
                bishop++;
                b=true;
                break;
            case "Bishop2_black":
                total++;
                bishop++;
                b=true;
                break;
            case "King_black":
                total++;
                king++;
                b=true;
                break;
            case "Queen_black":
                total++;
                queen++;
                b=true;
                break;
        }
        return b;
    }
    
}
