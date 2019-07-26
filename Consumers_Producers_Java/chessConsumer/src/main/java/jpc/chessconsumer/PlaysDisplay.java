/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpc.chessconsumer;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
 *
 * @author jpc
 */
public class PlaysDisplay extends HttpServlet{
        
        
        
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
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
        
		resp.setStatus(HttpServletResponse.SC_OK);
		PrintWriter out = resp.getWriter();
                out.print("fodase");
        }
}
                
