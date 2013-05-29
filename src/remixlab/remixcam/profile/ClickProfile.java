package remixlab.remixcam.profile;

import remixlab.remixcam.core.*;
//import remixlab.remixcam.core.*;
//import remixlab.remixcam.event.*;
import remixlab.remixcam.shortcut.*;

public abstract class ClickProfile extends AbstractProfile<ClickShortcut> {
	public ClickProfile(AbstractScene scn, String n) {
		super(scn, n);
	}

	/**
	@Override
	public DLEvent handle() {
		return new DLClickEvent(feedModifiers(), feedButton(), feedClickCount(),
				                    clickBinding(feedModifiers(), feedButton(), feedClickCount()));
	}
	*/
	
	/**
  public abstract Integer feedButton();
	
	public abstract Integer feedClickCount();
	*/

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
  public boolean isClickActionBound(DOF_0Action action) {
  	return bindings.isActionMapped(action.action());
  }
  
  /**
   * Binds the click-action to the given binding.
   * 
   * @param button binding
   * @param action action to be bound
   */
  public void setClickBinding(Integer button, DOF_0Action action) {
  	if ( isClickBindingInUse(button) ) {
  		DLAction a = clickBinding(button);
  		System.out.println("Warning: overwritting binding which was previously associated to " + a);
  	}
  	bindings.setBinding(new ClickShortcut(button), action.action());
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
  public void setClickBinding(Integer mask, Integer button, DOF_0Action action) {
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
  public void setClickBinding(Integer button, Integer nc, DOF_0Action action) {
  	if ( isClickBindingInUse(button, nc) ) {
  		DLAction a = clickBinding(button, nc);
  		System.out.println("Warning: overwritting binding which was previously associated to " + a);
  	}
  	bindings.setBinding(new ClickShortcut(button, nc), action.action());
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
  public void setClickBinding(Integer mask, Integer button, Integer nc, DOF_0Action action) {
  	if ( isClickBindingInUse(mask, button, nc) ) {
  		DLAction a = clickBinding(mask, button, nc);
  		System.out.println("Warning: overwritting binding which was previously associated to " + a);
  	}
  	bindings.setBinding(new ClickShortcut(mask, button, nc), action.action());
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
  public DLAction clickBinding(Integer button) {
  	return bindings.binding(new ClickShortcut(button));
  }

  /**
   * Returns the click-action associated to the given binding.
   * 
   * @param mask modifier mask defining the binding
   * @param button mouse button defining the binding
   */
  /**
  public DOF_0Action clickBinding(Integer mask, Integer button) {
          return bindings.binding(new ClickShortcut(mask, button));
  }
  */
  
  /**
   * Returns the click-action associated to the given binding.
   * 
   * @param button mouse button defining the binding
   * @param nc number of clicks defining the binding
   */
  public DLAction clickBinding(Integer button, Integer nc) {
  	return bindings.binding(new ClickShortcut(button, nc));
  }

  /**
   * Returns the click-action associated to the given binding.
   * 
   * @param mask modifier mask defining the binding
   * @param button mouse button defining the binding
   * @param nc number of clicks defining the binding
   */
  public DLAction clickBinding(Integer mask, Integer button, Integer nc) {
  	return bindings.binding(new ClickShortcut(mask, button, nc));
  }
}