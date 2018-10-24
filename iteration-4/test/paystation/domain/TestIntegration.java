package paystation.domain;

import org.junit.*;
import static org.junit.Assert.*;

import java.io.*;

/** Integration testing of the configurations of the pay station.
 
   This source code is from the book 
     "Flexible, Reliable Software:
       Using Patterns and Agile Development"
     published by CRC Press.
   Author: 
     Henrik B Christensen 
     Department of Computer Science
     Aarhus University
   
   Please visit http://www.baerbak.com/ for further information.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at
 
       http://www.apache.org/licenses/LICENSE-2.0
 
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

*/
public class TestIntegration {
  private PayStation ps;
  
  /**
   * Integration testing for AlphaTown
   */
  @Test 
  public void shouldIntegrateLinearRateCorrectly() 
    throws IllegalCoinException {
    // Configure pay station to be the alphatown pay station
    ps = new PayStationImpl( new AlphaTownFactory() );
    // add $ 2.0: 
    addOneDollar(); addOneDollar();
    
    assertEquals( "Linear Rate: 2$ should give 80 min ",
                  80 , ps.readDisplay() );

    Receipt receipt = ps.buy();
    // test that a standard receipt is issued.
    assertEquals( "AlphaTown should use standard receipts",
                  5, getReceiptLineCount(receipt) );
  }
  /**
   * Integration testing for BetaTown
   */
  @Test 
  public void shouldConfigureBetaTownCorrectly() 
    throws IllegalCoinException {
    // reconfigure ps to be the beta town pay station
    ps = new PayStationImpl( new BetaTownFactory() );
    // add $ 2.0: 1.5 gives 1 hours, next 0.5 gives 15 min
    addOneDollar(); addOneDollar();
    
    assertEquals( "Progressive Rate: 2$ should give 75 min ",
                  75 , ps.readDisplay() );

    Receipt receipt = ps.buy();
    // test that a barcode receipt is issued.
    assertEquals( "BetaTown should use barcode receipts",
                  6, getReceiptLineCount(receipt) );
  }

  /**
   * Integration testing for gamma town. This integration test will
   * ONLY work during normal working weekdays. I have therefore used
   * the explanatory text in the assert to make this premise very
   * clear.
   */
  @Test 
  public void shouldConfigureGammaTownCorrectly() 
    throws IllegalCoinException {
    // reconfigure ps to be the beta town pay station
    ps = new PayStationImpl( new GammaTownFactory() );
    addOneDollar(); addOneDollar();
    
    assertEquals( "WILL FAIL DURING WEEKENDS: Linear rate 2$ = 80 min ",
                  80 , ps.readDisplay() );
    
    Receipt receipt = ps.buy();
    // test that a standard receipt is issued.
    assertEquals( "GammaTown should use standard receipts",
                  5, getReceiptLineCount(receipt) );
  }

  /** return the number of lines in the given receipt */
  private int getReceiptLineCount(Receipt receipt) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream stream = new PrintStream(baos);
    receipt.print(stream);
    String output = baos.toString();
    String[] lines = output.split("\n");
    return lines.length;
  }

  private void addOneDollar() throws IllegalCoinException {
    ps.addPayment(25); ps.addPayment(25); 
    ps.addPayment(25); ps.addPayment(25); 
  }
}