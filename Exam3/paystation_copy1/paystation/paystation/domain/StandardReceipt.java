package paystation.domain;

import java.io.*;
import java.util.*;

/** Standard implementation of Receipt.
 
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

public class StandardReceipt implements Receipt {
  private int value;
  private boolean withBarCode;
  /** Make a receipt.
   * @param value the minute value of this receipt
   * @param withBarCode if true then a (fake) bar code is added
   */
  public StandardReceipt(int value, boolean withBarCode) { 
    this.value = value; 
    this.withBarCode = withBarCode;
  }
  public StandardReceipt(int value) {
    this(value, false);
  }
  public int value() { return value;}
  public void print(PrintStream stream) {
    String valuestring = ""+value;
    if ( valuestring.length() == 1 ) { valuestring = "00"+valuestring; }
    if ( valuestring.length() == 2 ) { valuestring = "0"+valuestring; }
    Calendar now = GregorianCalendar.getInstance();
    String hour = ""+now.get(Calendar.HOUR_OF_DAY);
    if ( hour.length() == 1 ) { hour = "0"+hour; }
    String min = ""+now.get(Calendar.MINUTE);
    if ( min.length() == 1 ) { min = "0"+min; }
    String nowstring = hour+":"+min;

    stream.println("-------------------------------------------------");
    stream.println("-------  P A R K I N G   R E C E I P T    -------");
    stream.println("                Value "+valuestring 
                   +" minutes.               ");
    stream.println("              Car parked at "+nowstring);
    if ( withBarCode ) {
      stream.println("||  ||||| | || ||| || ||  ||| | || |||| | || ||||");
    }
    stream.println("-------------------------------------------------");
  }   
}
