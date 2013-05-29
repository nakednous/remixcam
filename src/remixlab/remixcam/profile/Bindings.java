/**
 *                     RemixCam (version 1.0.0)      
 *      Copyright (c) 2012 by National University of Colombia
 *                 @author Jean Pierre Charalambos      
 *              https://github.com/nakednous/remixcam
 *                           
 * This java library provides classes to ease the creation of interactive 3D
 * scenes in various languages and frameworks such as JOGL, WebGL and Processing.
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

package remixlab.remixcam.profile;

import java.util.HashMap;
import java.util.Map.Entry;

import remixlab.remixcam.core.*;
import remixlab.remixcam.shortcut.Shortcut;

/**
 * A parameterized template class used to define shortcut bindings. This is
 * an internal class that should not be instantiated. 
 * <p>
 * Shortcuts are mapped to keyboard and mouse actions in different places:
 * 1. Scene global keyboard actions; 2. CameraProfile keyboard actions
 * and mouse (and mouse-click) actions.
 * <p>
 * Internally, this class is simply a parameterized hash-map wrap
 * (HashMap<K, DLAction>). 
 */
public class Bindings<K extends Shortcut> implements Constants {
	protected HashMap<K, DLAction> map;

	public Bindings() {
		map = new HashMap<K, DLAction>();
	}
	
	/**
	 * Returns the {@code map} (which is simply an instance of {@code HashMap})
	 * encapsulated by this object.
	 */
	public HashMap<K, DLAction> map() {
		return map;
	}
	
	public DLAction binding(Shortcut key) {
		return map.get(key);
	}

	/**
	 * Returns the action associated to a given Keyboard shortcut {@code key}.
	 */
	/**
	public DLAction binding(K key) {
		return map.get(key);
	}
	*/
	
	/**
	 * Defines the shortcut that triggers a given action.
	 * 
	 * @param key shortcut.
	 * @param action action.
	 */
	public void setBinding(K key, DLAction action) {
		map.put(key, action);
	}
	
	/**
	 * Removes the shortcut binding.
	 * 
	 * @param key shortcut
	 */
	public void removeBinding(K key) {
		map.remove(key);
	}
	
	/**
	 * Removes all the shortcuts from this object.
	 */
	public void removeAllBindings() {
		map.clear();
	}

	/**
	 * Returns true if this object contains a binding for the specified shortcut.
	 * 
	 * @param key shortcut
	 * @return true if this object contains a binding for the specified shortcut.
	 */
	public boolean isShortcutInUse(K key) {
		return map.containsKey(key);
	}

	/**
	 * Returns true if this object maps one or more shortcuts to the specified action.
	 * 
	 * @param action action whose presence in this object is to be tested
	 * @return true if this object maps one or more shortcuts to the specified action.
	 */
	public boolean isActionMapped(DLAction action) {
		return map.containsValue(action);
	}
	
	public String description() {
		String result = new String();
		for (Entry<K, DLAction> entry : map.entrySet())
			result += entry.getKey().description() + " -> " + entry.getValue().description() + "\n"; 
		return result;
	}
}