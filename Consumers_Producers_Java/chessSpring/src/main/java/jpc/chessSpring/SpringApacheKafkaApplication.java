/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpc.chessSpring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author pedro
 */
@SpringBootApplication
@RestController
public class SpringApacheKafkaApplication {
    
       @RequestMapping("/")
       public String home(){
           return "Hello from the other side";
       }
       public static void main(String[] args) {
		SpringApplication.run(SpringApacheKafkaApplication.class, args);
       }
    
}
