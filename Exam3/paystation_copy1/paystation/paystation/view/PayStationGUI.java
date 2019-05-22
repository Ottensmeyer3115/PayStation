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
public class PayStationGUI extends JFrame {

  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch ( Exception e ) {
      // ignore silently, as we then just do not get the required
      // look and feel for all windows
    }
    new PayStationGUI();
  }

  SpotManagerInterface spotManager;
  
  /** The "digital display" where readings are shown */
  LCDDigitDisplay display;
  /** The domain pay station that the gui interacts with */
  PayStation payStation;
  /** The paystation instance above may be decorated; if
      it is then the instance varible decoratee will
      contain the non-decorated instance */
  PayStation decoratee;
  
  PayStationStatusGUI payStationStatus;

  /** Create the GUI */
  public PayStationGUI() {
    super("PayStation GUI" );
    
    spotManager = new SpotManagerImpl(200);
    
    payStation = new PayStationImpl( new BetaTownFactory(), spotManager ); 
    decoratee = payStation;

    // all the gui setup
    JFrame.setDefaultLookAndFeelDecorated(true);
    setLocation( 100, 20 );
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

     
    Container cpane = getContentPane(); 

    cpane.setLayout( new BorderLayout() );

    cpane.add( createCoinInputPanel(), BorderLayout.EAST );
    cpane.add( createButtonPanel(), BorderLayout.SOUTH );

    cpane.add( createDisplayPanel(), BorderLayout.CENTER );

    display.set("   0");
    
    setJMenuBar( createAllMenus() );
    
    payStationStatus = new PayStationStatusGUI(payStation, spotManager);
    
    pack();
    setVisible(true);
  }

  /** Update the digital display with whatever the
      pay station domain shows */
  private void updateDisplay() {
    String prefixedZeros = 
      String.format("%4d", payStation.readDisplay() );
      display.set( prefixedZeros );
      payStationStatus.updateDisplay();
  }
  
  /** Create the coin input panel */
  private JComponent createCoinInputPanel() {
    Box p = new Box( BoxLayout.Y_AXIS );
    p.add( defineButton( " 5 c", "5" ));
    p.add( defineButton( "10 c", "10" ));
    p.add( defineButton( "25 c", "25" ));
    return p;
  }
  
  /** The button action listener that reacts on clicking the
      coin buttons */
  private ActionListener buttonActionListener = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();
        int coin = Integer.parseInt(s);
        try {
          payStation.addPayment( coin );
        } catch (IllegalCoinException exc) {
          // illegal coins just do nothing.
        }
        updateDisplay();
      }
    };
  
  /** Define a button's text and action command */
  private JButton defineButton(String text, String actioncommand) {
    JButton b;
    b = new JButton( text );
    b.setAlignmentX(Component.CENTER_ALIGNMENT);
    b.setActionCommand( actioncommand );
    b.addActionListener( buttonActionListener );
    return b;
  }

  /** Create the panel of buttons */
  private JComponent createButtonPanel() {
    Box p = new Box( BoxLayout.X_AXIS );
    JButton b;
    b = new JButton("Cancel");
    b.setAlignmentX(Component.CENTER_ALIGNMENT);
    p.add( Box.createHorizontalGlue() );
    p.add( b );
    b.addActionListener( new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          payStation.cancel();
          updateDisplay();
        } } );

    b = new JButton("Buy");
    b.setAlignmentX(Component.CENTER_ALIGNMENT);
    p.add( Box.createHorizontalGlue() );
    p.add( b );
    p.add( Box.createHorizontalGlue() );
    b.addActionListener( new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          Receipt r = payStation.buy();
          updateDisplay();
          // print the receipt
          showReceiptInWindow(r);
        } } );

    return p;
  }

  /** Create the panel for the display */
  private JComponent createDisplayPanel() {
    display = new LCDDigitDisplay();
    return display;
  }

  private void showReceiptInWindow(Receipt receipt) {
    // make the window in which to display receipt
    JFrame frame = new JFrame("Receipt");
    // print the receipt's contents in a string array
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    receipt.print(ps);
    String[] lines = baos.toString().split("\n");
    // copy the contents into a text area
    JTextArea text = new JTextArea(lines.length,20);
    text.setEditable(false);
    text.setFont( new Font("DialogInput", Font.PLAIN, 14) );

    for( int i = 0; i < lines.length; i++ ) {
      text.append(lines[i]+"\n");
    }
    frame.getContentPane().add(text);
    frame.pack();
    frame.setVisible(true);
  }

  /** Create all menus used by the GUI */
  private JMenuBar createAllMenus() {
    JMenuBar menuBar;
    JMenu menu, submenu;
    JMenuItem menuItem;
    
    menuBar = new JMenuBar();
    
    menu = new JMenu("Variant Selection");
    menu.getAccessibleContext().
      setAccessibleDescription("Select pay station variants here.");
    menuBar.add(menu);
    
    //a group of JMenuItems
    menuItem = makeTownMenuItem("Alphatown", new AlphaTownFactory() );
    menu.add(menuItem);
    menuItem = makeTownMenuItem("Betatown", new BetaTownFactory() );
    menu.add(menuItem);
    menuItem = makeTownMenuItem("Gammatown", new GammaTownFactory() );
    menu.add(menuItem);
    menuItem = makeTownMenuItem("Lunatown", new LunaTownFactory() );
    menu.add(menuItem);
    
    addMoreProductVariants(menu);

    menuItem = new JMenuItem("Decorate (log coin)" );
    menuItem.getAccessibleContext().
      setAccessibleDescription("Add/remove coin logging behaviour (output in shell).");
    menu.add(menuItem);
    menuItem.addActionListener( new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          if (payStation == decoratee) {
            // no logging enabled
            payStation = new LogDecorator(payStation);
          } else {
            payStation = decoratee;
          }
        }} );

    return menuBar;
  }
  
  // Template method (inheritance based/unification) for adding
  // more product variants to the menu bar.
  protected void addMoreProductVariants(JMenu menu) {
  }
    
  /** Create menu item for a single town; link it with the factory
      that defines that town's pay station */
  protected JMenuItem makeTownMenuItem( final String name,
                                        final PayStationFactory factory ) {
    JMenuItem menuItem;
    menuItem = new JMenuItem( name );
    menuItem.getAccessibleContext().
      setAccessibleDescription("Reconfig to basic "+name+" behaviour.");
    menuItem.addActionListener( new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          payStation = new PayStationImpl( factory, spotManager );
          decoratee = payStation;
          updateDisplay();
        }
      } );
    return menuItem;
  }
}
