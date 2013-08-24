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

/**
 * Generic interface used to implement action sub-sets.
 * 
 * The interface expects to be parametarized with the global enum action set,
 * and then defining a one-to-one mapping among the local subset and the global set.
 * 
 * @author pierre
 *
 * @param <E> Global enum action set.
 */
public interface Actionable<E extends Enum<E>> {
	/**
	 * Returns the global action this action is mapped to.
	 */
	E referenceAction();
	
	/**
	 * Returns a description of the action.
	 */
	String description();
	
	/**
	 * Returns the degrees-of-freedom needed to perform the action.
	 */
	public int dofs();
}
