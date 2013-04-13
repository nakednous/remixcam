package remixlab.remixcam.events;

public class DLMouseEvent extends DLEvent {
  static public final int PRESS = 1;
  static public final int RELEASE = 2;
  static public final int CLICK = 3;
  static public final int DRAG = 4;
  static public final int MOVE = 5;
  static public final int ENTER = 6;
  static public final int EXIT = 7;
  static public final int WHEEL = 8;

  protected int x, y;
  protected int button;
  protected float amount;

  public DLMouseEvent(long millis, int action, int modifiers,
                      int x, int y, int button, float amount) {  //int clickCount) {
    super(millis, action, modifiers);
    this.flavor = MOUSE;
    this.x = x;
    this.y = y;
    this.button = button;
    this.amount = amount;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  /** Which button was pressed, either LEFT, CENTER, or RIGHT. */
  public int getButton() {
    return button;
  }

  public int getClickCount() {
    return (int) amount; //clickCount;
  }

  /**
   * Number of clicks for mouse button events, or the number of steps (positive
   * or negative depending on direction) for a mouse wheel event.
   */
  public float getAmount() {
    return amount;
  }
}
