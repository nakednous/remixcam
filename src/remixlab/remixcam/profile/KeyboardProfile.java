package remixlab.remixcam.profile;

import java.util.Map;

import remixlab.proscene.Scene;
import remixlab.remixcam.core.*;
import remixlab.remixcam.event.*;
import remixlab.remixcam.shortcut.*;

public class KeyboardProfile extends AbstractProfile<KeyboardShortcut, Constants.DOF_0Action> implements Constants {
	public KeyboardProfile() {
		super();
	}
	
	protected KeyboardProfile(KeyboardProfile other) {
		bindings = new Bindings<KeyboardShortcut, DOF_0Action>();    
    for (Map.Entry<KeyboardShortcut, DOF_0Action> entry : other.bindings.map.entrySet()) {
    	KeyboardShortcut key = entry.getKey().get();
    	DOF_0Action value = entry.getValue();
    	bindings.setBinding(key, value);
    }
	}
	
  @Override
	public KeyboardProfile get() {
		return new KeyboardProfile(this);
	}
  
  /**
	public KeyboardProfile(AbstractScene scn, String n) {
		super(scn, n);		
	}
	*/
	
	@Override
	public void setDefaultBindings() {
		// D e f a u l t s h o r t c u t s		
		setShortcut('a', DOF_0Action.DRAW_AXIS);
		setShortcut('g', DOF_0Action.DRAW_GRID);
		setShortcut(' ', DOF_0Action.CAMERA_PROFILE);
		setShortcut('e', DOF_0Action.CAMERA_TYPE);		
		setShortcut('h', DOF_0Action.GLOBAL_HELP);
		setShortcut('H', DOF_0Action.CURRENT_CAMERA_PROFILE_HELP);
		setShortcut('r', DOF_0Action.EDIT_CAMERA_PATH);

		setShortcut('s', DOF_0Action.INTERPOLATE_TO_FIT_SCENE);
		setShortcut('S', DOF_0Action.SHOW_ALL);
		
		setShortcut(DLKeyEvent.RIGHT, DOF_0Action.MOVE_CAMERA_RIGHT);		
	  setShortcut(DLKeyEvent.LEFT, DOF_0Action.MOVE_CAMERA_LEFT);
		setShortcut(DLKeyEvent.UP, DOF_0Action.MOVE_CAMERA_UP);
		setShortcut(DLKeyEvent.DOWN, DOF_0Action.MOVE_CAMERA_DOWN);
		
		setShortcut((DLKeyEvent.ALT | DLKeyEvent.SHIFT), 'l', DOF_0Action.MOVE_CAMERA_LEFT);

		/**
		// K e y f r a m e s s h o r t c u t k e y s
		setAddKeyFrameKeyboardModifier(Event.CTRL);
		setDeleteKeyFrameKeyboardModifier(Event.ALT);
		setPathKey('1', 1);
		setPathKey('2', 2);
		setPathKey('3', 3);
		setPathKey('4', 4);
		setPathKey('5', 5);
		*/
	}
	
	/**
	@Override
	public void handle(DLEvent<DOF_0Action> e) {
		//super.handle((DLKeyEvent)e);
		super.handle(e);
	}
	*/
	
	public void handleKey(DLEvent<?> e) {
	//public void handleKey(DLKeyEvent e) {
		if(e != null)
			e.setAction( binding(((DLKeyEvent)e).keyShortcut()) );
	}
	
	/**
	 * Overload this method to define the z-axis translation feed this method
	 * if you plan to implement your own HIDevice. Otherwise use {@link #feedZTranslation(float)}
	 * and {@link #addHandler(Object, String)} to the HIDevice.
	 */
	
	/**
	public abstract Character feedKey();
	
	public abstract Integer feedKeyCode();
	*/
	
	// ---

	/**
	 * Defines a camera keyboard shortcut to bind the given action.
	 * 
	 * @param key shortcut
	 * @param action action to be bound
	 */
	public void setShortcut(Character key, DOF_0Action action) {
		if ( isKeyInUse(key) ) {
			DLAction a = shortcut(key);
			System.out.println("Warning: overwritting shortcut which was previously bound to " + a);
		}
		bindings.setBinding(new KeyboardShortcut(key), action);
	}
	
	/**
	 * Defines a camera keyboard shortcut to bind the given action.
	 * <p>
	 * High-level version of {@link #setShortcut(Integer, Integer, Scene.DOF_0Action)}.
	 * 
	 * @param mask modifier mask defining the shortcut
	 * @param key character (internally converted to a key coded) defining the shortcut
	 * @param action action to be bound
	 * 
	 * @see #setShortcut(Integer, Integer, Scene.DOF_0Action)
	 */
	public void setShortcut(Integer mask, Character key, DOF_0Action action) {
		setShortcut(mask, DLKeyEvent.getKeyCode(key), action);
	}
	
	/**
	 * Defines a camera keyboard shortcut to bind the given action.
	 * <p>
	 * Low-level version of {@link #setShortcut(Integer, Character, Scene.DOF_0Action)}.
	 * 
	 * @param mask modifier mask defining the shortcut
	 * @param vKey coded key defining the shortcut
	 * @param action action to be bound
	 * 
	 * @see #setShortcut(Integer, Character, Scene.DOF_0Action)
	 */
	public void setShortcut(Integer mask, Integer vKey, DOF_0Action action) {
		if ( isKeyInUse(mask, vKey) ) {
			DLAction a = shortcut(mask, vKey);
			System.out.println("Warning: overwritting shortcut which was previously bound to " + a);
		}
		bindings.setBinding(new KeyboardShortcut(mask, vKey), action);
	}
	
	/**
	 * Defines a camera keyboard shortcut to bind the given action.
	 * 
	 * @param vKey coded key (such PApplet.UP) that defines the shortcut
	 * @param action action to be bound
	 */
	public void setShortcut(Integer vKey, DOF_0Action action) {
		if ( isKeyInUse(vKey) ) {
			DLAction a = shortcut(vKey);
			System.out.println("Warning: overwritting shortcut which was previously bound to " + a);
		}
		bindings.setBinding(new KeyboardShortcut(vKey), action);
	}
	
	/**
	 * Removes the camera keyboard shortcut.
	 * 
	 * @param key shortcut
	 */
	public void removeShortcut(Character key) {
		bindings.removeBinding(new KeyboardShortcut(key));
	}
	
	/**
	 * Removes the camera keyboard shortcut.
	 * <p>
	 * High-level version of {@link #removeShortcut(Integer, Integer)}.
	 * 
	 * @param mask modifier mask that defining the shortcut
	 * @param key character (internally converted to a key coded) defining the shortcut
	 * 
	 * @see #removeShortcut(Integer, Integer)
	 */
	public void removeShortcut(Integer mask, Character key) {
		removeShortcut(mask, DLKeyEvent.getKeyCode(key));
	}
	
	/**
	 * Removes the camera keyboard shortcut.
	 * <p>
	 * low-level version of {@link #removeShortcut(Integer, Character)}.
	 * 
	 * @param mask modifier mask that defining the shortcut
	 * @param vKey coded key defining the shortcut
	 * 
	 * @see #removeShortcut(Integer, Character)
	 */
	public void removeShortcut(Integer mask, Integer vKey) {
		bindings.removeBinding(new KeyboardShortcut(mask, vKey));
	}

	/**
	 * Removes the camera keyboard shortcut.
	 * 
	 * @param vKey coded key (such PApplet.UP) that defines the shortcut
	 */
	public void removeShortcut(Integer vKey) {
		bindings.removeBinding(new KeyboardShortcut(vKey));
	}

	/**
	 * Returns the action that is bound to the given camera keyboard shortcut.
	 * 
	 * @param key shortcut
	 * @return action
	 */
	public DLAction shortcut(Character key) {
		return bindings.binding(new KeyboardShortcut(key)).action();
	}

	/**
   * Returns the action that is bound to the given camera keyboard shortcut.
   * <p>
   * Low-level version of {@link #shortcut(Integer, Character)}
   * 
   * @param mask modifier mask defining the shortcut
	 * @param vKey coded key defining the shortcut
   * @return action
   * 
   * @see #shortcut(Integer, Character)
   */
	public DLAction shortcut(Integer mask, Integer vKey) {
		return bindings.binding(new KeyboardShortcut(mask, vKey)).action();
	}
	
	/**
   * Returns the action that is bound to the given camera keyboard shortcut.
   * <p>
   * High-level version of {@link #shortcut(Integer, Integer)}
   * 
   * @param mask modifier mask defining the shortcut
	 * @param key character (internally converted to a coded key) defining the shortcut
   * @return action
   * 
   * @see #shortcut(Integer, Integer)
   */
	public DLAction shortcut(Integer mask, Character key) {
		return shortcut(mask, DLKeyEvent.getKeyCode(key));
	}

	/**
	 * Returns the action that is bound to the given camera keyboard shortcut.
	 * 
	 * @param vKey coded key (such PApplet.UP) that defines the shortcut
	 * @return action
	 */
	public DLAction shortcut(Integer vKey) {
		return bindings.binding(new KeyboardShortcut(vKey)).action();
	}

	/**
	 * Returns true if the given camera keyboard shortcut binds an action.
	 * 
	 * @param key shortcut
	 */
	public boolean isKeyInUse(Character key) {
		return bindings.isShortcutInUse(new KeyboardShortcut(key));
	}
	
	/**
	 * Returns true if the given camera keyboard shortcut binds an action.
	 * <p>
	 * High-level version of {@link #isKeyInUse(Integer, Integer)}.
   * 
   * @param mask modifier mask defining the shortcut
	 * @param key character (internally converted to a coded key) defining the shortcut
	 * 
	 * @see #isKeyInUse(Integer, Integer)
	 */
	public boolean isKeyInUse(Integer mask, Character key) {
		return isKeyInUse(mask, DLKeyEvent.getKeyCode(key));
	}
	
	/**
	 * Returns true if the given camera keyboard shortcut binds an action.
	 * <p>
	 * Low-level version of {@link #isKeyInUse(Integer, Character)}.
	 * 
	 * @param mask modifier mask defining the shortcut
	 * @param vKey coded key defining the shortcut
	 * 
	 * @see #isKeyInUse(Integer, Character)
	 */
	public boolean isKeyInUse(Integer mask, Integer vKey) {
		return bindings.isShortcutInUse(new KeyboardShortcut(mask, vKey));
	}
	
	/**
	 * Returns true if the given camera keyboard shortcut binds an action.
	 * 
	 * @param vKey coded key (such PApplet.UP) that defines the shortcut
	 */
	public boolean isKeyInUse(Integer vKey) {
		return bindings.isShortcutInUse(new KeyboardShortcut(vKey));
	}

	/**
	 * Returns true if there is a camera keyboard shortcut for the given action.
	 */
	public boolean isKeyboardActionBound(DOF_0Action action) {
		return bindings.isActionMapped(action.action());
	}
	
	// ---- pending
	//TODO: hint here is to use the int represented by this char, i.e., Character.getNumericValue('1');
	
	//TODO these two should belong elsewhere guess where?!	
  //K E Y F R A M E S
	/**
	protected Bindings<Integer, Integer> pathKeys;
	public Integer addKeyFrameKeyboardModifier;
	public Integer deleteKeyFrameKeyboardModifier;
	*/
	
	/**
	 * Associates key-frame interpolator path to key. High-level version
	 * of {@link #setPathKey(Integer, Integer)}.
	 *  
	 * @param key character (internally converted to a key coded) defining the shortcut
	 * @param path key-frame interpolator path
	 * 
	 * @see #setPathKey(Integer, Integer)
	 */
	/**
	public void setPathKey(Character key, Integer path) {
		setPathKey(DLKeyEvent.getKeyCode(key), path);
	}
	*/
	
	/**
	 * Associates key-frame interpolator path to the given virtual key. Low-level version
	 * of {@link #setPathKey(Character, Integer)}.
	 * 
	 * @param vKey shortcut
	 * @param path key-frame interpolator path
	 * 
	 * @see #setPathKey(Character, Integer)
	 */
	/**
	public void setPathKey(Integer vKey, Integer path) {
		if ( isPathKeyInUse(vKey) ) {
			Integer p = path(vKey);
			System.out.println("Warning: overwritting path key which was previously bound to path " + p);
		}
		pathKeys.setBinding(vKey, path);
	}
	*/

	/**
	 * Returns the key-frame interpolator path associated with key. High-level version
	 * of {@link #path(Integer)}.
	 * 
	 * @param key character (internally converted to a key coded) defining the shortcut
	 * 
	 * @see #path(Integer)
	 */
	/**
	public Integer path(Character key) {
		return path(DLKeyEvent.getKeyCode(key));
	}
	*/
	
	/**
	 * Returns the key-frame interpolator path associated with key. Low-level version
	 * of {@link #path(Character)}.
	 * 
	 * @param vKey shortcut
	 * 
	 * @see #path(Character)
	 */
	/**
	public Integer path(Integer vKey) {
		return pathKeys.binding(vKey);
	}
	*/

	/**
	 * Removes the key-frame interpolator shortcut. High-level version
	 * of {@link #removePathKey(Integer)}.
	 * 
	 * @param key character (internally converted to a key coded) defining the shortcut
	 * 
	 * @see #removePathKey(Integer)
	 */
	/**
	public void removePathKey(Character key) {
		removePathKey(DLKeyEvent.getKeyCode(key));
	}
	*/
	
	/**
	 * Removes the key-frame interpolator shortcut. Low-level version
	 * of {@link #removePathKey(Character)}.
	 * 
	 * @param vKey shortcut
	 * 
	 * @see #removePathKey(Character)
	 */
	/**
	public void removePathKey(Integer vKey) {
		pathKeys.removeBinding(vKey);
	}
	*/
	
	/**
	 * Returns true if the given key binds a key-frame interpolator path. High-level version
	 * of {@link #isPathKeyInUse(Integer)}.
	 * 
	 * @param key character (internally converted to a key coded) defining the shortcut
	 * 
	 * @see #isPathKeyInUse(Integer)
	 */
	/**
	public boolean isPathKeyInUse(Character key) {
		return isPathKeyInUse(DLKeyEvent.getKeyCode(key));
	}
	*/
	
	/**
	 * Returns true if the given virtual key binds a key-frame interpolator path. Low-level version
	 * of {@link #isPathKeyInUse(Character)}.
	 * 
	 * @param vKey shortcut
	 * 
	 * @see #isPathKeyInUse(Character)
	 */
	/**
	public boolean isPathKeyInUse(Integer vKey) {
		return pathKeys.isShortcutInUse(vKey);
	}
	*/

	/**
	 * Sets the modifier key needed to play the key-frame interpolator paths.
	 * 
	 * @param modifier
	 */
	/**
	public void setAddKeyFrameKeyboardModifier(Integer modifier) {
		addKeyFrameKeyboardModifier = modifier;
	}
	*/

	/**
	 * Sets the modifier key needed to delete the key-frame interpolator paths.
	 * 
	 * @param modifier
	 */
	/**
	public void setDeleteKeyFrameKeyboardModifier(Integer modifier) {
		deleteKeyFrameKeyboardModifier = modifier;
	}
	*/
}
