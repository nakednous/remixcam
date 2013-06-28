package remixlab.tersehandling.core;

public interface TConstants {
  //modifier keys
  static public final int NOMODIFIER_MASK    = 0;
  static public final int SHIFT              = 1 << 0;
  static public final int CTRL               = 1 << 1;
  static public final int META               = 1 << 2;
  static public final int ALT                = 1 << 3;
  static public final int ALT_GRAPH          = 1 << 4;
  
  static public final int SHIFT_DOWN         = 64;
  static public final int CTRL_DOWN          = 128;
  static public final int META_DOWN          = 256;
  static public final int ALT_DOWN           = 512;
  static public final int ALT_GRAPH_DOWN     = 8192;
  
  static final int NOBUTTON	= 0;
  
  static final int CENTER = 3;
  
  //Arrows  
  static final int LEFT  = 37;
  static final int UP    = 38;
  static final int RIGHT = 39;
  static final int DOWN  = 40; 
}
