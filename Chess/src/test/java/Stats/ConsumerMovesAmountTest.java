/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Stats;

import jpc.chess.ChessConsumer;
import org.junit.Test;

/**
 *
 * @author franciscoteixeira
 */
public class ConsumerMovesAmountTest {
    
    @Test
    public void ConsumerMovesAmountTest() throws Exception {
        ChessConsumer tester = new ChessConsumer(); // testing ChessConsumer
        assert(tester.retrieveMoves().size()==10);
    }
    
}
