package remixlab.remixcam.devices;

public class Mouse {
	/**
	 * Constants associated to the different mouse buttons which follow java conventions.
	 */
	public enum Button {
		// values correspond to: BUTTON1_DOWN_MASK, BUTTON2_DOWN_MASK and BUTTON3_DOWN_MASK
		// see: http://download-llnw.oracle.com/javase/6/docs/api/constant-values.html
		LEFT(1024), MIDDLE(2048), RIGHT(4096);
		public final int ID;
    Button(int code) {
    	this.ID = code;
    }    
    //The following code works but is considered overkill :)
    /**
    public int id() { return ID; }
    private static final Map<Integer,Button> lookup = new HashMap<Integer,Button>();
    static {
    	for(Button s : EnumSet.allOf(Button.class))
         lookup.put(s.id(), s);
    }
    public static Button get(int code) { 
      return lookup.get(code);
    }
    // */
	}
}
