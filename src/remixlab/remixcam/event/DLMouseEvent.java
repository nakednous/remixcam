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
		.isEquals();
	}

  protected Integer x, y;
  protected Integer button;
  
  public DLMouseEvent() {
  	this.x = null;
  	this.y = null;
  	this.button = null;
  }
  
  public DLMouseEvent(int modifiers, int x, int y, int button, int amount) {
  	super(modifiers);
  	this.x = x;
  	this.y = y;
  	this.button = button;
  }

  public DLMouseEvent(int action, int modifiers, int x, int y, int button, int amount) {
    super(modifiers);
    this.x = x;
    this.y = y;
    this.button = button;
  }
  
  protected DLMouseEvent(DLMouseEvent other) {
  	super(other);
		this.x = other.x;
		this.y = other.y;
		this.button = other.button;
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
}
