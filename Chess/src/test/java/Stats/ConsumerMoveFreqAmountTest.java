/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Stats;

import java.util.ArrayList;
import jpc.chess.ChessConsumer;
import jpc.chess.MovesFrequency;
import org.junit.Test;

/**
 *
 * @author franciscoteixeira
 */
public class ConsumerMoveFreqAmountTest {
    
    @Test
    public void ConsumerMoveFreqAmountTest() throws Exception {
        MovesFrequency tester = new MovesFrequency(); // testing MovesFrequency
        assert(tester.retrieveProcessedMoves().size()==10);
    }
    
}
