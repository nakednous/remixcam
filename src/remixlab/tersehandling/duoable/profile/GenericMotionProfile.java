/**
 *                  TerseHandling (version 0.70.0)      
 *           Copyright (c) 2013 by Jean Pierre Charalambos
 *                 @author Jean Pierre Charalambos      
 *              https://github.com/nakednous/remixcam
 *                           
 * This library provides classes to ease the creation of interactive scenes.
 * 
 * This source file is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option)
 * any later version.
 * 
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 * 
 * A copy of the GNU General Public License is available on the World Wide Web
 * at <http://www.gnu.org/copyleft/gpl.html>. You can also obtain it by
 * writing to the Free Software Foundation, 51 Franklin Street, Suite 500
 * Boston, MA 02110-1335, USA.
 */

package remixlab.tersehandling.duoable.profile;

import remixlab.tersehandling.shortcut.*;

public class GenericMotionProfile<A extends Actionable<?>> extends GenericProfile<ButtonShortcut, A> {
	/**
	public AbstractMotionProfile(AbstractScene scn, String n) {
		super(scn, n);
	}
	*/
	
	public boolean isBindingInUse() {
		return isBindingInUse(TH_NOMODIFIER_MASK, TH_NOBUTTON);
	}
	
	/**
	 * Returns true if the given binding binds a camera mouse-action.
	 * 
	 * @param button
	 */	
	public boolean isBindingInUse(Integer button) {
		return isBindingInUse(TH_NOMODIFIER_MASK, button);
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
		setBinding(TH_NOBUTTON, action);
	}

	/**
	 * Binds the camera mouse-action to the given binding
	 * 
	 * @param button
	 * @param action 
	 */
	public void setBinding(Integer button, A action) {
		setBinding(TH_NOMODIFIER_MASK, button, action);
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
			Actionable<?> a = binding(mask, button);
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
		removeBinding(TH_NOMODIFIER_MASK, TH_NOBUTTON);
	}

	/**
	 * Removes the camera mouse-action binding.
	 * 
	 * @param button
	 */
	public void removeBinding(Integer button) {
		removeBinding(TH_NOMODIFIER_MASK, button);
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
	
	public Actionable<?> binding() {
		return binding(TH_NOMODIFIER_MASK, TH_NOBUTTON);
	}
	
	/**
	 * Returns the camera mouse-action associated to the given binding.
	 * 
	 * @param button
	 */
	public Actionable<?> binding(Integer button) {
		return binding(TH_NOMODIFIER_MASK, button);
	}
	
	/**
	 * Returns the camera mouse-action associated to the given binding.
	 * 
	 * @param mask
	 * @param button
	 */
	public Actionable<?> binding(Integer mask, Integer button) {
		return bindings.binding(new ButtonShortcut(mask, button));
	}
}