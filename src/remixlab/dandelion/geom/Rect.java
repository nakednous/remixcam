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

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

/**
 * Rectangle class that provides a quick replacement for the java.awt.Rectangle.
 */
public class Rect {
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
				
		Rect other = (Rect) obj;
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
	protected int x;
	
	/**
	 * The Y coordinate of the upper-left corner of the Rectangle.
	 */
	protected int y;
	
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
	public Rect() {
		this(0, 0, 0, 0);
	}

	/**
	 * Copy constructor
	 * 
	 * @param r
	 *          the rectangle to be copied
	 */
	public Rect(Rect r) {
		this(r.x, r.y, r.width, r.height);
	}

	/**
	 * Constructs a new Rectangle whose upper-left corner is specified as (x,y)
	 * and whose width and height are specified by the arguments of the same name.
	 */
	public Rect(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public float x() {
		return x;
	}
	
	public float y() {
		return y;
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
