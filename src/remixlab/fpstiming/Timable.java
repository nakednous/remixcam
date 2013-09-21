/*******************************************************************************
 * FPSTiming (version 0.9.50)
 * Copyright (c) 2013 Jean Pierre Charalambos.
 * @author Jean Pierre Charalambos
 * https://github.com/remixlab
 * 
 * All rights reserved. Library that eases the creation of interactive
 * scenes released under the terms of the GNU Public License v3.0
 * which available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package remixlab.fpstiming;

public interface Timable { 
	public void run(long period);
	public void run();
	public void stop();
	public void cancel();
	public void create();
	public boolean isActive();
	public long period();
	public void setPeriod(long period);
	public boolean isSingleShot();
	public void setSingleShot(boolean singleShot);
}
