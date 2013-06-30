package remixlab.tersehandling.duoable.event;

import remixlab.tersehandling.duoable.profile.Actionable;
import remixlab.tersehandling.duoable.profile.Duoble;
import remixlab.tersehandling.event.ClickEvent;

public class GenericClickEvent <A extends Actionable<?>> extends ClickEvent implements Duoble<A> {
	Actionable<?> action;
	
	public GenericClickEvent(int b) {
		super(b);
	}
	
	public GenericClickEvent(int b, int clicks) {
		super(b, clicks);
	}
	
	public GenericClickEvent(Integer modifiers, int b) {
		super(modifiers, b);
	}
	
	public GenericClickEvent(Integer modifiers, int b, int clicks) {
		super(modifiers, b, clicks);
	}
	
	public GenericClickEvent(int b, Actionable<?> a) {
		super(b);
		action = a;
	}
	
	public GenericClickEvent(int b, int clicks, Actionable<?> a) {
		super(b, clicks);
		action = a;
	}
	
	public GenericClickEvent(Integer modifiers, int b, Actionable<?> a) {
		super(modifiers, b);
		action = a;
	}
	
	public GenericClickEvent(Integer modifiers, int b, int clicks, Actionable<?> a) {
		super(modifiers, b, clicks);
		action = a;
	}
	
	protected GenericClickEvent(GenericClickEvent<A> other) {
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
	public GenericClickEvent<A> get() {
		return new GenericClickEvent<A>(this);
	}
}
