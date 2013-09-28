/*******************************************************************************
 * FPSTiming (version 0.9.50)
 * Copyright (c) 2013 Jean Pierre Charalambos.
 * @author Jean Pierre Charalambos
 * https://github.com/remixlab
 *   
 * All rights reserved. Library that eases the creation of interactive
 * scenes released under the terms of the GNU Public License v3.0
 * which is available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package remixlab.fpstiming;

/**
 * Interface to define the timer callback method.
 */
public interface Taskable {
	/**
	 * Timer callback method
	 */
	public void execute();
}
