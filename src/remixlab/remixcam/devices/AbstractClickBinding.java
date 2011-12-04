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

package remixlab.remixcam.devices;

import remixlab.remixcam.core.*;

/**
 * This class represents mouse click shortcuts.
 * <p>
 * Mouse click shortcuts are defined with a specific number of clicks
 * and can be of one out of two forms: 1. A mouse button; and, 2. A mouse
 * button plus a key-modifier (such as the CTRL key).
 */
public abstract class AbstractClickBinding {		
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((button == null) ? 0 : button.hashCode());
		result = prime * result + ((mask == null) ? 0 : mask.hashCode());
		result = prime * result
				+ ((numberOfClicks == null) ? 0 : numberOfClicks.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractClickBinding other = (AbstractClickBinding) obj;
		if (button == null) {
			if (other.button != null)
				return false;
		} else if (!button.equals(other.button))
			return false;
		if (mask == null) {
			if (other.mask != null)
				return false;
		} else if (!mask.equals(other.mask))
			return false;
		if (numberOfClicks == null) {
			if (other.numberOfClicks != null)
				return false;
		} else if (!numberOfClicks.equals(other.numberOfClicks))
			return false;
		return true;
	}

	protected final Integer mask;
	protected final Integer numberOfClicks;
	protected final AbstractScene.Button button;

	/**
	 * Defines a mouse single click shortcut from the given mouse button. 
	 * 
	 * @param b mouse button
	 */
	public AbstractClickBinding(AbstractScene.Button b) {
		this(0, b, 1);
	}
	
	/**
	 * Defines a mouse single click shortcut from the given mouse button
	 * and modifier mask.
	 * 	
	 * @param m modifier mask
	 * @param b mouse button
	 */
	public AbstractClickBinding(Integer m, AbstractScene.Button b) {
		this(m, b, 1);
	}
	
	/**
	 * Defines a mouse click shortcut from the given mouse button and
	 * number of clicks. 
	 * 
	 * @param b mouse button
	 * @param c number of clicks
	 */
	public AbstractClickBinding(AbstractScene.Button b, Integer c) {
		this(0, b, c);
	}
	
	/**
	 * Defines a mouse click shortcut from the given mouse button,
	 * modifier mask, and number of clicks.
	 * 
	 * @param m modifier mask
	 * @param b mouse button
	 * @param c bumber of clicks
	 */
	public AbstractClickBinding(Integer m, AbstractScene.Button b, Integer c) {
		this.mask = m;
		this.button = b;
		if(c <= 0)
			this.numberOfClicks = 1;
		else
			this.numberOfClicks = c;
	}
	
	public abstract String description();
}