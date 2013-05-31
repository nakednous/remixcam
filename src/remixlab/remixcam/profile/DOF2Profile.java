package remixlab.remixcam.profile;

import remixlab.remixcam.core.*;
import remixlab.remixcam.shortcut.*;

public abstract class DOF2Profile extends AbstractProfile<ButtonShortcut> {
	public DOF2Profile(AbstractScene scn, String n) {
		super(scn, n);
	}
	
	@Override
	public void setDefaultBindings() {
	}

	/**
	 * Returns true if the given binding binds a camera mouse-action.
	 * 
	 * @param button
	 */	
	public boolean isCameraMouseBindingInUse(Integer button) {
		return bindings.isShortcutInUse(new ButtonShortcut(button));
	}
	
	/**
	 * Returns true if the given binding binds a camera mouse-action.
	 * 
	 * @param mask
	 * @param button
	 */
	public boolean isCameraMouseBindingInUse(Integer mask, Integer button) {
		return bindings.isShortcutInUse(new ButtonShortcut(mask, button));
	}

	/**
	 * Returns true if the given camera mouse-action is binded.
	 */
	public boolean isCameraMouseActionBinded(DOF_2Action action) {
		return bindings.isActionMapped(action.action());
	}

	/**
	 * Binds the camera mouse-action to the given binding
	 * 
	 * @param button
	 * @param action 
	 */
	public void setCameraMouseBinding(Integer button, DOF_2Action action) {
		if ( isCameraMouseBindingInUse(button) ) {
			DLAction a = cameraMouseBinding(button);
			System.out.println("Warning: overwritting binding which was previously associated to " + a);
		}
		bindings.setBinding(new ButtonShortcut(button), action.action());
	}
	
	/**
	 * Binds the camera mouse-action to the given binding
	 * 
	 * @param mask
	 * @param button
	 * @param action
	 * 
	 * <b>Attention:</b> Mac users should avoid using the CTRL modifier key, since its use is
	 * reserved to emulate the right button of the mouse.
	 */
	public void setCameraMouseBinding(Integer mask, Integer button, DOF_2Action action) {
		if ( isCameraMouseBindingInUse(mask, button) ) {
			DLAction a = cameraMouseBinding(mask, button);
			System.out.println("Warning: overwritting binding which was previously associated to " + a);
		}
		bindings.setBinding(new ButtonShortcut(mask, button), action.action());
	}

	/**
	 * Removes the camera mouse-action binding.
	 * 
	 * @param button
	 */
	public void removeCameraMouseBinding(Integer button) {
		bindings.removeBinding(new ButtonShortcut(button));
	}
	
	/**
	 * Removes the camera mouse-action binding.
	 * 
	 * @param mask
	 * @param button
	 */
	public void removeCameraMouseBinding(Integer mask, Integer button) {
		bindings.removeBinding(new ButtonShortcut(mask, button));
	}	
	
	/**
	 * Returns the camera mouse-action associated to the given binding.
	 * 
	 * @param button
	 */
	public DLAction cameraMouseBinding(Integer button) {
		return bindings.binding(new ButtonShortcut(button));
	}
	
	/**
	 * Returns the camera mouse-action associated to the given binding.
	 * 
	 * @param mask
	 * @param button
	 */
	public DLAction cameraMouseBinding(Integer mask, Integer button) {
		return bindings.binding(new ButtonShortcut(mask, button));
	}
}
