package remixlab.remixcam.shortcut;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

import remixlab.remixcam.core.AbstractScene;

public abstract class AbstractShortcut {
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
		
		AbstractShortcut other = (AbstractShortcut) obj;
	  return new EqualsBuilder()		
		.append(mask, other.mask)
		.isEquals();
	}	
	
	protected final Integer mask;
	
	public AbstractShortcut(Integer m) {
		mask = m;
	}
	
	public AbstractShortcut() {
		mask = 0;
	}
	
	public String description() {
		AbstractScene.showMissingImplementationWarning("description");
		return null;
	}
}
