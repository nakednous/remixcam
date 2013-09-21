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
