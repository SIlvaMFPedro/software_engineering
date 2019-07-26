/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpc.chessconsumer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

/**
 *
 * @author pedro
 */
@Component
public class MessageStorage {
    private List<String> storage;
    
    public MessageStorage(){
        this.storage = new ArrayList<String>();
    }
    
    public void put(String message){
        storage.add(message);
    }
    public String toString(){
        StringBuffer info = new StringBuffer();
        storage.forEach(msg->info.append(msg).append("</br>"));
        return info.toString();
    }
    public void clear(){
        storage.clear();
    }
    
    
}
