package remixlab.remixcam.devices;


import remixlab.proscene.Scene;
//import remixlab.proscene.Scene;
import remixlab.remixcam.core.*;
//import remixlab.remixcam.core.AbstractScene.Modifier;

public class DesktopEvents implements Constants {
	protected Scene scene;
	public DesktopEvents(Scene s) {
		scene = s;
	}
	
	/**
	 * Function that maps characters to virtual keys defined according to
	 * {@code java.awt.event.KeyEvent}.
	 */
	public static int getVKey(char key) {
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
	 * Wrapper function that simply returns what
	 * {@code java.awt.event.KeyEvent.getKeyText(key)} would return.
	 */
	public static String getKeyText(int key) {
    String result;
    switch (key) {
        case 48: result = "0"; break;
        case 49: result = "1"; break;
        case 50: result = "2"; break;
        case 51: result = "3"; break;
        case 52: result = "4"; break;
        case 53: result = "5"; break;
        case 54: result = "6"; break;
        case 55: result = "7"; break;
        case 56: result = "8"; break;
        case 57: result = "9"; break;
        case 65: result = "a"; break;
        case 66: result = "b"; break;
        case 67: result = "c"; break;
        case 68: result = "d"; break;
        case 69: result = "e"; break;
        case 70: result = "f"; break;
        case 71: result = "g"; break;
        case 72: result = "h"; break;
        case 73: result = "i"; break;
        case 74: result = "j"; break;
        case 75: result = "k"; break;
        case 76: result = "l"; break;
        case 77: result = "m"; break;
        case 78: result = "n"; break;
        case 79: result = "o"; break;
        case 80: result = "p"; break;
        case 81: result = "q"; break;
        case 82: result = "r"; break;
        case 83: result = "s"; break;
        case 84: result = "t"; break;
        case 85: result = "u"; break;
        case 86: result = "v"; break;
        case 87: result = "w"; break;
        case 88: result = "x"; break;
        case 89: result = "y"; break;
        case 90: result = "z"; break;
        
        case LEFT : result = "LEFT"; break;
        case UP : result = "UP"; break;
        case RIGHT : result = "RIGHT"; break;        
        case DOWN : result = "DOWN"; break;
        
        default: result = "Unrecognized key";
                 break;                 
    }
    return result;
	}
	
	public static String getModifiersText(int mask) {
		String r = new String();
		if((ALT & mask)       == ALT) r += "ALT";						
		if((SHIFT & mask)     == SHIFT) r += (r.length() > 0) ? "+SHIFT" : "SHIFT";
		if((CTRL & mask)      == CTRL) r += (r.length() > 0) ? "+CTRL" : "CTRL";
		if((META & mask)      == META) r += (r.length() > 0) ? "+META" : "META";
		if((ALT_GRAPH & mask) == ALT_GRAPH) r += (r.length() > 0) ? "+ALT_GRAPH" : "ALT_GRAPH";
		return r;
	}	
}
