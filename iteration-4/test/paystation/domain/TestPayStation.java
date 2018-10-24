package paystation.domain;

import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;

/** Test cases for the Pay Station system.
 
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
public class TestPayStation {
  PayStation ps;
  PayStationFactory factory;
  RateStrategy rs;
  /** Fixture for pay station testing. */
  @Before
  public void setUp() {
    ps = new PayStationImpl( new TestTownFactory() );
  }

  /** Test acceptance of all legal coins */
  @Test
  public void shouldAcceptLegalCoins() throws IllegalCoinException {
	boolean isDanish = ps.isDanishCurrency(false);
    ps.addPayment( 5 );
    ps.addPayment( 10 );
    ps.addPayment( 25 );
    assertEquals( "Should accept 5, 10, and 25 cents", 
                  5+10+25, ps.readDisplay() );
  }
  @Test
  public void shouldAcceptLegalDanishCoins() throws IllegalCoinException {
	  boolean isDanish = ps.isDanishCurrency(true);
	    isDanish = ps.isDanishCurrency(true);
	    ps.addPayment( 1 );
	    ps.addPayment( 2 );
	    ps.addPayment( 5 );
	    ps.addPayment( 10 );
	    ps.addPayment( 20 );
	    assertEquals( "should accept 1, 2, 5, 10, and 20 krones",
	    				1+2+5+10+20, ps.readDisplay() ); 
  }

  /** 
   * Verify that illegal coin values are rejected.
  */
  @Test(expected=IllegalCoinException.class)
  public void shouldRejectIllegalCoin() throws IllegalCoinException {
	ps.isDanishCurrency(false);
    ps.addPayment(17);
  }

  /**
   * Buy should return a valid receipt of the 
   * proper amount of parking time
  */
  @Test 
  public void shouldReturnCorrectReceiptWhenBuy() 
    throws IllegalCoinException {
    ps.addPayment(5);
    ps.addPayment(10);
    ps.addPayment(25);
    Receipt receipt;
    receipt = ps.buy();
    assertNotNull( "Receipt reference cannot be null",
                   receipt );
    assertEquals( "Receipt value must be correct.",
                  5+10+25, receipt.value() );
  }
 
  /**
   * Receipts must be able to store parking time values
   */
  @Test 
  public void shouldStoreTimeInReceipt() {
    Receipt receipt = new StandardReceipt(30);
    assertEquals( "Receipt can store 30 minute value",
                  30, receipt.value() );
  }

  /**
   * Buy for 100 cents and verify the receipt
  */
  @Test 
  public void shouldReturnReceiptWhenBuy100c() 
    throws IllegalCoinException {
    ps.addPayment(10);
    ps.addPayment(10);
    ps.addPayment(10);
    ps.addPayment(10);
    ps.addPayment(10);
    ps.addPayment(25);
    ps.addPayment(25);

    Receipt receipt;
    receipt = ps.buy();
    assertEquals((5*10+2*25) , receipt.value() );
  }

  /**
   * Verify that the pay station is cleared after a buy scenario
  */
  @Test 
  public void shouldClearAfterBuy() 
    throws IllegalCoinException {
    ps.addPayment(25);
    ps.buy(); // I do not care about the result
    // verify that the display reads 0
    assertEquals( "Display should have been cleared",
                  0 , ps.readDisplay() );
    // verify that a following buy scenario behaves properly
    ps.addPayment(10); ps.addPayment(25);
    assertEquals( "Next add payment should display correct time",
                  10+25, ps.readDisplay() );
    Receipt r = ps.buy();
    assertEquals( "Next buy should return valid receipt",
                  (10+25), r.value() );
    assertEquals( "Again, display should be cleared",
                  0 , ps.readDisplay() );
  }
  /**
   * Verify that cancel clears the pay station
   */
  @Test 
  public void shouldClearAfterCancel() 
    throws IllegalCoinException {
    ps.addPayment(10);
    ps.cancel();
    assertEquals( "Cancel should clear display",
                  0 , ps.readDisplay() );
    ps.addPayment(25);
    assertEquals( "Insert after cancel should work",
                  25, ps.readDisplay() );
  }

  /** Test that the receipt prints proper information */
  @Test public void shouldPrintReceiptCorrectly() {
    Receipt receipt = new StandardReceipt(30);
    // Prepare a PrintStream instance that lets me inspect the 
    // data written to it.
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    // let the 30 minute print itself
    receipt.print(ps);

    // get the string printed to the stream
    String output = baos.toString();
    // split the string into individual lines
    String[] lines = output.split("\n");
    // test to see that the receipt consist of five lines
    assertEquals( 5, lines.length );
    // test parts of the contents
    assertEquals( "---", lines[0].substring(0,3) );
    assertEquals( "---", lines[4].substring(0,3) );
    assertEquals( "P A R K I N G", lines[1].substring(9,22) );
    // test the receipt's value 
    assertEquals( "030", lines[2].substring(22,25) );
    // test that the format of the "parking starts at" time
    // is plausible
    String parkedAtString = lines[3].substring(28,33);
    assertEquals( ':', parkedAtString.charAt(2) );
    // if the substring below is not an integer a
    // NumberFormatException is thrown which will 
    // make JUnit fail this test
    Integer.parseInt( parkedAtString.substring(0,2) );
    Integer.parseInt( parkedAtString.substring(3,5) );
   }

  /** Test that the bar code receipt has a bar code line */
  @Test public void shouldPrintBarCodeReceiptCorrectly() {
    Receipt receipt = new StandardReceipt(30, true);
    // as the receipt's bar code line is mere a faked line of
    // | and spaces, all I care about is that it is indeed
    // one line longer than the standard receipt.
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    receipt.print(ps);
    String output = baos.toString();
    String[] lines = output.split("\n");
    assertEquals( "Bar code receipts must be 6 lines long",
                  6, lines.length );
  }
  
  /** Test that the isDanishCurrency resets the strategy
   *  to use the Danish Strategy and returns the appropriate
   *  value
   */
  @Test
  public void shouldResetStrategyToDanish(){
	  rs = new DanishRateStrategy();
	  assertEquals( "", rs, ps.isDanishCurrency(true));
  }
  
  @Test
  public void shouldReturnTrueIfDanishAndFalseIfNot(){
	  rs = new DanishRateStrategy();
	  assertEquals("", true, ps.isDanishCurrency(true));
	  assertEquals("", false, ps.isDanishCurrency(false));
  }
}
