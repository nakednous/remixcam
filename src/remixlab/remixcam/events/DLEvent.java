package remixlab.remixcam.events;

import remixlab.remixcam.core.Constants;

public class DLEvent implements Constants {
  protected long millis;
  protected int action;
  protected int modifiers;

  // Types of events. As with all constants in Processing, brevity's preferred.
  static public final int KEY = 1;
  static public final int MOUSE = 2;
  static public final int TOUCH = 3;
  protected int flavor;

  public DLEvent(long millis, int action, int modifiers) {    
    this.millis = millis;
    this.action = action;
    this.modifiers = modifiers;
  }

  public int getFlavor() {
    return flavor;
  }

  public long getMillis() {
    return millis;
  }

  public int getAction() {
    return action;
  }

  public int getModifiers() {
    return modifiers;
  }

  public boolean isShiftDown() {
    return (modifiers & SHIFT) != 0;
  }

  public boolean isControlDown() {
    return (modifiers & CTRL) != 0;
  }

  public boolean isMetaDown() {
    return (modifiers & META) != 0;
  }

  public boolean isAltDown() {
    return (modifiers & ALT) != 0;
  }
  
  public boolean isAltGraph() {
    return (modifiers & ALT_GRAPH) != 0;
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
