package paystation.domain;
import paystation.thirdparty.*;

/** An adapter for adapting the Lunatown rate calculator
 
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
public class LunaAdapter implements RateStrategy {
  private LunaRateCalculator calculator;
  public LunaAdapter() {
    calculator = new LunaRateCalculator();
  }

  public int calculateTime( int amount ) {
    double dollar = amount / 100.0;
    return calculator.calculateRateForAmount( dollar );
  }
}

