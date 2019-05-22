package paystation.view;

import java.awt.Font;

//import javax.swing.JComponent;
import javax.swing.JLabel;

public class LCDDigitDisplay extends JLabel {
	
	public LCDDigitDisplay(){//JLabel label){
		Font font = this.getFont();
		font = font.deriveFont((float) 100.0);
		this.setFont(font);
	}
	
	public void set(String string) {
		setText(string);
	}
	
}
