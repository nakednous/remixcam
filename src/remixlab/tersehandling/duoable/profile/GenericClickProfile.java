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

import remixlab.tersehandling.shortcut.ClickShortcut;

public class GenericClickProfile<A extends Actionable<?>> extends GenericProfile<ClickShortcut, A> {
  /**
   * Returns true if the given binding binds a click-action.
   *      
   * @param button binding
   */
  public boolean isClickBindingInUse(Integer button) {
  	return bindings.isShortcutInUse(new ClickShortcut(button));
  }
  
  /**
   * Returns true if the given binding binds a click-action.
   * 
   * @param mask modifier mask defining the binding
   * @param button button defining the binding
   */
  /**
  public boolean isClickBindingInUse(Integer mask, Integer button) {
          return clickActions.isShortcutInUse(new ClickBinding(mask, button));
  }
  */
  
  /**
   * Returns true if the given binding binds a click-action.
   * 
   * @param button button defining the binding
   * @param nc number of clicks defining the binding
   */
  public boolean isClickBindingInUse(Integer button, Integer nc) {
  	return bindings.isShortcutInUse(new ClickShortcut(button, nc));
  }

  /**
   * Returns true if the given binding binds a click-action.
   * 
   * @param mask modifier mask defining the binding
   * @param button button defining the binding
   * @param nc number of clicks defining the binding
   */
  public boolean isClickBindingInUse(Integer mask, Integer button, Integer nc) {
  	return bindings.isShortcutInUse(new ClickShortcut(mask, button, nc));
  }

  /** 
   * Returns true if the given click-action is bound.
   */
  public boolean isClickActionBound(A action) {
  	return bindings.isActionMapped(action);
  }
  
  /**
   * Binds the click-action to the given binding.
   * 
   * @param button binding
   * @param action action to be bound
   */
  public void setClickBinding(Integer button, A action) {
  	if ( isClickBindingInUse(button) ) {
  		Actionable<?> a = clickBinding(button);
  		System.out.println("Warning: overwritting binding which was previously associated to " + a);
  	}
  	bindings.setBinding(new ClickShortcut(button), action);
  }

  /**
   * Binds the click-action to the given binding.
   * 
   * @param mask modifier mask defining the binding
   * @param button mouse button defining the binding
   * @param action action to be bound
   * 
   * <b>Attention:</b> Mac users should avoid using the CTRL modifier key, since its use is
   * reserved to emulate the right button of the mouse.
   */
  /**
  public void setClickBinding(Integer mask, Integer button, A action) {
          if ( isClickBindingInUse(mask, button) ) {
                  ClickAction a = clickBinding(mask, button);
                  System.out.println("Warning: overwritting bindings which was previously associated to " + a);
          }
          bindings.setBinding(new ClickShortcut(mask, button), action);
  }
  */
  
  /**
   * Binds the click-action to the given binding.
   * 
   * @param button mouse button defining the binding
   * @param nc number of clicks that defines the binding
   * @param action action to be bound
   */
  public void setClickBinding(Integer button, Integer nc, A action) {
  	if ( isClickBindingInUse(button, nc) ) {
  		Actionable<?> a = clickBinding(button, nc);
  		System.out.println("Warning: overwritting binding which was previously associated to " + a);
  	}
  	bindings.setBinding(new ClickShortcut(button, nc), action);
  }

  /**
   * Binds the click-action to the given binding.
   * 
   * @param mask modifier mask defining the binding
   * @param button mouse button defining the binding
   * @param nc number of clicks that defines the binding
   * @param action action to be bound
   * 
   * <b>Attention:</b> Mac users should avoid using the CTRL modifier key, since its use is
   * reserved to emulate the right button of the mouse.
   */
  public void setClickBinding(Integer mask, Integer button, Integer nc, A action) {
  	if ( isClickBindingInUse(mask, button, nc) ) {
  		Actionable<?> a = clickBinding(mask, button, nc);
  		System.out.println("Warning: overwritting binding which was previously associated to " + a);
  	}
  	bindings.setBinding(new ClickShortcut(mask, button, nc), action);
  }
  
  /**
   * Removes the mouse-click binding.
   * 
   * @param button binding
   */
  public void removeClickBinding(Integer button) {
  	bindings.removeBinding(new ClickShortcut(button));
  }

  /**
   * Removes the mouse-click binding.
   * 
   * @param mask modifier mask defining the binding
   * @param button mouse button defining the binding
   */
  /**
  public void removeClickBinding(Integer mask, Integer button) {
          bindings.removeBinding(new ClickShortcut(mask, button));
  }
  */
  
  /**
   * Removes the mouse-click binding.
   * 
   * @param button mouse button defining the binding
   * @param nc number of clicks defining the binding
   */
  public void removeClickBinding(Integer button, Integer nc) {
  	bindings.removeBinding(new ClickShortcut(button, nc));
  }
  
  /**
   * Removes the mouse-click binding.
   * 
   * @param mask modifier mask defining the binding
   * @param button mouse button defining the binding
   * @param nc number of clicks defining the binding
   */
  public void removeClickBinding(Integer mask, Integer button, Integer nc) {
  	bindings.removeBinding(new ClickShortcut(mask, button, nc));
  }
  
  /**
   * Returns the click-action associated to the given binding.
   * 
   * @param button binding
   */
  public Actionable<?> clickBinding(Integer button) {
  	return bindings.binding(new ClickShortcut(button));
  }

  /**
   * Returns the click-action associated to the given binding.
   * 
   * @param mask modifier mask defining the binding
   * @param button mouse button defining the binding
   */
  /**
  public A clickBinding(Integer mask, Integer button) {
          return bindings.binding(new ClickShortcut(mask, button));
  }
  */
  
  /**
   * Returns the click-action associated to the given binding.
   * 
   * @param button mouse button defining the binding
   * @param nc number of clicks defining the binding
   */
  public Actionable<?> clickBinding(Integer button, Integer nc) {
  	return bindings.binding(new ClickShortcut(button, nc));
  }

  /**
   * Returns the click-action associated to the given binding.
   * 
   * @param mask modifier mask defining the binding
   * @param button mouse button defining the binding
   * @param nc number of clicks defining the binding
   */
  public Actionable<?> clickBinding(Integer mask, Integer button, Integer nc) {
  	return bindings.binding(new ClickShortcut(mask, button, nc));
  }
}
