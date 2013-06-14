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

package remixlab.remixcam.geom;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

import remixlab.remixcam.core.Constants;

/**
 * A class to describe a two or three dimensional vector.
 * <p>
 * The result of all functions are applied to the vector itself, with the
 * exception of cross(), which returns a new PVector (or writes to a specified
 * 'target' PVector). That is, add() will add the contents of one vector to
 * this one. Using add() with additional parameters allows you to put the
 * result into a new PVector. Functions that act on multiple vectors also
 * include static versions. Because creating new objects can be computationally
 * expensive, most functions include an optional 'target' PVector, so that a
 * new PVector object is not created with each operation.
 * <p>
 * Initially based on the Vector3D class by <a href="http://www.shiffman.net">Dan Shiffman</a>.
 * <p>
 * This class has been almost entirely taken from Processing.
 * 
 * @author pierre
 */
public class DLVector implements Constants, Primitivable {
	@Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).		
		append(this.vec[0]).
		append(this.vec[1]).
		append(this.vec[2]).
    toHashCode();
  }
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;		
		if (obj.getClass() != getClass()) return false;
				
		DLVector other = (DLVector) obj;		
	   return new EqualsBuilder()    
    .append(this.vec[0], other.vec[0])
    .append(this.vec[1], other.vec[1])
    .append(this.vec[2], other.vec[2])
		.isEquals();
	} 
  
  /**
	 * The x, y and z coordinates of the Vector3D.
	 */
	public float vec[] = new float[3];
  

  /**
   * Constructor for an empty vector: x, y, and z are set to 0.
   */
  public DLVector() {  	
  	reset();
  }
  
  /**
   * Constructor for a 3D vector.
   *
   * @param  x the x coordinate.
   * @param  y the y coordinate.
   * @param  z the y coordinate.
   */
  public DLVector(float x, float y, float z) {
    this.vec[0] = x;
    this.vec[1] = y;
    this.vec[2] = z;
  }
  
  public DLVector(DLVector other) {
  	set(other);
  }

  /**
   * Constructor for a 2D vector: z coordinate is set to 0.
   *
   * @param  x the x coordinate.
   * @param  y the y coordinate.
   */
  public DLVector(float x, float y) {
    this.vec[0] = x;
    this.vec[1] = y;
    this.vec[2] = 0;
  }
  
  public float x() {
  	return this.vec[0];
  }
  
  public float y() {
		return this.vec[1];
	}
	
	public float z() {
		return this.vec[2];
	}	
	
	public float x(float x) {
		return this.vec[0] = x;
	}
	
	public float y(float y) {
		return this.vec[1] = y;
	}
	
	public float z(float z) {
		return this.vec[2] = z;
	}  
  
  public DLVector projectVectorOnAxis(DLVector direction) {
  	return projectVectorOnAxis(this, direction);
  }
  
  public static DLVector projectVectorOnAxis(DLVector src, DLVector direction) {
		float directionSquaredNorm = squaredNorm(direction);
		if (Geom.zero(directionSquaredNorm))
			throw new RuntimeException("Direction squared norm is nearly 0");

		float modulation = src.dot(direction) / directionSquaredNorm;
		return DLVector.mult(direction, modulation);
	}
  
  public DLVector projectVectorOnPlane(DLVector normal) {
  	return projectVectorOnPlane(this, normal);
  }

	/**
	 * Utility function that simply projects {@code src} on the plane whose normal
	 * is {@code normal} that passes through the origin.
	 * <p>
	 * {@code normal} does not need to be normalized (but must be non null).
	 */
	public static DLVector projectVectorOnPlane(DLVector src, DLVector normal) {
		float normalSquaredNorm = squaredNorm(normal);
		if (Geom.zero(normalSquaredNorm))
			throw new RuntimeException("Normal squared norm is nearly 0");

		float modulation = src.dot(normal) / normalSquaredNorm;
		return DLVector.sub(src, DLVector.mult(normal, modulation));
	}
	
	public static final float lerp(float start, float stop, float amt) {
    return start + (stop-start) * amt;
  }
	
	public float squaredNorm() {
		return squaredNorm(this);
	}

	/**
	 * Utility function that returns the squared norm of the Vector3D.
	 */
	public static float squaredNorm(DLVector v) {
		return (v.vec[0] * v.vec[0]) + (v.vec[1] * v.vec[1]) + (v.vec[2] * v.vec[2]);
	}
	
	public DLVector orthogonalVector() {
		return orthogonalVector(this);
	}

	/**
	 * Utility function that returns a Vector3D orthogonal to {@code v}. Its
	 * {@code mag()} depends on the Vector3D, but is zero only for a {@code null}
	 * Vector3D. Note that the function that associates an {@code
	 * orthogonalVector()} to a Vector3D is not continuous.
	 */
	public static DLVector orthogonalVector(DLVector v) {
		// Find smallest component. Keep equal case for null values.
		if ((Math.abs(v.vec[1]) >= 0.9f * Math.abs(v.vec[0]))
				&& (Math.abs(v.vec[2]) >= 0.9f * Math.abs(v.vec[0])))
			return new DLVector(0.0f, -v.vec[2], v.vec[1]);
		else if ((Math.abs(v.vec[0]) >= 0.9f * Math.abs(v.vec[1]))
				&& (Math.abs(v.vec[2]) >= 0.9f * Math.abs(v.vec[1])))
			return new DLVector(-v.vec[2], 0.0f, v.vec[0]);
		else
			return new DLVector(-v.vec[1], v.vec[0], 0.0f);
	}
	
	@Override
	public void link(float [] src) {
		vec = src;
	}
	
	@Override
	public void unLink() {
		float [] data = new float [3];
  	get(data);
  	set(data);
	}
	
	public void reset() {
		vec[0] = vec[1] = vec[2] = 0;
	}
  
  // end new

  /**
   * Set x, y, and z coordinates.
   *
   * @param x the x coordinate.
   * @param y the y coordinate.
   * @param z the z coordinate.
   */
  public void set(float x, float y, float z) {
    this.vec[0] = x;
    this.vec[1] = y;
    this.vec[2] = z;
  }

  /**
   * Set x, y, and z coordinates from a Vector3D object.
   *
   * @param v the Vector3D object to be copied
   */
  @Override
  public void set(Primitivable v) {
  	if(! (v instanceof DLVector) )
  		throw new RuntimeException("v should be an instance of Vector3D");
  	set((DLVector) v);
  }
  
  public void set(DLVector v) {
  	this.vec[0] = v.vec[0];
  	this.vec[1] = v.vec[1];
  	this.vec[2] = v.vec[2];
  }

  /**
   * Set the x, y (and maybe z) coordinates using a float[] array as the source.
   * @param source array to copy from
   */
  @Override
  public void set(float[] source) {
    if (source.length >= 2) {
    	this.vec[0] = source[0];
    	this.vec[1] = source[1];
    }
    if (source.length >= 3) {
    	this.vec[2] = source[2];
    }
  }

  /**
   * Get a copy of this vector.
   */
  @Override
  public DLVector get() {
  	return new DLVector(this);
  }

  @Override
  public float[] get(float[] target) {
    if (target == null) {
      return new float[] { this.vec[0], this.vec[1], this.vec[2] };
    }
    if (target.length >= 2) {
      target[0] = this.vec[0];
      target[1] = this.vec[1];
    }
    if (target.length >= 3) {
      target[2] = this.vec[2];
    }
    return target;
  }

  /**
   * Calculate the magnitude (length) of the vector
   * @return the magnitude of the vector
   */
  public float mag() {
    return (float) Math.sqrt(this.vec[0]*this.vec[0] + this.vec[1]*this.vec[1] + this.vec[2]*this.vec[2]);
  }

  /**
   * Calculate the squared magnitude of the vector
   * Faster if the real length is not required in the 
   * case of comparing vectors, etc.
   * 
   * @return squared magnitude of the vector
   */
  public float magSq() {
    return (this.vec[0]*this.vec[0] + this.vec[1]*this.vec[1] + this.vec[2]*this.vec[2]);
  }

  /**
   * Add a vector to this vector
   * @param v the vector to be added
   */
  public void add(DLVector v) {
  	this.vec[0] += v.vec[0];
  	this.vec[1] += v.vec[1];
  	this.vec[2] += v.vec[2];
  }

  public void add(float x, float y, float z) {
    this.vec[0] += x;
    this.vec[1] += y;
    this.vec[2] += z;
  }

  /**
   * Add two vectors
   * @param v1 a vector
   * @param v2 another vector
   * @return a new vector that is the sum of v1 and v2
   */
  static public DLVector add(DLVector v1, DLVector v2) {
    return add(v1, v2, null);
  }

  /**
   * Add two vectors into a target vector
   * @param v1 a vector
   * @param v2 another vector
   * @param target the target vector (if null, a new vector will be created)
   * @return a new vector that is the sum of v1 and v2
   */
  static public DLVector add(DLVector v1, DLVector v2, DLVector target) {
    if (target == null) {
      target = new DLVector(v1.vec[0] + v2.vec[0],v1.vec[1] + v2.vec[1], v1.vec[2] + v2.vec[2]);
    } else {
      target.set(v1.vec[0] + v2.vec[0], v1.vec[1] + v2.vec[1], v1.vec[2] + v2.vec[2]);
    }
    return target;
  }

  /**
   * Subtract a vector from this vector
   * @param v the vector to be subtracted
   */
  public void sub(DLVector v) {
  	this.vec[0] -= v.vec[0];
  	this.vec[1] -= v.vec[1];
  	this.vec[2] -= v.vec[2];
  }

  public void sub(float x, float y, float z) {
    this.vec[0] -= x;
    this.vec[1] -= y;
    this.vec[2] -= z;
  }

  /**
   * Subtract one vector from another
   * @param v1 a vector
   * @param v2 another vector
   * @return a new vector that is v1 - v2
   */
  static public DLVector sub(DLVector v1, DLVector v2) {
    return sub(v1, v2, null);
  }

  static public DLVector sub(DLVector v1, DLVector v2, DLVector target) {
    if (target == null) {
      target = new DLVector(v1.vec[0] - v2.vec[0], v1.vec[1] - v2.vec[1], v1.vec[2] - v2.vec[2]);
    } else {
      target.set(v1.vec[0] - v2.vec[0], v1.vec[1] - v2.vec[1], v1.vec[2] - v2.vec[2]);
    }
    return target;
  }

  /**
   * Multiply this vector by a scalar
   * @param n the value to multiply by
   */
  public void mult(float n) {
  	this.vec[0] *= n;
  	this.vec[1] *= n;
  	this.vec[2] *= n;
  }

  /**
   * Multiply a vector by a scalar
   * @param v a vector
   * @param n scalar
   * @return a new vector that is v1 * n
   */
  static public DLVector mult(DLVector v, float n) {
    return mult(v, n, null);
  }

  /**
   * Multiply a vector by a scalar, and write the result into a target Vector3D.
   * @param v a vector
   * @param n scalar
   * @param target Vector3D to store the result
   * @return the target vector, now set to v1 * n
   */
  static public DLVector mult(DLVector v, float n, DLVector target) {
    if (target == null) {
      target = new DLVector(v.vec[0]*n, v.vec[1]*n, v.vec[2]*n);
    } else {
      target.set(v.vec[0]*n, v.vec[1]*n, v.vec[2]*n);
    }
    return target;
  }

  /**
   * Multiply each element of one vector by the elements of another vector.
   * @param v the vector to multiply by
   */
  public void mult(DLVector v) {
  	this.vec[0] *= v.vec[0];
  	this.vec[1] *= v.vec[1];
  	this.vec[2] *= v.vec[2];
  }

  /**
   * Multiply each element of one vector by the individual elements of another
   * vector, and return the result as a new Vector3D.
   */
  static public DLVector mult(DLVector v1, DLVector v2) {
    return mult(v1, v2, null);
  }

  /**
   * Multiply each element of one vector by the individual elements of another
   * vector, and write the result into a target vector.
   * @param v1 the first vector
   * @param v2 the second vector
   * @param target Vector3D to store the result
   */
  static public DLVector mult(DLVector v1, DLVector v2, DLVector target) {
    if (target == null) {
      target = new DLVector(v1.vec[0]*v2.vec[0], v1.vec[1]*v2.vec[1], v1.vec[2]*v2.vec[2]);
    } else {
      target.set(v1.vec[0]*v2.vec[0], v1.vec[1]*v2.vec[1], v1.vec[2]*v2.vec[2]);
    }
    return target;
  }

  /**
   * Divide this vector by a scalar
   * @param n the value to divide by
   */
  public void div(float n) {
  	this.vec[0] /= n;
  	this.vec[1] /= n;
  	this.vec[2] /= n;
  }

  /**
   * Divide a vector by a scalar and return the result in a new vector.
   * @param v a vector
   * @param n scalar
   * @return a new vector that is v1 / n
   */
  static public DLVector div(DLVector v, float n) {
    return div(v, n, null);
  }

  static public DLVector div(DLVector v, float n, DLVector target) {
    if (target == null) {
      target = new DLVector(v.vec[0]/n, v.vec[1]/n, v.vec[2]/n);
    } else {
      target.set(v.vec[0]/n, v.vec[1]/n, v.vec[2]/n);
    }
    return target;
  }

  /**
   * Divide each element of one vector by the elements of another vector.
   */
  public void div(DLVector v) {
  	this.vec[0] /= v.vec[0];
  	this.vec[1] /= v.vec[1];
  	this.vec[2] /= v.vec[2];
  }

  /**
   * Divide each element of one vector by the individual elements of another
   * vector, and return the result as a new Vector3D.
   */
  static public DLVector div(DLVector v1, DLVector v2) {
    return div(v1, v2, null);
  }

  /**
   * Divide each element of one vector by the individual elements of another
   * vector, and write the result into a target vector.
   * @param v1 the first vector
   * @param v2 the second vector
   * @param target Vector3D to store the result
   */
  static public DLVector div(DLVector v1, DLVector v2, DLVector target) {
    if (target == null) {
      target = new DLVector(v1.vec[0]/v2.vec[0], v1.vec[1]/v2.vec[1], v1.vec[2]/v2.vec[2]);
    } else {
      target.set(v1.vec[0]/v2.vec[0], v1.vec[1]/v2.vec[1], v1.vec[2]/v2.vec[2]);
    }
    return target;
  }

  /**
   * Calculate the Euclidean distance between two points (considering a point as a vector object)
   * @param v another vector
   * @return the Euclidean distance between
   */
  public float dist(DLVector v) {
    float dx = this.vec[0] - v.vec[0];
    float dy = this.vec[1] - v.vec[1];
    float dz = this.vec[2] - v.vec[2];
    return (float) Math.sqrt(dx*dx + dy*dy + dz*dz);
  }

  /**
   * Calculate the Euclidean distance between two points (considering a point as a vector object)
   * @param v1 a vector
   * @param v2 another vector
   * @return the Euclidean distance between v1 and v2
   */
  static public float dist(DLVector v1, DLVector v2) {
    float dx = v1.vec[0] - v2.vec[0];
    float dy = v1.vec[1] - v2.vec[1];
    float dz = v1.vec[2] - v2.vec[2];
    return (float) Math.sqrt(dx*dx + dy*dy + dz*dz);
  }

  /**
   * Calculate the dot product with another vector
   * @return the dot product
   */
  public float dot(DLVector v) {
    return this.vec[0]*v.vec[0] + this.vec[1]*v.vec[1] + this.vec[2]*v.vec[2];
  }

  public float dot(float x, float y, float z) {
    return this.vec[0]*x + this.vec[1]*y + this.vec[2]*z;
  }

  static public float dot(DLVector v1, DLVector v2) {
    return v1.vec[0]*v2.vec[0] + v1.vec[1]*v2.vec[1] + v1.vec[2]*v2.vec[2];
  }

  /**
   * Return a vector composed of the cross product between this and another.
   */
  public DLVector cross(DLVector v) {
    return cross(v, null);
  }

  /**
   * Perform cross product between this and another vector, and store the
   * result in 'target'. If target is null, a new vector is created.
   */
  public DLVector cross(DLVector v, DLVector target) {
    float crossX = this.vec[1] * v.vec[2] - v.vec[1] * this.vec[2];
    float crossY = this.vec[2] * v.vec[0] - v.vec[2] * this.vec[0];
    float crossZ = this.vec[0] * v.vec[1] - v.vec[0] * this.vec[1];

    if (target == null) {
      target = new DLVector(crossX, crossY, crossZ);
    } else {
      target.set(crossX, crossY, crossZ);
    }
    return target;
  }

  static public DLVector cross(DLVector v1, DLVector v2, DLVector target) {
    float crossX = v1.vec[1] * v2.vec[2] - v2.vec[1] * v1.vec[2];
    float crossY = v1.vec[2] * v2.vec[0] - v2.vec[2] * v1.vec[0];
    float crossZ = v1.vec[0] * v2.vec[1] - v2.vec[0] * v1.vec[1];

    if (target == null) {
      target = new DLVector(crossX, crossY, crossZ);
    } else {
      target.set(crossX, crossY, crossZ);
    }
    return target;
  }

  /**
   * Normalize the vector to length 1 (make it a unit vector)
   */
  public void normalize() {
    float m = mag();
    if (m != 0 && m != 1) {
      div(m);
    }
  }

  /**
   * Normalize this vector, storing the result in another vector.
   * @param target Set to null to create a new vector
   * @return a new vector (if target was null), or target
   */
  public DLVector normalize(DLVector target) {
    if (target == null) {
      target = new DLVector();
    }
    float m = mag();
    if (m > 0) {
      target.set(vec[0]/m, vec[1]/m, vec[02]/m);
    } else {
      target.set(vec[0], vec[1], vec[2]);
    }
    return target;
  }

  /**
   * Limit the magnitude of this vector
   * @param max the maximum length to limit this vector
   */
  public void limit(float max) {
    if (mag() > max) {
      normalize();
      mult(max);
    }
  }

  /**
   * Sets the magnitude of the vector to an arbitrary amount.
   * @param len the new length for this vector
   */
  public void setMag(float len) {
    normalize();
    mult(len);	
  }

  /**
   * Sets the magnitude of this vector, storing the result in another vector.
   * @param target Set to null to create a new vector
   * @param len the new length for the new vector
   * @return a new vector (if target was null), or target
   */
  public DLVector setMag(DLVector target, float len) {
    target = normalize(target);
    target.mult(len);
    return target;
  }

  /**
   * Calculate the angle of rotation for this vector (only 2D vectors)
   * @return the angle of rotation
   */
  public float heading2D() {
    float angle = (float) Math.atan2(-this.vec[1], this.vec[0]);
    return -1*angle;
  }

  /**
   * Rotate the vector by an angle (only 2D vectors), magnitude remains the same
   * @param theta the angle of rotation
   */
  public void rotate(float theta) {
    float xTemp = this.vec[0];
    // Might need to check for rounding errors like with angleBetween function?
    this.vec[0] = this.vec[0]*(float) Math.cos(theta) - this.vec[1]*(float) Math.sin(theta);
    this.vec[1] = xTemp*(float) Math.sin(theta) + this.vec[1]*(float) Math.cos(theta);
  }

  /**
   * Linear interpolate the vector to another vector
   * @param v the vector to lerp to
   * @param amt  The amt parameter is the amount to interpolate between the two vectors where 1.0 equal to the new vector
   * 0.1 is very near the new vector, 0.5 is half-way in between.
   */
  public void lerp(DLVector v, float amt) {
  	this.vec[0] = lerp(this.vec[0],v.vec[0],amt);
  	this.vec[1] = lerp(this.vec[1],v.vec[1],amt);
  }

  public void lerp(float x, float y, float z, float amt) {
    this.vec[0] = lerp(this.vec[0],x,amt);
    this.vec[1] = lerp(this.vec[1],y,amt);
  }

  /**
   * Calculate the angle between two vectors, using the dot product
   * @param v1 a vector
   * @param v2 another vector
   * @return the angle between the vectors
   */
  static public float angleBetween(DLVector v1, DLVector v2) {
    double dot = v1.vec[0] * v2.vec[0] + v1.vec[1] * v2.vec[1] + v1.vec[2] * v2.vec[2];
    double v1mag = Math.sqrt(v1.vec[0] * v1.vec[0] + v1.vec[1] * v1.vec[1] + v1.vec[2] * v1.vec[2]);
    double v2mag = Math.sqrt(v2.vec[0] * v2.vec[0] + v2.vec[1] * v2.vec[1] + v2.vec[2] * v2.vec[2]);
    // This should be a number between -1 and 1, since it's "normalized"
    double amt = dot / (v1mag * v2mag);
    // But if it's not due to rounding error, then we need to fix it
    // http://code.google.com/p/processing/issues/detail?id=340
    // Otherwise if outside the range, acos() will return NaN
    // http://www.cppreference.com/wiki/c/math/acos
    if (amt <= -1) {
      return PI;
    } else if (amt >= 1) {
      // http://code.google.com/p/processing/issues/detail?id=435
      return 0;
    }
    return (float) Math.acos(amt);
  }
  
  /**
   * Make a new 2D unit vector from an angle.
   *
   * ( end auto-generated )
   *
   * @webref pvector:method
   * @usage web_application
   * @brief Make a new 2D unit vector from an angle
   * @param angle the angle
   * @return the new unit PVector3D
   */
  static public DLVector fromAngle(float angle) {
    return fromAngle(angle,null);
  }


  /**
   * Make a new 2D unit vector from an angle
   * @param angle the angle
   * @param target the target vector (if null, a new vector will be created)
   * @return the Vector3D
   */
  static public DLVector fromAngle(float angle, DLVector target) {
    if (target == null) {
      target = new DLVector((float)Math.cos(angle),(float)Math.sin(angle),0);
    } else {
      target.set((float)Math.cos(angle),(float)Math.sin(angle),0);
    }
    return target;
  }
  
  public void print() {
  	System.out.println(x() + " " + y() + " " + z() + "\n");
  }

  public String toString() {
    return "[ " + this.vec[0] + ", " + this.vec[1] + ", " + this.vec[2] + " ]";
  }  
}