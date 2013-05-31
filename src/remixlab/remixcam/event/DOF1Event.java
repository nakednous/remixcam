package remixlab.remixcam.event;

import remixlab.remixcam.shortcut.*;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

/**
//TODO should decide whether or not this class should go
//i.e., wheel event may be understood as a DOF1Event with button = 0
public class DOFEvent extends DLEvent {
@Override
public int hashCode() {
  return new HashCodeBuilder(17, 37).
  appendSuper(super.hashCode()).
	append(x).
  toHashCode();
}

@Override
public boolean equals(Object obj) {
	if (obj == null) return false;
	if (obj == this) return true;		
	if (obj.getClass() != getClass()) return false;
	
	DOFEvent other = (DOFEvent) obj;
	return new EqualsBuilder()
  .appendSuper(super.equals(obj))
	.append(x, other.x)
	.isEquals();
}

protected Float x;

public DOFEvent(float x) {
	super();
	this.x = x;
}

public DOFEvent(float x, int modifiers) {
	super(modifiers);
	this.x = x;
}  

public DOFEvent(float x, DLAction a) {
	super(a);
	this.x = x;
}


public DOFEvent(float x, int modifiers, DLAction a) {
  super(modifiers, a);
  this.x = x;
}

protected DOFEvent(DOFEvent other) {
	super(other);
	this.x = other.x;
}

@Override
public DOFEvent get() {
	return new DOFEvent(this);
}

public float getX() {
  return x;
}

public static DOFEvent deltaEvent(DOFEvent current, DOFEvent prev) {
	return new DOFEvent((current.getX() - prev.getX()), current.modifiers, current.action);
}

public DOFEvent deltaEvent(DOFEvent prev) {
	return deltaEvent(this, prev);
}
}
//*/


/**
public class DOF1Event extends DOFEvent {
	@Override
	public int hashCode() {
    return new HashCodeBuilder(17, 37).
    appendSuper(super.hashCode()).
		append(button).
    toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;		
		if (obj.getClass() != getClass()) return false;
		
		DOF1Event other = (DOF1Event) obj;
		return new EqualsBuilder()
    .appendSuper(super.equals(obj))
		.append(button, other.button)
		.isEquals();
	}

	protected Integer button;
	
  public DOF1Event(float x) {
  	super(x);
  	this.button = 0;
  }
  
  public DOF1Event(float x, int button) {
  	super(x);
  	this.button = button;
  }
  
  public DOF1Event(float x, int modifiers, int button) {
  	super(x, modifiers);
  	this.button = button;
  }  
  
  public DOF1Event(float x, DLAction a) {
  	super(x, a);
  	this.button = 0;
  }
  
  public DOF1Event(float x, int button, DLAction a) {
  	super(x, a);
  	this.button = button;
  }

  public DOF1Event(float x, int modifiers, int button, DLAction a) {
    super(x, modifiers, a);
    this.button = button;
  }
  
  protected DOF1Event(DOF1Event other) {
  	super(other);
  	this.x = other.x;
		this.button = other.button;
	}
  
  @Override
	public DOF1Event get() {
		return new DOF1Event(this);
	}
  
  public float getX() {
    return x;
  }
  
	public int getButton() {
		return button;
	}

	@Override
	public ButtonShortcut shortcut() {
		//TODO test this condition
		if( getButton() == 0 )
			return new ButtonShortcut(getModifiers());
		return new ButtonShortcut(getModifiers(), getButton());
	}
  
  public static DOF1Event deltaEvent(DOF1Event current, DOF1Event prev) {
  	return new DOF1Event((current.getX() - prev.getX()), current.modifiers, current.button, current.action);
  }
  
  public DOF1Event deltaEvent(DOF1Event prev) {
  	return deltaEvent(this, prev);
  }
}
//*/

// /**
public class DOF1Event extends DLEvent {
	@Override
	public int hashCode() {
    return new HashCodeBuilder(17, 37).
    appendSuper(super.hashCode()).
		append(button).
		append(x).
    toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;		
		if (obj.getClass() != getClass()) return false;
		
		DOF1Event other = (DOF1Event) obj;
		return new EqualsBuilder()
    .appendSuper(super.equals(obj))
		.append(button, other.button)
		.append(x, other.x)
		.isEquals();
	}

	protected Float x;
	protected Integer button;
	
  public DOF1Event(float x) {
  	super();
  	this.x = x;
  	this.button = NOBUTTON;
  }
  
  public DOF1Event(float x, int button) {
  	super();
  	this.x = x;
  	this.button = button;
  }
  
  public DOF1Event(float x, int modifiers, int button) {
  	super(modifiers);
  	this.x = x;
  	this.button = button;
  }  
  
  public DOF1Event(float x, DLAction a) {
  	super(a);
  	this.x = x;
  	this.button = NOBUTTON;
  }
  
  public DOF1Event(float x, int button, DLAction a) {
  	super(a);
  	this.x = x;
  	this.button = button;
  }

  public DOF1Event(float x, int modifiers, int button, DLAction a) {
    super(modifiers, a);
    this.x = x;
    this.button = button;
  }
  
  protected DOF1Event(DOF1Event other) {
  	super(other);
  	this.x = other.x;
		this.button = other.button;
	}
  
  @Override
	public DOF1Event get() {
		return new DOF1Event(this);
	}
  
  public float getX() {
    return x;
  }
  
	public int getButton() {
		return button;
	}
	
	@Override
	public ButtonShortcut shortcut() {
		if( getButton() == 0 )
			return new ButtonShortcut(getModifiers());
		return new ButtonShortcut(getModifiers(), getButton());
	}
  
  public static DOF1Event deltaEvent(DOF1Event current, DOF1Event prev) {
  	return new DOF1Event((current.getX() - prev.getX()), current.modifiers, current.button, current.action);
  }
  
  public DOF1Event deltaEvent(DOF1Event prev) {
  	return deltaEvent(this, prev);
  }
}
// */
