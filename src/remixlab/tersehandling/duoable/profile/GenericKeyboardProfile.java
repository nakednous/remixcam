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

import remixlab.tersehandling.event.KeyboardEvent;
import remixlab.tersehandling.shortcut.KeyboardShortcut;

public class GenericKeyboardProfile<A extends Actionable<?>>  extends GenericProfile<KeyboardShortcut, A> {
	///**
	public Actionable<?> handleKey(KeyDuoable<?> event) {
	//public void handleKey(DLKeyEvent e) {
		if(event != null)
			return binding(event.keyShortcut());
		return null;
	}
	// */

	/**
	 * Defines a camera keyboard shortcut to bind the given action.
	 * 
	 * @param key shortcut
	 * @param action action to be bound
	 */
	public void setShortcut(Character key, A action) {
		if ( isKeyInUse(key) ) {
			Actionable<?> a = shortcut(key);
			System.out.println("Warning: overwritting shortcut which was previously bound to " + a);
		}
		bindings.setBinding(new KeyboardShortcut(key), action);
	}
	
	/**
	 * Defines a camera keyboard shortcut to bind the given action.
	 * <p>
	 * High-level version of {@link #setShortcut(Integer, Integer, Scene.A)}.
	 * 
	 * @param mask modifier mask defining the shortcut
	 * @param key character (internally converted to a key coded) defining the shortcut
	 * @param action action to be bound
	 * 
	 * @see #setShortcut(Integer, Integer, Scene.A)
	 */
	public void setShortcut(Integer mask, Character key, A action) {
		setShortcut(mask, KeyboardEvent.getKeyCode(key), action);
	}
	
	/**
	 * Defines a camera keyboard shortcut to bind the given action.
	 * <p>
	 * Low-level version of {@link #setShortcut(Integer, Character, Scene.A)}.
	 * 
	 * @param mask modifier mask defining the shortcut
	 * @param vKey coded key defining the shortcut
	 * @param action action to be bound
	 * 
	 * @see #setShortcut(Integer, Character, Scene.A)
	 */
	public void setShortcut(Integer mask, Integer vKey, A action) {
		if ( isKeyInUse(mask, vKey) ) {
			Actionable<?> a = shortcut(mask, vKey);
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
	public void setShortcut(Integer vKey, A action) {
		if ( isKeyInUse(vKey) ) {
			Actionable<?> a = shortcut(vKey);
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
		removeShortcut(mask, KeyboardEvent.getKeyCode(key));
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
	public Actionable<?> shortcut(Character key) {
		return bindings.binding(new KeyboardShortcut(key));
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
	public Actionable<?> shortcut(Integer mask, Integer vKey) {
		return bindings.binding(new KeyboardShortcut(mask, vKey));
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
	public Actionable<?> shortcut(Integer mask, Character key) {
		return shortcut(mask, KeyboardEvent.getKeyCode(key));
	}

	/**
	 * Returns the action that is bound to the given camera keyboard shortcut.
	 * 
	 * @param vKey coded key (such PApplet.UP) that defines the shortcut
	 * @return action
	 */
	public Actionable<?> shortcut(Integer vKey) {
		return bindings.binding(new KeyboardShortcut(vKey));
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
		return isKeyInUse(mask, KeyboardEvent.getKeyCode(key));
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
	public boolean isKeyboardActionBound(A action) {
		return bindings.isActionMapped(action);
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
