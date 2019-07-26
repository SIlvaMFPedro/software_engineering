package jpc.chessconsumer;


import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.kafka.annotation.KafkaListener;

/**
 *
 * @author jpc
 */
@Service
public class chessConsumer {
    
    private static final Logger log = (Logger) LoggerFactory.getLogger(chessConsumer.class);
    
    @Autowired
    private static MessageStorage storage;
    
    public static void main(String[] args) throws UnsupportedEncodingException {
            
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
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                String message = new String();
                //print output;
                System.out.printf("offset = %d, key = %s, value = %s\n", record.offset(), record.key(), record.value());
                message = "offset = " + record.offset() + ", key = " + record.key() + ", value = " + record.value();
                System.out.print("\nPLAY MESSAGE: " + message + "\n");
                processMessage(message);
            }
        }
    }
    @KafkaListener(topics="${jsa.kafka.topic}")
    public static void processMessage(String message) {
            log.info("received content = '{}'", message);
            System.out.print("\n");
            System.out.print("PUTTING MESSAGE ( ͡° ͜ʖ ͡°) I REPEAT PUTTING MESSAGE ( ͡° ͜ʖ ͡°)\n");
            System.out.print("\n");
            storage.put(message);
    }
    

}