package remixlab.remixcam.profile;

import remixlab.remixcam.core.*;

public class DOF1Profile extends DOFProfile<Constants.DOF_1Action> {
	public DOF1Profile(AbstractScene scn, String n) {
		super(scn, n);
	}
	
	@Override
	public void setDefaultBindings() {
		setBinding(DOF_1Action.ZOOM);
	}	
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