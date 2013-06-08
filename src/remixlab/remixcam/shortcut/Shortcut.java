package remixlab.remixcam.shortcut;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

import remixlab.remixcam.core.*;
import remixlab.remixcam.event.*;

public class Shortcut implements Constants, Copyable {
	@Override
	public int hashCode() {
    return new HashCodeBuilder(17, 37).		
		append(mask).
    toHashCode();		
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;		
		if (obj.getClass() != getClass()) return false;		
		
		Shortcut other = (Shortcut) obj;
	  return new EqualsBuilder()		
		.append(mask, other.mask)
		.isEquals();
	}	
	
	//TODO pending ButtonShortcut fix!
	//protected final Integer mask;
	protected Integer mask;
	
	public Shortcut(Integer m) {
		mask = m;
	}
	
	public Shortcut() {
		mask = NOMODIFIER_MASK;
	}
	
	protected Shortcut(Shortcut other) {
		this.mask = new Integer(other.mask);
	}
	
	@Override
	public Shortcut get() {
		return new Shortcut(this);
	}
	
	public String description() {
		return DLEvent.getModifiersText(mask);
	}
}
