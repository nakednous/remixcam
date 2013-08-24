/**
 *                  TerseHandling (version 0.70.0)
 *           Copyright (c) 2013 by Jean Pierre Charalambos
 *                 @author Jean Pierre Charalambos
 *             https://github.com/nakednous/tersehandling
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

package remixlab.tersehandling.generic.profile;

import remixlab.tersehandling.event.shortcut.Shortcut;

/**
 * Generic interface used to defined generic events. It simply attaches an
 * action to terse events.
 * 
 * @author pierre
 *
 * @param <A> Actionable set of actions that may be attached to the event.
 */
public interface Duoable <A extends Actionable<?>> {
	/**
	 * Action attached to an event.
	 */
	public Actionable<?> action();
	
	/**
	 * Attaches the given action to the event.
	 */
	public void setAction(Actionable<?> a);
	
	/**
	 * Interface to event shortcut.
	 */
	public Shortcut shortcut();
}
