package remixlab.remixcam.event;

import remixlab.remixcam.core.AbstractScene;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

public class DLMouseEvent extends HIDeviceEvent {
	@Override
	public int hashCode() {
    return new HashCodeBuilder(17, 37).
    appendSuper(super.hashCode()).
		append(x).
		append(y).
		append(button).
		append(amount).
    toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;		
		if (obj.getClass() != getClass()) return false;
		
		DLMouseEvent other = (DLMouseEvent) obj;
		return new EqualsBuilder()
    .appendSuper(super.equals(obj))		
    .append(x, other.x)
		.append(y, other.y)
		.append(button, other.button)
		.append(amount, other.amount)
		.isEquals();
	}
	
  static public final int PRESS = 1;
  static public final int RELEASE = 2;
  static public final int CLICK = 3;
  static public final int DRAG = 4;
  static public final int MOVE = 5;
  static public final int ENTER = 6;
  static public final int EXIT = 7;
  static public final int WHEEL = 8;

  protected Integer x, y;
  protected Integer button;
  protected Float amount;
  
  public DLMouseEvent(AbstractScene scn) {
  	super(scn);
  	this.x = null;
  	this.y = null;
  	this.button = null;
  	this.amount = null;
  }
  
  public DLMouseEvent() {
  	this.x = null;
  	this.y = null;
  	this.button = null;
  	this.amount = null;
  }
  
  public DLMouseEvent(AbstractScene scn, int action, int modifiers,
      int x, int y, int button, float amount) {
  	super(scn, action, modifiers);
  	this.x = x;
  	this.y = y;
  	this.button = button;
  	this.amount = amount;
  }

  public DLMouseEvent(int action, int modifiers,
                      int x, int y, int button, float amount) {  //int clickCount) {
    super(action, modifiers);
    this.x = x;
    this.y = y;
    this.button = button;
    this.amount = amount;
  }
  
  protected DLMouseEvent(DLMouseEvent other) {
  	super(other);
		this.x = other.x;
		this.y = other.y;
		this.button = other.button;
  	this.amount = other.amount;
	}
  
  @Override
	public DLMouseEvent get() {
		return new DLMouseEvent(this);
	}

  public int getX() {
    return x;
  }
  
  public void setX(int val) {
  	x = val;
  }

  public int getY() {
    return y;
  }
  
  public void setY(int val) {
  	y = val;
  }

  /** Which button was pressed, either LEFT, CENTER, or RIGHT. */
  public int getButton() {
    return button;
  }
  
  public void setButton(int val) {
  	button = val;
  }

  public Integer getClickCount() {
    return (int) amount.floatValue(); //clickCount;
  }
  
  public void setClickCount(float val) {
  	amount = val;
  }

  /**
   * Number of clicks for mouse button events, or the number of steps (positive
   * or negative depending on direction) for a mouse wheel event.
   */
  public float getAmount() {
    return amount;
  }
  
  public void setAmount(float val) {
  	amount = val;
  }
}
