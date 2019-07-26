/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.web;

import java.util.List;

/**
 *
 * @author pedro
 */
public class JsonObj {
    private String index;
    private String object;
 
    public JsonObj(){}
    
    public JsonObj(String index, String object){
        this.index = index;
        this.object = object;
    }
    
    public String getIndex(){
        return index;
    }
    
    public void setIndex(String index){
        this.index = index;
    }
    
    public String getObject(){
        return object;
    }
    
    public void setObject(String object){
        this.object = object;
    }
    
}
