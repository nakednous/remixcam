package remixlab.remixcam.profile;

import java.util.Map;

import remixlab.remixcam.core.*;
import remixlab.remixcam.shortcut.ButtonShortcut;

public class DOF1Profile extends AbstractMotionProfile<Constants.DOF_1Action> {
	public DOF1Profile() {
		super();
	}
	
	protected DOF1Profile(DOF1Profile other) {
		bindings = new Bindings<ButtonShortcut, DOF_1Action>();    
    for (Map.Entry<ButtonShortcut, DOF_1Action> entry : other.bindings.map.entrySet()) {
    	ButtonShortcut key = entry.getKey().get();
    	DOF_1Action value = entry.getValue();
    	bindings.setBinding(key, value);
    }
	}
	
  @Override
	public DOF1Profile get() {
		return new DOF1Profile(this);
	}
  
	/**
	public DOF1Profile(AbstractScene scn, String n) {
		super(scn, n);
	}
	*/	
}

/**
public abstract class WheelProfile extends AbstractProfile<Shortcut> {
	public WheelProfile(AbstractScene scn, String n) {
		super(scn, n);
	}
	
	@Override
	public void setDefaultBindings() {
		setWheelBinding(DOF_1Action.ZOOM);
	}
	
	public boolean isWheelBindingInUse(Integer mask) {
		return bindings.isShortcutInUse(new Shortcut(mask));
	}

	public boolean isWheelActionBound(DOF_1Action action) {
		return bindings.isActionMapped(action.action());
	}
	
	public void setWheelBinding(DOF_1Action action) {
		setWheelBinding(0, action);
	}

	public void setWheelBinding(Integer mask, DOF_1Action action) {
		if ( isWheelBindingInUse(mask) ) {
			DLAction a = wheelBinding(mask);
			System.out.println("Warning: overwritting binding which was previously associated to " + a);
		}
		bindings.setBinding(new Shortcut(mask), action.action());
	}
	
	public void removeWheelBinding() {
		removeWheelBinding(0);
	}

	public void removeWheelBinding(Integer mask) {
		bindings.removeBinding(new Shortcut(mask));
	}

	public DLAction wheelBinding() {
		return bindings.binding(new Shortcut(0));
	}
	
	public DLAction wheelBinding(Integer mask) {
		return bindings.binding(new Shortcut(mask));
	}	
}
*/