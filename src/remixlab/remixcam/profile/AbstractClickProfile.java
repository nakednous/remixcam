package remixlab.remixcam.profile;

import remixlab.remixcam.action.Actionable;
import remixlab.remixcam.shortcut.ClickShortcut;

public abstract class AbstractClickProfile<A extends Actionable> extends AbstractProfile<ClickShortcut, A> {
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
  		Actionable a = clickBinding(button);
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
  		Actionable a = clickBinding(button, nc);
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
  		Actionable a = clickBinding(mask, button, nc);
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
  public Actionable clickBinding(Integer button) {
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
  public Actionable clickBinding(Integer button, Integer nc) {
  	return bindings.binding(new ClickShortcut(button, nc));
  }

  /**
   * Returns the click-action associated to the given binding.
   * 
   * @param mask modifier mask defining the binding
   * @param button mouse button defining the binding
   * @param nc number of clicks defining the binding
   */
  public Actionable clickBinding(Integer mask, Integer button, Integer nc) {
  	return bindings.binding(new ClickShortcut(mask, button, nc));
  }
}
