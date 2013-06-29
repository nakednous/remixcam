/**
 *                     Dandelion (version 0.70.0)      
 *          Copyright (c) 2013 by Jean Pierre Charalambos
 *                 @author Jean Pierre Charalambos      
 *              https://github.com/nakednous/remixcam
 *                           
 * This library provides classes to ease the creation of interactive
 * frame-based, 2d and 3d scenes.
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

package remixlab.dandelion.geom;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

/**
 * Rectangle class that provides a quick replacement for the java.awt.Rectangle.
 */
public class Rectangle {
	@Override
	public int hashCode() {
    return new HashCodeBuilder(17, 37).    
    append(this.x).
    append(this.y).
    append(this.width).
    append(this.height).
    toHashCode();    
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;		
		if (obj.getClass() != getClass()) return false;
				
		Rectangle other = (Rectangle) obj;
		return new EqualsBuilder()		
		.append(this.x, other.x)
		.append(this.y, other.y)
		.append(this.width, other.width)
		.append(this.height, other.height)
		.isEquals();						
	}
	
	/**
	 * The X coordinate of the upper-left corner of the Rectangle.
	 */
	public int x;
	
	/**
	 * The Y coordinate of the upper-left corner of the Rectangle.
	 */
	public int y;
	
	/**
	 * The width of the Rectangle.
	 */
	public int width;
	
	/**
	 * The height of the Rectangle.
	 */
	public int height;

	/**
	 * Constructs a new Rectangle whose upper-left corner is at (0, 0) in the
	 * coordinate space, and whose width and height are both zero.
	 */
	public Rectangle() {
		this(0, 0, 0, 0);
	}

	/**
	 * Copy constructor
	 * 
	 * @param r
	 *          the rectangle to be copied
	 */
	public Rectangle(Rectangle r) {
		this(r.x, r.y, r.width, r.height);
	}

	/**
	 * Constructs a new Rectangle whose upper-left corner is specified as (x,y)
	 * and whose width and height are specified by the arguments of the same name.
	 */
	public Rectangle(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	/**
	 * Returns the X coordinate of the center of the rectangle.
	 */
	public float getCenterX() {
		return (float) x + ((float) width / 2);
	}
	
	/**
	 * Returns the Y coordinate of the center of the rectangle.
	 */
	public float getCenterY() {
		return (float) y + ((float) height / 2);
	}
}
