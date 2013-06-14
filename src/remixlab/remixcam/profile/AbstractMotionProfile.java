package remixlab.remixcam.profile;

import remixlab.remixcam.action.VActionable;
import remixlab.remixcam.shortcut.*;

public abstract class AbstractMotionProfile<A extends VActionable> extends AbstractProfile<ButtonShortcut, A> {
	/**
	public AbstractMotionProfile(AbstractScene scn, String n) {
		super(scn, n);
	}
	*/
	
	public boolean isBindingInUse() {
		return isBindingInUse(NOMODIFIER_MASK, NOBUTTON);
	}
	
	/**
	 * Returns true if the given binding binds a camera mouse-action.
	 * 
	 * @param button
	 */	
	public boolean isBindingInUse(Integer button) {
		return isBindingInUse(NOMODIFIER_MASK, button);
	}
	
	/**
	 * Returns true if the given binding binds a camera mouse-action.
	 * 
	 * @param mask
	 * @param button
	 */
	public boolean isBindingInUse(Integer mask, Integer button) {
		return bindings.isShortcutInUse(new ButtonShortcut(mask, button));
	}

	/**
	 * Returns true if the given camera mouse-action is bound.
	 */
	public boolean isActionBound(A action) {
		return bindings.isActionMapped(action);
	}
	
	/**
	 * Convenience function that simply calls {@code setWheelShortcut(0, action)}
	 * 
	 * @see #setWheelBinding(Integer, A)
	 */
	public void setBinding(A action) {
		setBinding(NOBUTTON, action);
	}

	/**
	 * Binds the camera mouse-action to the given binding
	 * 
	 * @param button
	 * @param action 
	 */
	public void setBinding(Integer button, A action) {
		setBinding(NOMODIFIER_MASK, button, action);
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
	public void setBinding(Integer mask, Integer button, A action) {
		if ( isBindingInUse(mask, button) ) {
			VActionable a = binding(mask, button);
			System.out.println("Warning: overwritting binding which was previously associated to " + a);
		}
		bindings.setBinding(new ButtonShortcut(mask, button), action);
	}
	
	/**
	 * Convenience function that simply calls {@code removeWheelShortcut(0)}.
	 * 
	 * @see #removeWheelBinding(Integer)
	 */
	public void removeBinding() {
		removeBinding(NOMODIFIER_MASK, NOBUTTON);
	}

	/**
	 * Removes the camera mouse-action binding.
	 * 
	 * @param button
	 */
	public void removeBinding(Integer button) {
		removeBinding(NOMODIFIER_MASK, button);
	}
	
	/**
	 * Removes the camera mouse-action binding.
	 * 
	 * @param mask
	 * @param button
	 */
	public void removeBinding(Integer mask, Integer button) {
		bindings.removeBinding(new ButtonShortcut(mask, button));
	}	
	
	public VActionable binding() {
		return binding(NOMODIFIER_MASK, NOBUTTON);
	}
	
	/**
	 * Returns the camera mouse-action associated to the given binding.
	 * 
	 * @param button
	 */
	public VActionable binding(Integer button) {
		return binding(NOMODIFIER_MASK, button);
	}
	
	/**
	 * Returns the camera mouse-action associated to the given binding.
	 * 
	 * @param mask
	 * @param button
	 */
	public VActionable binding(Integer mask, Integer button) {
		return bindings.binding(new ButtonShortcut(mask, button));
	}
}