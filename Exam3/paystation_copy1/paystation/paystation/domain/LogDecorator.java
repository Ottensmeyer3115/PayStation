package paystation.domain;
import java.util.Date;
/** A  PayStation decorator that logs coin entries.
 
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
public class LogDecorator implements PayStation {
  private PayStation paystation;
  public LogDecorator( PayStation ps ) {
    paystation = ps;
  }
  public void addPayment( int coinValue ) throws IllegalCoinException {
    System.out.println( ""+coinValue+" cents: "+new Date() );
    paystation.addPayment( coinValue );
  }
  public int readDisplay() { return paystation.readDisplay(); }
  public Receipt buy() { return paystation.buy(); }
  public void cancel() { paystation.cancel(); }
@Override
public int getTotalEarnings() {
	// TODO Auto-generated method stub
	return paystation.getTotalEarnings();
}
}

