package remixlab.tersehandling.duoable.event;

import remixlab.tersehandling.duoable.profile.Actionable;
import remixlab.tersehandling.duoable.profile.Duoble;
import remixlab.tersehandling.event.THKeyboardEvent;

public class GenericKeyboardEvent<A extends Actionable<?>> extends THKeyboardEvent implements Duoble<A> {
	Actionable<?> action;
	
	public GenericKeyboardEvent() {
		super();
	}
	
	public GenericKeyboardEvent(Integer modifiers, Character c, Integer vk) {
		super(modifiers, c, vk);
	}
	
	public GenericKeyboardEvent(Integer modifiers, Character c) {
		super(modifiers, c);
	}
	
	public GenericKeyboardEvent(Integer modifiers, Integer vk) {
		super(modifiers, vk);
	}
	
	public GenericKeyboardEvent(Character c) {
		super(c);
	}
	
	public GenericKeyboardEvent(Actionable<?> a) {
		super();
		action = a;
	}
	
	public GenericKeyboardEvent(Integer modifiers, Character c, Integer vk, Actionable<?> a) {
		super(modifiers, c, vk);
		action = a;
	}
	
	public GenericKeyboardEvent(Integer modifiers, Character c, Actionable<?> a) {
		super(modifiers, c);
		action = a;
	}
	
	public GenericKeyboardEvent(Integer modifiers, Integer vk, Actionable<?> a) {
		super(modifiers, vk);
		action = a;
	}
	
	public GenericKeyboardEvent(Character c, Actionable<?> a) {
		super(c);
		action = a;
	}
	
	protected GenericKeyboardEvent(GenericKeyboardEvent<A> other) {
		super(other);
		action = other.action;
	}
	
	@Override
	public Actionable<?> getAction() {
		return action;
	}
	
	@Override
	public void setAction(Actionable<?> a) {
		if( a instanceof Actionable<?> ) action = a;
	}
	
	@Override
	public GenericKeyboardEvent<A> get() {
		return new GenericKeyboardEvent<A>(this);
	}
}
