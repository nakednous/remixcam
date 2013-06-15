package remixlab.remixcam.interactivity;

import remixlab.remixcam.core.Constants;
import remixlab.remixcam.event.GenericKeyboardEvent;

/**
import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;
*/

public class KeyboardEvent extends GenericKeyboardEvent<Constants.DOF_0Action> {
	public KeyboardEvent() {
		super();
	}
	
	public KeyboardEvent(DOF_0Action a) {
		super(a);
	}
	
	public KeyboardEvent(Integer modifiers, Character c, Integer vk) {
		super(modifiers, c, vk);
	}
	
	public KeyboardEvent(Integer modifiers, Character c) {
		super(modifiers, c);
	}
	
	public KeyboardEvent(Integer modifiers, Integer vk) {
		super(modifiers, vk);
	}
	
	public KeyboardEvent(Character c) {
		super(c);
	}
	
	public KeyboardEvent(Integer modifiers, Character c, Integer vk, DOF_0Action a) {
		super(modifiers, c, vk, a);
	}
	
	public KeyboardEvent(Integer modifiers, Character c, DOF_0Action a) {
		super(modifiers, c, a);
	}
	
	public KeyboardEvent(Integer modifiers, Integer vk, DOF_0Action a) {
		super(modifiers, vk, a);
	}
	
	public KeyboardEvent(Character c, DOF_0Action a) {
		super(c,a);
	}
	
	protected KeyboardEvent(KeyboardEvent other) {
		super(other);
	}
	
	@Override
	public KeyboardEvent get() {
		return new KeyboardEvent(this);
	}
	
	@Override
	public DOF_0Action getAction() {
  	return (DOF_0Action)action;
  }
}
