package paystation.view;

import javax.swing.*;

import paystation.domain.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;


//import softcollection.lcd.*;

/** A graphical user interface used as alternative to a real
    hardware to operate the PayStation domain code.
 
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
public class PayStationStatusGUI extends JFrame {
	
	JLabel spots;
	JLabel totalEarnings;
  /** The domain pay station that the gui interacts with */
  PayStation payStation;
  
  SpotManagerInterface spotManager;
  /** The paystation instance above may be decorated; if
      it is then the instance varible decoratee will
      contain the non-decorated instance */
  //PayStation decoratee;

  /** Create the GUI */
  public PayStationStatusGUI(PayStation paystation, SpotManagerInterface spotManager) {
    super("Paystation Status GUI" );
    
    payStation = paystation; 
    this.spotManager = spotManager;
    //decoratee = payStation;

    // all the gui setup
    JFrame.setDefaultLookAndFeelDecorated(true);
    setLocation( 100, 20 );
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

     
    Container cpane = getContentPane(); 

    cpane.setLayout( new BorderLayout() );

    

    cpane.add( createLabels(), BorderLayout.CENTER );
    

    pack();
    setVisible(true);
  }

  /** Update the digital display with whatever the
      pay station domain shows */
  public void updateDisplay() {
	  spots.setText("Number of vacant spots: " + spotManager.getVacantSpots());
	  totalEarnings.setText("Total Earnings: " + payStation.getTotalEarnings());
      //String.format("%3d", payStation.readDisplay() );
  }
  
  /** Create the panel of buttons */
  private JComponent createLabels() {
    Box p = new Box( BoxLayout.Y_AXIS );
    spots = new JLabel("Number of vacant spots: ");
    spots.setAlignmentX(Component.LEFT_ALIGNMENT);
    Font font = spots.getFont();
	font = font.deriveFont((float) 20.0);
	spots.setFont(font);
    p.add( Box.createHorizontalGlue() );
    p.add( spots );

    totalEarnings = new JLabel("Total Earnings: ");
    totalEarnings.setAlignmentX(Component.LEFT_ALIGNMENT);
    font = totalEarnings.getFont();
	font = font.deriveFont((float) 20.0);
	totalEarnings.setFont(font);
    p.add( Box.createHorizontalGlue() );
    p.add( totalEarnings );
    p.add( Box.createHorizontalGlue() );
   
    return p;
  }
}
