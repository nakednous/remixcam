package remixlab.proscene;

import java.awt.event.KeyEvent;

import remixlab.remixcam.core.AbstractScene.Button;
import remixlab.remixcam.devices.*;

public class ClickBinding extends AbstractClickBinding {
	public ClickBinding(Button b) {
		super(b);
	}
	
	public ClickBinding(Integer m, Button b) {
		super (m,b);
	}
	
	public ClickBinding(Button b, Integer c) {
		super(b, c);
	}
	
	public ClickBinding(Integer m, Button b, Integer c) {
		super(m,b,c);
	} 

	/**
	 * Returns a textual description of this click shortcut.
	 *  
	 * @return description
	 */
	@Override
	public String description() {
		String description = new String();
		if(mask != 0)
			description += ClickBinding.getModifiersExText(mask) + " + ";
		switch (button) {
		case LEFT :
			description += "Button1";
			break;
		case MIDDLE :
			description += "Button2";
			break;
		case RIGHT :
			description += "Button3";
			break;		
		}
		if(numberOfClicks==1)
		  description += " + " + numberOfClicks.toString() + " click";
		else
			description += " + " + numberOfClicks.toString() + " clicks";
		return description;
	}

	/**
	 * Function that maps characters to virtual keys defined according to
	 * {@code java.awt.event.KeyEvent}.
	 */
	protected static int getVKey(char key) {
	  if(key == '0') return 48;
	  if(key == '1') return 49;
	  if(key == '2') return 50;
	  if(key == '3') return 51;
	  if(key == '4') return 52;
	  if(key == '5') return 53;
	  if(key == '6') return 54;
	  if(key == '7') return 55;
	  if(key == '8') return 56;
	  if(key == '9') return 57;		
	  if((key == 'a')||(key == 'A')) return 65;
	  if((key == 'b')||(key == 'B')) return 66;
	  if((key == 'c')||(key == 'C')) return 67;
	  if((key == 'd')||(key == 'D')) return 68;
	  if((key == 'e')||(key == 'E')) return 69;
	  if((key == 'f')||(key == 'F')) return 70;
	  if((key == 'g')||(key == 'G')) return 71;
	  if((key == 'h')||(key == 'H')) return 72;
	  if((key == 'i')||(key == 'I')) return 73;
	  if((key == 'j')||(key == 'J')) return 74;
	  if((key == 'k')||(key == 'K')) return 75;
	  if((key == 'l')||(key == 'L')) return 76;
	  if((key == 'm')||(key == 'M')) return 77;
	  if((key == 'n')||(key == 'N')) return 78;
	  if((key == 'o')||(key == 'O')) return 79;
	  if((key == 'p')||(key == 'P')) return 80;
	  if((key == 'q')||(key == 'Q')) return 81;
	  if((key == 'r')||(key == 'R')) return 82;
	  if((key == 's')||(key == 'S')) return 83;
	  if((key == 't')||(key == 'T')) return 84;
	  if((key == 'u')||(key == 'U')) return 85;
	  if((key == 'v')||(key == 'V')) return 86;
	  if((key == 'w')||(key == 'W')) return 87;
	  if((key == 'x')||(key == 'X')) return 88;
	  if((key == 'y')||(key == 'Y')) return 89;
	  if((key == 'z')||(key == 'Z')) return 90;
	  return -1;
	}

	/**
	 * Wrapper function that simply returns
	 * {@code java.awt.event.KeyEvent.getKeyText(key)}.
	 */
	public static String getKeyText(int key) {
		return KeyEvent.getKeyText(key);
	}

	/**
	 * Wrapper function that simply returns
	 * {@code java.awt.event.KeyEvent.getModifiersExText(mask)}.
	 */
	public static String getModifiersExText(int mask) {
		return KeyEvent.getModifiersExText(mask);
	}
}
