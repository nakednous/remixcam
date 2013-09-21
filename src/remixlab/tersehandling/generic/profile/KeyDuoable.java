/*******************************************************************************
 * TerseHandling (version 0.9.50)
 * Copyright (c) 2013 Jean Pierre Charalambos.
 * @author Jean Pierre Charalambos
 * https://github.com/remixlab
 * 
 * All rights reserved. Library that eases the creation of interactive
 * scenes released under the terms of the GNU Public License v3.0
 * which available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package remixlab.tersehandling.generic.profile;

import remixlab.tersehandling.event.shortcut.KeyboardShortcut;

/**
 * A specialized Duoable interface to deal with keyboard events.
 * 
 * @author pierre
 *
 * @param <A> Actionable set of actions that may be attached to the keyboard event.
 */
public interface KeyDuoable<A extends Actionable<?>> extends Duoable<A> {
	/**
	 * Interface to keyboard event shortcut.
	 */
	public KeyboardShortcut keyShortcut();
}
