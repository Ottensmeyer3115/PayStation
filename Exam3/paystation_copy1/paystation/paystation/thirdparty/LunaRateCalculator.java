package paystation.thirdparty;

import java.util.*;

/** Lunatown rate calculator based on lunar phases.
 
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
public final class LunaRateCalculator {
  /** Calculate the amount of minutes parking time the given
      'amount' is worth.
      @param dollaramount the entered value in dollars
  */
  public int calculateRateForAmount( double dollaramount ) {
    Calendar cal = GregorianCalendar.getInstance();
    int 
      y = cal.get( Calendar.YEAR ),
      m = cal.get( Calendar.MONTH ) + 1,  // tricky: january = 0
      d = cal.get( Calendar.DAY_OF_MONTH );
    //System.out.println( "" + y + ":" + m + ":" + d );
    int jd = greg2jd(y,m,d);
    double moonage = moonAge(jd);
    //System.out.println( "jd=" + jd + " moon="+ moonage );
    
    // The base rate is (dollar amount * 40) = alphatown's
    // at full moon (moon age = 15); while double at new moon.
    // We handle this by defining a 'factor' that vary between
    // 1.0 and 2.0 depending on the moon age. 
    
    // Find the moonage factor 
    double factor = Math.abs(moonage - 15.0) / 15.0 + 1.0;
    // and divide it up in alphatown's rate structure.
    double time = dollaramount * 40 / factor;
    
    return (int) time;
  }

  /** Approximate moonage for a given julian day. The time
   * between two new moon is about 29.53 days long. A moonage of
   * 0 or 29 indicates new moon, about 7 is first quarter, etc.
   * The accuracy of this method is low, about +/- one day.
   * @param jd julian day
   * @return Approximate moon age where 0/29 is new moon, 15 is full
   * moon.
   */
  private double moonAge(int jd) {
    double synodicMonth = jd / 29.530589;
    double moonAge = (synodicMonth - Math.floor(synodicMonth) - 0.33)
      *29.530589;
    if (moonAge < 0.0) moonAge += 29.5306;
    return moonAge;
  }

  /** Convert from gregorian date to julian day.
   * Time of day is not considered.
   * Source: <em>Sky and Telescope</em> Maj 1984. 
   * @return julian day for the given year, month and day. */
  private int greg2jd( int Year, int Month, int Day )
  {
    double D, F, J, J1, M, S, A;
    
    D  = Day;
    M  = Month;
    J = - Math.floor(7.*(Math.floor((M+9.)/12.)+Year)/4.);
    if ( (M-9.) > 0.0)  
      S = 1.0;
    else 
      if ( (M-9.) < 0.0 )
	S = -1.0;
      else 
	S = 0.0;
    
    A  = Math.abs(M-9.0);
    J1 = Math.floor(Year+S*Math.floor(A/7.0));
    J1 = - Math.floor((Math.floor(J1/100.0)+1.0)*3.0/4.0);
    J  = J + Math.floor(275.0*M/9.0)+D+J1;
    J  = J + 1721027.0+2.0+367.0*Year;
    return (int) J; 
  }
}

