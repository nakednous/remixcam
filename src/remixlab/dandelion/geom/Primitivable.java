/*******************************************************************************
 * dandelion (version 1.0.0-alpha.1)
 * Copyright (c) 2013 National University of Colombia, https://github.com/remixlab
 * @author Jean Pierre Charalambos, http://otrolado.info/
 *     
 * All rights reserved. Library that eases the creation of interactive
 * scenes, released under the terms of the GNU Public License v3.0
 * which is available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package remixlab.dandelion.geom;

import remixlab.tersehandling.core.Copyable;

public interface Primitivable extends Copyable {
	public void link(float [] src);
	public void unLink();
	@Override
	public Primitivable get();
	public float [] get(float [] target);
	public void set(Primitivable source);
	public void set(float [] source);	
	public void reset();
}
