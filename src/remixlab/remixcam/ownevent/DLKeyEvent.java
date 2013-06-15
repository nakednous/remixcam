package remixlab.remixcam.ownevent;

import remixlab.remixcam.core.Constants;
import remixlab.remixcam.event.VKeyEvent;

/**
import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;
*/

public class DLKeyEvent extends VKeyEvent<Constants.DOF_0Action> {
	public DLKeyEvent() {
		super();
	}
	
	public DLKeyEvent(DOF_0Action a) {
		super(a);
	}
	
	public DLKeyEvent(Integer modifiers, Character c, Integer vk) {
		super(modifiers, c, vk);
	}
	
	public DLKeyEvent(Integer modifiers, Character c) {
		super(modifiers, c);
	}
	
	public DLKeyEvent(Integer modifiers, Integer vk) {
		super(modifiers, vk);
	}
	
	public DLKeyEvent(Character c) {
		super(c);
	}
	
	public DLKeyEvent(Integer modifiers, Character c, Integer vk, DOF_0Action a) {
		super(modifiers, c, vk, a);
	}
	
	public DLKeyEvent(Integer modifiers, Character c, DOF_0Action a) {
		super(modifiers, c, a);
	}
	
	public DLKeyEvent(Integer modifiers, Integer vk, DOF_0Action a) {
		super(modifiers, vk, a);
	}
	
	public DLKeyEvent(Character c, DOF_0Action a) {
		super(c,a);
	}
	
	protected DLKeyEvent(DLKeyEvent other) {
		super(other);
	}
	
	@Override
	public DLKeyEvent get() {
		return new DLKeyEvent(this);
	}
	
	@Override
	public DOF_0Action getAction() {
  	return (DOF_0Action)action;
  }
}
