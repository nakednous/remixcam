package remixlab.tersehandling.duoable.event;

import remixlab.tersehandling.duoable.profile.Actionable;
import remixlab.tersehandling.duoable.profile.Duoble;
import remixlab.tersehandling.event.THDOF2Event;

public class GenericDOF2Event<A extends Actionable<?>> extends THDOF2Event implements Duoble<A> {
	Actionable<?> action;
	
	public GenericDOF2Event(float x, float y) {
		super(x, y);
	}
	
	public GenericDOF2Event(GenericDOF2Event<A> prevEvent, float x, float y) {
		super(prevEvent, x, y);
	}
	
	public GenericDOF2Event(GenericDOF2Event<A> prevEvent, float x, float y, int modifiers, int button) {
		super(prevEvent, x, y, modifiers, button);
	}
	
	public GenericDOF2Event(float x, float y, int modifiers, int button) {
		super(x, y, modifiers, button);
	}
	
	public GenericDOF2Event(float x, float y, Actionable<?> a) {
		super(x, y);
		action = a;
	}
	
	public GenericDOF2Event(GenericDOF2Event<A> prevEvent, float x, float y, Actionable<?> a) {
		super(prevEvent, x, y);
		action = a;
	}
	
	public GenericDOF2Event(GenericDOF2Event<A> prevEvent, float x, float y, int modifiers, int button, Actionable<?> a) {
		super(prevEvent, x, y, modifiers, button);
		action = a;
	}
	
	public GenericDOF2Event(float x, float y, int modifiers, int button, Actionable<?> a) {
		super(x, y, modifiers, button);
		action = a;
	}
	
	protected GenericDOF2Event(GenericDOF2Event<A> other) {
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
	public GenericDOF2Event<A> get() {
		return new GenericDOF2Event<A>(this);
	}
}
