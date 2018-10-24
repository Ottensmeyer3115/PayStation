package paystation.domain;

/** Implementation of the pay station.

   Responsibilities:
			
   1) Accept payment;
   2) Calculate parking time based on payment;
   3) Know earning, parking time bought;
   4) Issue receipts;
   5) Handle buy and cancel events.
 
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

public class PayStationImpl implements PayStation {
  private int insertedSoFar;
  private int timeBought;
  
  //Check if we are using danish currency
  private boolean isDanishCurrency;

  /** the strategy for rate calculations */
  private RateStrategy rateStrategy;
  /** the factory that defines strategies */
  private PayStationFactory factory;
  
  /** Construct a pay station.
      @param factory the factory to produce strategies and receipts
  */
  public PayStationImpl( PayStationFactory factory ) {
    this.factory = factory;
    this.rateStrategy = factory.createRateStrategy();
    reset();
  }

  public void addPayment( int coinValue ) throws IllegalCoinException {
	  if(isDanishCurrency == false){
	    switch ( coinValue ) {
	    case 5: break;
	    case 10: break;  
	    case 25: break;  
	    default:
	      if(isDanishCurrency == true){
	    	  break;
	      } else{  
	    	  throw new IllegalCoinException("Invalid coin: "+coinValue);
	      }
	    }
	  }
	  else{
		  switch ( coinValue ){
		  case 1: break;
		  case 2: break;
		  case 5: break;
		  case 10: break;
		  case 20: break;
		  default:
			  throw new IllegalCoinException("Invalid coin: " + coinValue);
		  }
	  }
    insertedSoFar += coinValue;
    timeBought = rateStrategy.calculateTime(insertedSoFar);
  }
  public int readDisplay() {
    return timeBought;
  }
  public Receipt buy() {
    Receipt r = factory.createReceipt(timeBought);
    reset();
    return r;
  }
  public void cancel() {
    reset();
  }
  private void reset() {
    timeBought = insertedSoFar = 0;
  }
  
  public boolean isDanishCurrency(boolean useDanishCurrency){
	  if(useDanishCurrency == true){
		  rateStrategy = new DanishRateStrategy();
	  }
	  return useDanishCurrency;
  }
}

