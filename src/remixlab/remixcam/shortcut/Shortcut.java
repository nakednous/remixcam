package remixlab.remixcam.shortcut;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

import remixlab.remixcam.event.DLEvent;

//WHEEL
public class Shortcut {
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
	
	protected final Integer mask;
	
	public Shortcut(Integer m) {
		mask = m;
	}
	
	public Shortcut() {
		mask = 0;
	}
	
	public String description() {
		return DLEvent.getModifiersText(mask);
	}
}
