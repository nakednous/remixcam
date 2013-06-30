package remixlab.tersehandling.duoable.event;

import remixlab.tersehandling.duoable.profile.Actionable;
import remixlab.tersehandling.duoable.profile.Duoble;
import remixlab.tersehandling.event.THDOF3Event;

public class GenericDOF3Event<A extends Actionable<?>> extends THDOF3Event implements Duoble<A> {
	Actionable<?> action;
	
	public GenericDOF3Event(float x, float y, float z, int modifiers, int button) {
		super(x, y, z, modifiers, button);
	}
	
	public GenericDOF3Event(GenericDOF3Event<A> prevEvent, float x, float y, float z, int modifiers, int button) {
		super(prevEvent, x, y, z, modifiers, button);
	}
	
	public GenericDOF3Event(float x, float y, float z) {
		super(x, y, z);
	}
	
	public GenericDOF3Event(GenericDOF3Event<A> prevEvent, float x, float y, float z) {
		super(prevEvent, x, y, z);
	}
	
	public GenericDOF3Event(float x, float y, float z, int modifiers, int button, Actionable<?> a) {
		super(x, y, z, modifiers, button);
		action = a;
	}
	
	public GenericDOF3Event(GenericDOF3Event<A> prevEvent, float x, float y, float z, int modifiers, int button, Actionable<?> a) {
		super(prevEvent, x, y, z, modifiers, button);
		action = a;
	}
	
	public GenericDOF3Event(float x, float y, float z, Actionable<?> a) {
		super(x, y, z);
		action = a;
	}
	
	public GenericDOF3Event(GenericDOF3Event<A> prevEvent, float x, float y, float z, Actionable<?> a) {
		super(prevEvent, x, y, z);
		action = a;
	}
	
	protected GenericDOF3Event(GenericDOF3Event<A> other) {
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
	public GenericDOF3Event<A> get() {
		return new GenericDOF3Event<A>(this);
	}
}
