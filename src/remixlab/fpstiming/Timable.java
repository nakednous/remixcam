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
 * Interface defining timers.
 */
public interface Timable {
	/**
	 * Runs the timer according to period. The timer may be scheduled
	 * for repeated fixed-rate execution according to {@link #isSingleShot()}.
	 * 
	 * @param period time in milliseconds between successive task executions
	 */
	public void run(long period);

	public void run();

	/**
	 * Stops the timer.
	 */
	public void stop();

	/**
	 * Stops the timer.
	 */
	public void cancel();

	/**
	 * Creates the timer.
	 */
	public void create();

	/**
	 * Tells whether or not the timer is active.
	 */
	public boolean isActive();

	/**
	 * Returns the timer period in milliseconds.
	 */
	public long period();

	/**
	 * Defines the timer period in milliseconds.
	 */
	public void setPeriod(long period);

	/**
	 * Returns whether or not the timer is scheduled to be executed only once.
	 */
	public boolean isSingleShot();

	/**
	 * Defines the timer as a single shot or for repeated execution.
	 */
	public void setSingleShot(boolean singleShot);
}
