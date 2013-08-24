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

package remixlab.tersehandling.core;

import remixlab.tersehandling.event.*;

/**
 * Grabbers are means to attach a set of actions to application objects.
 * Grabbers are attached to Agents through their API, and may be attached
 * to more than just a single Agent.
 * <p>
 * Each application object willing to subscribe a set of actions should
 * either implement the Grabbable interface or extend from the AbstractGrabber
 * class (which provides a default implementation of that interface easing the
 * implementation), and override the following two methods:
 * {@link #checkIfGrabsInput(TerseEvent)}, which defines the rules to set the
 * application object as an input grabber; and,
 * {@link #performInteraction(TerseEvent)}, which defines how the application
 * object should behave according to a given TerseEvent, which may be
 * parameterized to hold a user-defined action.
 * 
 * @author pierre
 */
public interface Grabbable {
	/**
	 * Defines the rules to set the application object as an input grabber.
	 */
	boolean checkIfGrabsInput(TerseEvent event);
	
	/**
	 * Defines how the application object should behave according to a given
	 * TerseEvent, which may be parameterized to hold a user-defined action.
	 */
	void performInteraction(TerseEvent event);
	
	/**
	 * Check if this object grabs agent. Should return {@code true} if this object
	 * grabs the agent and {@code false} otherwise.
	 */
	boolean grabsAgent(Agent agent);
}
