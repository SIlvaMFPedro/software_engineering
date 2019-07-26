/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpc.chessController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jpc.chessconsumer.MessageStorage;
import jpc.chessSim.Fen;


/**
 *
 * @author pedro
 */
@RestController
@RequestMapping(value="/jsa/kafka")
public class WebRestController {
    @Autowired
    Fen producer;
    
    @Autowired
    MessageStorage storage;
    
    String[] args = new String[0];
    
    @GetMapping(value="/producer")
    public String producer(@RequestParam("data")String data) throws InterruptedException{
            Fen.main(args);
            return "Done";
    }
    
    @GetMapping(value="/consumer")
    public String getAllReceivedMessages(){
            String messages = storage.toString();
            storage.clear();
            return messages;
    }
    
    
    
    
}
