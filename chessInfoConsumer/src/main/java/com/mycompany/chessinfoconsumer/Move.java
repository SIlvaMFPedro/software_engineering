/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.chessinfoconsumer;


/**
 *
 * @author franciscoteixeira
 */

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Move implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String move_made;

    public String getMove_made() {
        return move_made;
    }

    public void setMove_made(String move_made) {
        this.move_made = move_made;
    }
    
    @Override
    public String toString() {
        return "Move [move made=" + move_made + "]";
    }
    
}
