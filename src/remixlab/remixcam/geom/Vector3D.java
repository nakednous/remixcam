/* -*- mode: java; c-basic-offset: 2; indent-tabs-mode: nil -*- */

/*
  Part of the Processing project - http://processing.org

  Copyright (c) 2008 Dan Shiffman
  Copyright (c) 2008-10 Ben Fry and Casey Reas

  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public
  License version 2.1 as published by the Free Software Foundation.

  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General
  Public License along with this library; if not, write to the
  Free Software Foundation, Inc., 59 Temple Place, Suite 330,
  Boston, MA  02111-1307  USA
*/

package remixlab.remixcam.geom;

import java.io.Serializable;

/**
 * A class to describe a two or three dimensional vector.
 * <p>
 * The result of all functions are applied to the vector itself, with the
 * exception of cross(), which returns a new Vector3D (or writes to a specified
 * 'target' Vector3D). That is, add() will add the contents of one vector to
 * this one. Using add() with additional parameters allows you to put the
 * result into a new Vector3D. Functions that act on multiple vectors also
 * include static versions. Because creating new objects can be computationally
 * expensive, most functions include an optional 'target' Vector3D, so that a
 * new Vector3D object is not created with each operation.
 * <p>
 * Initially based on the Vector3D class by <a href="http://www.shiffman.net">Dan Shiffman</a>.
 */
public class Vector3D implements Serializable {

  /**
	 * Generated 2011-03-12 by nakednous
	 */
	private static final long serialVersionUID = 3003291235947398399L;

	/** The x component of the vector. */
  public float x;

  /** The y component of the vector. */
  public float y;

  /** The z component of the vector. */
  public float z;

  /** Array so that this can be temporarily used in an array context */
  transient protected float[] array;

  /**
   * Constructor for an empty vector: x, y, and z are set to 0.
   */
  public Vector3D() {
  }


  /**
   * Constructor for a 3D vector.
   *
   * @param  x the x coordinate.
   * @param  y the y coordinate.
   * @param  z the y coordinate.
   */
  public Vector3D(float x, float y, float z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }


  /**
   * Constructor for a 2D vector: z coordinate is set to 0.
   *
   * @param  x the x coordinate.
   * @param  y the y coordinate.
   */
  public Vector3D(float x, float y) {
    this.x = x;
    this.y = y;
    this.z = 0;
  }
  

  /**
   * Set x, y, and z coordinates.
   *
   * @param x the x coordinate.
   * @param y the y coordinate.
   * @param z the z coordinate.
   */
  public void set(float x, float y, float z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }


  /**
   * Set x, y, and z coordinates from a Vector3D object.
   *
   * @param v the Vector3D object to be copied
   */
  public void set(Vector3D v) {
    x = v.x;
    y = v.y;
    z = v.z;
  }


  /**
   * Set the x, y (and maybe z) coordinates using a float[] array as the source.
   * @param source array to copy from
   */
  public void set(float[] source) {
    if (source.length >= 2) {
      x = source[0];
      y = source[1];
    }
    if (source.length >= 3) {
      z = source[2];
    }
  }


  /**
   * Get a copy of this vector.
   */
  public Vector3D get() {
    return new Vector3D(x, y, z);
  }


  public float[] get(float[] target) {
    if (target == null) {
      return new float[] { x, y, z };
    }
    if (target.length >= 2) {
      target[0] = x;
      target[1] = y;
    }
    if (target.length >= 3) {
      target[2] = z;
    }
    return target;
  }


  /**
   * Calculate the magnitude (length) of the vector
   * @return the magnitude of the vector
   */
  public float mag() {
    return (float) Math.sqrt(x*x + y*y + z*z);
  }


  /**
   * Add a vector to this vector
   * @param v the vector to be added
   */
  public void add(Vector3D v) {
    x += v.x;
    y += v.y;
    z += v.z;
  }


  public void add(float x, float y, float z) {
    this.x += x;
    this.y += y;
    this.z += z;
  }


  /**
   * Add two vectors
   * @param v1 a vector
   * @param v2 another vector
   * @return a new vector that is the sum of v1 and v2
   */
  static public Vector3D add(Vector3D v1, Vector3D v2) {
    return add(v1, v2, null);
  }


  /**
   * Add two vectors into a target vector
   * @param v1 a vector
   * @param v2 another vector
   * @param target the target vector (if null, a new vector will be created)
   * @return a new vector that is the sum of v1 and v2
   */
  static public Vector3D add(Vector3D v1, Vector3D v2, Vector3D target) {
    if (target == null) {
      target = new Vector3D(v1.x + v2.x,v1.y + v2.y, v1.z + v2.z);
    } else {
      target.set(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
    }
    return target;
  }


  /**
   * Subtract a vector from this vector
   * @param v the vector to be subtracted
   */
  public void sub(Vector3D v) {
    x -= v.x;
    y -= v.y;
    z -= v.z;
  }


  public void sub(float x, float y, float z) {
    this.x -= x;
    this.y -= y;
    this.z -= z;
  }


  /**
   * Subtract one vector from another
   * @param v1 a vector
   * @param v2 another vector
   * @return a new vector that is v1 - v2
   */
  static public Vector3D sub(Vector3D v1, Vector3D v2) {
    return sub(v1, v2, null);
  }


  static public Vector3D sub(Vector3D v1, Vector3D v2, Vector3D target) {
    if (target == null) {
      target = new Vector3D(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
    } else {
      target.set(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
    }
    return target;
  }


  /**
   * Multiply this vector by a scalar
   * @param n the value to multiply by
   */
  public void mult(float n) {
    x *= n;
    y *= n;
    z *= n;
  }


  /**
   * Multiply a vector by a scalar
   * @param v a vector
   * @param n scalar
   * @return a new vector that is v1 * n
   */
  static public Vector3D mult(Vector3D v, float n) {
    return mult(v, n, null);
  }


  /**
   * Multiply a vector by a scalar, and write the result into a target Vector3D.
   * @param v a vector
   * @param n scalar
   * @param target Vector3D to store the result
   * @return the target vector, now set to v1 * n
   */
  static public Vector3D mult(Vector3D v, float n, Vector3D target) {
    if (target == null) {
      target = new Vector3D(v.x*n, v.y*n, v.z*n);
    } else {
      target.set(v.x*n, v.y*n, v.z*n);
    }
    return target;
  }


  /**
   * Multiply each element of one vector by the elements of another vector.
   * @param v the vector to multiply by
   */
  public void mult(Vector3D v) {
    x *= v.x;
    y *= v.y;
    z *= v.z;
  }


  /**
   * Multiply each element of one vector by the individual elements of another
   * vector, and return the result as a new Vector3D.
   */
  static public Vector3D mult(Vector3D v1, Vector3D v2) {
    return mult(v1, v2, null);
  }


  /**
   * Multiply each element of one vector by the individual elements of another
   * vector, and write the result into a target vector.
   * @param v1 the first vector
   * @param v2 the second vector
   * @param target Vector3D to store the result
   */
  static public Vector3D mult(Vector3D v1, Vector3D v2, Vector3D target) {
    if (target == null) {
      target = new Vector3D(v1.x*v2.x, v1.y*v2.y, v1.z*v2.z);
    } else {
      target.set(v1.x*v2.x, v1.y*v2.y, v1.z*v2.z);
    }
    return target;
  }


  /**
   * Divide this vector by a scalar
   * @param n the value to divide by
   */
  public void div(float n) {
    x /= n;
    y /= n;
    z /= n;
  }


  /**
   * Divide a vector by a scalar and return the result in a new vector.
   * @param v a vector
   * @param n scalar
   * @return a new vector that is v1 / n
   */
  static public Vector3D div(Vector3D v, float n) {
    return div(v, n, null);
  }


  static public Vector3D div(Vector3D v, float n, Vector3D target) {
    if (target == null) {
      target = new Vector3D(v.x/n, v.y/n, v.z/n);
    } else {
      target.set(v.x/n, v.y/n, v.z/n);
    }
    return target;
  }


  /**
   * Divide each element of one vector by the elements of another vector.
   */
  public void div(Vector3D v) {
    x /= v.x;
    y /= v.y;
    z /= v.z;
  }


  /**
   * Multiply each element of one vector by the individual elements of another
   * vector, and return the result as a new Vector3D.
   */
  static public Vector3D div(Vector3D v1, Vector3D v2) {
    return div(v1, v2, null);
  }


  /**
   * Divide each element of one vector by the individual elements of another
   * vector, and write the result into a target vector.
   * @param v1 the first vector
   * @param v2 the second vector
   * @param target Vector3D to store the result
   */
  static public Vector3D div(Vector3D v1, Vector3D v2, Vector3D target) {
    if (target == null) {
      target = new Vector3D(v1.x/v2.x, v1.y/v2.y, v1.z/v2.z);
    } else {
      target.set(v1.x/v2.x, v1.y/v2.y, v1.z/v2.z);
    }
    return target;
  }


  /**
   * Calculate the Euclidean distance between two points (considering a point as a vector object)
   * @param v another vector
   * @return the Euclidean distance between
   */
  public float dist(Vector3D v) {
    float dx = x - v.x;
    float dy = y - v.y;
    float dz = z - v.z;
    return (float) Math.sqrt(dx*dx + dy*dy + dz*dz);
  }


  /**
   * Calculate the Euclidean distance between two points (considering a point as a vector object)
   * @param v1 a vector
   * @param v2 another vector
   * @return the Euclidean distance between v1 and v2
   */
  static public float dist(Vector3D v1, Vector3D v2) {
    float dx = v1.x - v2.x;
    float dy = v1.y - v2.y;
    float dz = v1.z - v2.z;
    return (float) Math.sqrt(dx*dx + dy*dy + dz*dz);
  }


  /**
   * Calculate the dot product with another vector
   * @return the dot product
   */
  public float dot(Vector3D v) {
    return x*v.x + y*v.y + z*v.z;
  }


  public float dot(float x, float y, float z) {
    return this.x*x + this.y*y + this.z*z;
  }


  static public float dot(Vector3D v1, Vector3D v2) {
      return v1.x*v2.x + v1.y*v2.y + v1.z*v2.z;
  }


  /**
   * Return a vector composed of the cross product between this and another.
   */
  public Vector3D cross(Vector3D v) {
    return cross(v, null);
  }


  /**
   * Perform cross product between this and another vector, and store the
   * result in 'target'. If target is null, a new vector is created.
   */
  public Vector3D cross(Vector3D v, Vector3D target) {
    float crossX = y * v.z - v.y * z;
    float crossY = z * v.x - v.z * x;
    float crossZ = x * v.y - v.x * y;

    if (target == null) {
      target = new Vector3D(crossX, crossY, crossZ);
    } else {
      target.set(crossX, crossY, crossZ);
    }
    return target;
  }


  static public Vector3D cross(Vector3D v1, Vector3D v2, Vector3D target) {
    float crossX = v1.y * v2.z - v2.y * v1.z;
    float crossY = v1.z * v2.x - v2.z * v1.x;
    float crossZ = v1.x * v2.y - v2.x * v1.y;

    if (target == null) {
      target = new Vector3D(crossX, crossY, crossZ);
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
  public Vector3D normalize(Vector3D target) {
    if (target == null) {
      target = new Vector3D();
    }
    float m = mag();
    if (m > 0) {
      target.set(x/m, y/m, z/m);
    } else {
      target.set(x, y, z);
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
   * Calculate the angle of rotation for this vector (only 2D vectors)
   * @return the angle of rotation
   */
  public float heading2D() {
    float angle = (float) Math.atan2(-y, x);
    return -1*angle;
  }


  /**
   * Calculate the angle between two vectors, using the dot product
   * @param v1 a vector
   * @param v2 another vector
   * @return the angle between the vectors
   */
  static public float angleBetween(Vector3D v1, Vector3D v2) {
    double dot = v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
    double v1mag = Math.sqrt(v1.x * v1.x + v1.y * v1.y + v1.z * v1.z);
    double v2mag = Math.sqrt(v2.x * v2.x + v2.y * v2.y + v2.z * v2.z);
    // This should be a number between -1 and 1, since it's "normalized"
    double amt = dot / (v1mag * v2mag);
    // But if it's not due to rounding error, then we need to fix it
    // http://code.google.com/p/processing/issues/detail?id=340
    // Otherwise if outside the range, acos() will return NaN
    // http://www.cppreference.com/wiki/c/math/acos
    if (amt <= -1) {
      return (float) Math.PI;
    } else if (amt >= 1) {
      // http://code.google.com/p/processing/issues/detail?id=435
      return 0;
    }
    return (float) Math.acos(amt);
  }


  public String toString() {
    return "[ " + x + ", " + y + ", " + z + " ]";
  }


  /**
   * Return a representation of this vector as a float array. This is only for
   * temporary use. If used in any other fashion, the contents should be copied
   * by using the get() command to copy into your own array.
   */
  public float[] array() {
    if (array == null) {
      array = new float[3];
    }
    array[0] = x;
    array[1] = y;
    array[2] = z;
    return array;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Vector3D))
      return false;
    final Vector3D p = (Vector3D) obj;
    return x == p.x && y == p.y && z == p.z;
  }

  @Override
  public int hashCode() {
    int result = 1;
    result = 31 * result + Float.floatToIntBits(x);
    result = 31 * result + Float.floatToIntBits(y);
    result = 31 * result + Float.floatToIntBits(z);
    return result;
  }
  
  public Vector3D projectVectorOnAxis(Vector3D direction) {
  	return projectVectorOnAxis(this, direction);
	}

	/**
	 * Utility function that simply projects {@code src} on the axis of direction
	 * {@code direction} that passes through the origin.
	 * <p>
	 * {@code direction} does not need to be normalized (but must be non null).
	 */
	public static Vector3D projectVectorOnAxis(Vector3D src, Vector3D direction) {
		float directionSquaredNorm = squaredNorm(direction);
		if (directionSquaredNorm < 1E-10f)
			throw new RuntimeException("Direction squared norm is nearly 0");

		float modulation = src.dot(direction) / directionSquaredNorm;
		return Vector3D.mult(direction, modulation);
	}
	
	public Vector3D projectVectorOnPlane(Vector3D normal) {
		return projectVectorOnPlane(this, normal);
	}

	/**
	 * Utility function that simply projects {@code src} on the plane whose normal
	 * is {@code normal} that passes through the origin.
	 * <p>
	 * {@code normal} does not need to be normalized (but must be non null).
	 */
	public static Vector3D projectVectorOnPlane(Vector3D src, Vector3D normal) {
		float normalSquaredNorm = squaredNorm(normal);
		if (normalSquaredNorm < 1E-10f)
			throw new RuntimeException("Normal squared norm is nearly 0");

		float modulation = src.dot(normal) / normalSquaredNorm;
		return Vector3D.sub(src, Vector3D.mult(normal, modulation));
	}
	
	public float squaredNorm() {
		return squaredNorm(this);
	}

	/**
	 * Utility function that returns the squared norm of the Vector3D.
	 */
	public static float squaredNorm(Vector3D v) {
		return (v.x * v.x) + (v.y * v.y) + (v.z * v.z);
	}

	/**
	 * Utility function that returns a Vector3D orthogonal to {@code v}. Its
	 * {@code mag()} depends on the Vector3D, but is zero only for a {@code null}
	 * Vector3D. Note that the function that associates an {@code
	 * orthogonalVector()} to a Vector3D is not continuous.
	 */
	public static Vector3D orthogonalVector(Vector3D v) {
		// Find smallest component. Keep equal case for null values.
		if ((Math.abs(v.y) >= 0.9f * Math.abs(v.x))
				&& (Math.abs(v.z) >= 0.9f * Math.abs(v.x)))
			return new Vector3D(0.0f, -v.z, v.y);
		else if ((Math.abs(v.x) >= 0.9f * Math.abs(v.y))
				&& (Math.abs(v.z) >= 0.9f * Math.abs(v.y)))
			return new Vector3D(-v.z, 0.0f, v.x);
		else
			return new Vector3D(-v.y, v.x, 0.0f);
	}
}
