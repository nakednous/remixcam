package remixlab.remixcam.profile;

import remixlab.remixcam.core.*;
import remixlab.remixcam.shortcut.*;

public abstract class DOF1Profile extends AbstractProfile<ButtonShortcut> {
	public DOF1Profile(AbstractScene scn, String n) {
		super(scn, n);
	}
	
	@Override
	public void setDefaultBindings() {
		setWheelBinding(DOF_1Action.ZOOM);
	}
	
	/**
	 * Returns true if the given binding binds a camera wheel-action.
	 * 
	 * @param mask binding
	 */
	public boolean isWheelBindingInUse(Integer mask) {
		return bindings.isShortcutInUse(new ButtonShortcut(mask));
	}

	/**
	 * Returns true if the given camera wheel-action is bound.
	 * 
	 * @param action
	 */
	public boolean isWheelActionBound(DOF_1Action action) {
		return bindings.isActionMapped(action.action());
	}
	
	/**
	 * Convenience function that simply calls {@code setWheelShortcut(0, action)}
	 * 
	 * @see #setWheelBinding(Integer, DOF_1Action)
	 */
	public void setWheelBinding(DOF_1Action action) {
		setWheelBinding(0, action);
	}

	/**
	 * Binds the camera wheel-action to the given binding.
	 * 
	 * @param mask modifier mask defining the binding
	 * 
	 * @see #setWheelBinding(DOF_1Action)
	 * 
	 * <b>Attention:</b> Mac users should avoid using the CTRL modifier key, since its use is
	 * reserved to emulate the right button of the mouse.
	 */
	public void setWheelBinding(Integer mask, DOF_1Action action) {
		if ( isWheelBindingInUse(mask) ) {
			DLAction a = wheelBinding(mask);
			System.out.println("Warning: overwritting binding which was previously associated to " + a);
		}
		bindings.setBinding(new ButtonShortcut(mask), action.action());
	}
	
	/**
	 * Convenience function that simply calls {@code removeWheelShortcut(0)}.
	 * 
	 * @see #removeWheelBinding(Integer)
	 */
	public void removeWheelBinding() {
		removeWheelBinding(0);
	}

	/**
	 * Removes the camera wheel-action binding.
	 * 
	 * @param mask shortcut
	 * 
	 * @see #removeWheelBinding()
	 */
	public void removeWheelBinding(Integer mask) {
		bindings.removeBinding(new ButtonShortcut(mask));
	}

	/**
	 * Convenience function that simply returns {@code bindings.binding(0)}.
	 * 
	 * @see #wheelBinding(Integer)
	 */
	public DLAction wheelBinding() {
		return bindings.binding(new ButtonShortcut(0));
	}
	
	/**
	 * Returns the camera wheel-action associated to the given binding.
	 * 
	 * @param mask binding
	 * 
	 * @see #wheelBinding()
	 */
	public DLAction wheelBinding(Integer mask) {
		return bindings.binding(new ButtonShortcut(mask));
	}	
}