package remixlab.tersehandling.duoable.event;

import remixlab.tersehandling.duoable.profile.Actionable;
import remixlab.tersehandling.duoable.profile.Duoble;
import remixlab.tersehandling.event.THDOF6Event;

public class GenericDOF6Event<A extends Actionable<?>> extends THDOF6Event implements Duoble<A> {
	Actionable<?> action;
	
	public GenericDOF6Event(float x, float y, float z, float rx, float ry, float rz, int modifiers, int button) {
		super(x, y, z, rx, ry, rz, modifiers, button);
	}
	
	public GenericDOF6Event(GenericDOF6Event<A> prevEvent, float x, float y, float z, float rx, float ry, float rz, int modifiers, int button) {
		super(prevEvent, x, y, z, rx, ry, rz, modifiers, button);
	}
	
	public GenericDOF6Event(float x, float y, float z, float rx, float ry, float rz) {
		super(x, y, z, rx, ry, rz);
	}
	
	public GenericDOF6Event(GenericDOF6Event<A> prevEvent, float x, float y, float z, float rx, float ry, float rz) {
		super(prevEvent, x, y, z, rx, ry, rz);
	}
	
	public GenericDOF6Event(float x, float y, float z, float rx, float ry, float rz, int modifiers, int button, Actionable<?> a) {
		super(x, y, z, rx, ry, rz, modifiers, button);
		action = a;
	}
	
	public GenericDOF6Event(GenericDOF6Event<A> prevEvent, float x, float y, float z, float rx, float ry, float rz, int modifiers, int button, Actionable<?> a) {
		super(prevEvent, x, y, z, rx, ry, rz, modifiers, button);
		action = a;
	}
	
	public GenericDOF6Event(float x, float y, float z, float rx, float ry, float rz, Actionable<?> a) {
		super(x, y, z, rx, ry, rz);
		action = a;
	}
	
	public GenericDOF6Event(GenericDOF6Event<A> prevEvent, float x, float y, float z, float rx, float ry, float rz, Actionable<?> a) {
		super(prevEvent, x, y, z, rx, ry, rz);
		action = a;
	}
	
	protected GenericDOF6Event(GenericDOF6Event<A> other) {
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
	public GenericDOF6Event<A> get() {
		return new GenericDOF6Event<A>(this);
	}
}
