package remixlab.remixcam.event;

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
		append(count).
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
		.append(count, other.count)
		.isEquals();
	}

  protected Integer x, y;
  protected Integer button;
  protected Integer count;
  
  public DLMouseEvent() {
  	this.x = null;
  	this.y = null;
  	this.button = null;
  	this.count = null;
  }
  
  public DLMouseEvent(int modifiers, int x, int y, int button, int amount) {
  	super(modifiers);
  	this.x = x;
  	this.y = y;
  	this.button = button;
  	this.count = amount;
  }

  public DLMouseEvent(int action, int modifiers, int x, int y, int button, int amount) {
    super(modifiers);
    this.x = x;
    this.y = y;
    this.button = button;
    this.count = amount;
  }
  
  protected DLMouseEvent(DLMouseEvent other) {
  	super(other);
		this.x = other.x;
		this.y = other.y;
		this.button = other.button;
  	this.count = other.count;
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
    return count; //clickCount;
  }
  
  public void setClickCount(int val) {
  	count = val;
  }

  /**
   * Number of clicks for mouse button events, or the number of steps (positive
   * or negative depending on direction) for a mouse wheel event.
   */
  public float getCount() {
    return count;
  }
  
  public void setCount(int val) {
  	count = val;
  }
}
