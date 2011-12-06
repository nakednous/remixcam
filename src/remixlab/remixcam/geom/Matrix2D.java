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

/**
 * 3x2 affine matrix implementation.
 * <p>
 * This class has been almost entirely taken from Processing.
 * 
 * @author pierre
 */
public class Matrix2D implements Matrix {

  public float m00, m01, m02;
  public float m10, m11, m12;


  public Matrix2D() {
    reset();
  }


  public Matrix2D(float m00, float m01, float m02,
                   float m10, float m11, float m12) {
    set(m00, m01, m02,
        m10, m11, m12);
  }


  public Matrix2D(Matrix matrix) {
    set(matrix);
  }
  

  /**
  // TODO: need to override this?
  public Matrix2D(Object any) {
  	try {
  		float [] result = new float [16];  		
      any.getClass().getMethod("get", new Class[] { float [].class }).invoke(any, result);
      
      m00 = result[0];
      m01 = result[1];
      m02 = result[2];

      m10 = result[3];
      m11 = result[4];
      m12 = result[5];      
      
  		} catch ( Exception e ) {
  			throw(new RuntimeException("vec cannot handle class in constructor: "+any.getClass(),e));
  		}
  }
  */


  public void reset() {
    set(1, 0, 0,
        0, 1, 0);
  }


  /**
   * Returns a copy of this Matrix.
   */
  public Matrix2D get() {
    Matrix2D outgoing = new Matrix2D();
    outgoing.set(this);
    return outgoing;
  }


  /**
   * Copies the matrix contents into a 6 entry float array.
   * If target is null (or not the correct size), a new array will be created.
   */
  public float[] get(float[] target) {
    if ((target == null) || (target.length != 6)) {
      target = new float[6];
    }
    target[0] = m00;
    target[1] = m01;
    target[2] = m02;

    target[3] = m10;
    target[4] = m11;
    target[5] = m12;

    return target;
  }


  public void set(Matrix matrix) {
    if (matrix instanceof Matrix2D) {
      Matrix2D src = (Matrix2D) matrix;
      set(src.m00, src.m01, src.m02,
          src.m10, src.m11, src.m12);
    } else {
      throw new IllegalArgumentException("Matrix2D.set() only accepts Matrix2D objects.");
    }
  }


  public void set(Matrix3D src) {
  }


  public void set(float[] source) {
    m00 = source[0];
    m01 = source[1];
    m02 = source[2];

    m10 = source[3];
    m11 = source[4];
    m12 = source[5];
  }


  public void set(float m00, float m01, float m02,
                  float m10, float m11, float m12) {
    this.m00 = m00; this.m01 = m01; this.m02 = m02;
    this.m10 = m10; this.m11 = m11; this.m12 = m12;
  }


  public void set(float m00, float m01, float m02, float m03,
                  float m10, float m11, float m12, float m13,
                  float m20, float m21, float m22, float m23,
                  float m30, float m31, float m32, float m33) {

  }


  public void translate(float tx, float ty) {
    m02 = tx*m00 + ty*m01 + m02;
    m12 = tx*m10 + ty*m11 + m12;
  }


  public void translate(float x, float y, float z) {
    throw new IllegalArgumentException("Cannot use translate(x, y, z) on a Matrix2D.");
  }


  // Implementation roughly based on AffineTransform.
  public void rotate(float angle) {
    float s = sin(angle);
    float c = cos(angle);

    float temp1 = m00;
    float temp2 = m01;
    m00 =  c * temp1 + s * temp2;
    m01 = -s * temp1 + c * temp2;
    temp1 = m10;
    temp2 = m11;
    m10 =  c * temp1 + s * temp2;
    m11 = -s * temp1 + c * temp2;
  }


  public void rotateX(float angle) {
    throw new IllegalArgumentException("Cannot use rotateX() on a Matrix2D.");
  }


  public void rotateY(float angle) {
    throw new IllegalArgumentException("Cannot use rotateY() on a Matrix2D.");
  }


  public void rotateZ(float angle) {
    rotate(angle);
  }


  public void rotate(float angle, float v0, float v1, float v2) {
    throw new IllegalArgumentException("Cannot use this version of rotate() on a Matrix2D.");
  }


  public void scale(float s) {
    scale(s, s);
  }


  public void scale(float sx, float sy) {
    m00 *= sx;  m01 *= sy;
    m10 *= sx;  m11 *= sy;
  }


  public void scale(float x, float y, float z) {
    throw new IllegalArgumentException("Cannot use this version of scale() on a Matrix2D.");
  }


  public void shearX(float angle) {
    apply(1, 0, 1,  tan(angle), 0, 0);
  }


  public void shearY(float angle) {
    apply(1, 0, 1,  0, tan(angle), 0);
  }


  public void apply(Matrix source) {
    if (source instanceof Matrix2D) {
      apply((Matrix2D) source);
    } else if (source instanceof Matrix3D) {
      apply((Matrix3D) source);
    }
  }


  public void apply(Matrix2D source) {
    apply(source.m00, source.m01, source.m02,
          source.m10, source.m11, source.m12);
  }


  public void apply(Matrix3D source) {
    throw new IllegalArgumentException("Cannot use apply(Matrix3D) on a Matrix2D.");
  }


  public void apply(float n00, float n01, float n02,
                    float n10, float n11, float n12) {
    float t0 = m00;
    float t1 = m01;
    m00  = n00 * t0 + n10 * t1;
    m01  = n01 * t0 + n11 * t1;
    m02 += n02 * t0 + n12 * t1;

    t0 = m10;
    t1 = m11;
    m10  = n00 * t0 + n10 * t1;
    m11  = n01 * t0 + n11 * t1;
    m12 += n02 * t0 + n12 * t1;
  }


  public void apply(float n00, float n01, float n02, float n03,
                    float n10, float n11, float n12, float n13,
                    float n20, float n21, float n22, float n23,
                    float n30, float n31, float n32, float n33) {
    throw new IllegalArgumentException("Cannot use this version of apply() on a Matrix2D.");
  }


  /**
   * Apply another matrix to the left of this one.
   */
  public void preApply(Matrix2D left) {
    preApply(left.m00, left.m01, left.m02,
             left.m10, left.m11, left.m12);
  }


  public void preApply(Matrix3D left) {
    throw new IllegalArgumentException("Cannot use preApply(Matrix3D) on a Matrix2D.");
  }


  public void preApply(float n00, float n01, float n02,
                       float n10, float n11, float n12) {
    float t0 = m02;
    float t1 = m12;
    n02 += t0 * n00 + t1 * n01;
    n12 += t0 * n10 + t1 * n11;

    m02 = n02;
    m12 = n12;

    t0 = m00;
    t1 = m10;
    m00 = t0 * n00 + t1 * n01;
    m10 = t0 * n10 + t1 * n11;

    t0 = m01;
    t1 = m11;
    m01 = t0 * n00 + t1 * n01;
    m11 = t0 * n10 + t1 * n11;
  }


  public void preApply(float n00, float n01, float n02, float n03,
                       float n10, float n11, float n12, float n13,
                       float n20, float n21, float n22, float n23,
                       float n30, float n31, float n32, float n33) {
    throw new IllegalArgumentException("Cannot use this version of preApply() on a Matrix2D.");
  }


  //////////////////////////////////////////////////////////////


  /**
   * Multiply the x and y coordinates of a Vector3D against this matrix.
   */
  public Vector3D mult(Vector3D source, Vector3D target) {
    if (target == null) {
      target = new Vector3D();
    }
    target.x = m00*source.x + m01*source.y + m02;
    target.y = m10*source.x + m11*source.y + m12;
    return target;
  }


  /**
   * Multiply a two element vector against this matrix.
   * If out is null or not length four, a new float array will be returned.
   * The values for vec and out can be the same (though that's less efficient).
   */
  public float[] mult(float vec[], float out[]) {
    if (out == null || out.length != 2) {
      out = new float[2];
    }

    if (vec == out) {
      float tx = m00*vec[0] + m01*vec[1] + m02;
      float ty = m10*vec[0] + m11*vec[1] + m12;

      out[0] = tx;
      out[1] = ty;

    } else {
      out[0] = m00*vec[0] + m01*vec[1] + m02;
      out[1] = m10*vec[0] + m11*vec[1] + m12;
    }

    return out;
  }


  public float multX(float x, float y) {
    return m00*x + m01*y + m02;
  }


  public float multY(float x, float y) {
    return m10*x + m11*y + m12;
  }


  /**
   * Transpose this matrix.
   */
  public void transpose() {
  }


  /**
   * Invert this matrix. Implementation stolen from OpenJDK.
   * @return true if successful
   */
  public boolean invert() {
    float determinant = determinant();
    if (Math.abs(determinant) <= Float.MIN_VALUE) {
      return false;
    }

    float t00 = m00;
    float t01 = m01;
    float t02 = m02;
    float t10 = m10;
    float t11 = m11;
    float t12 = m12;

    m00 =  t11 / determinant;
    m10 = -t10 / determinant;
    m01 = -t01 / determinant;
    m11 =  t00 / determinant;
    m02 = (t01 * t12 - t11 * t02) / determinant;
    m12 = (t10 * t02 - t00 * t12) / determinant;

    return true;
  }


  /**
   * @return the determinant of the matrix
   */
  public float determinant() {
    return m00 * m11 - m01 * m10;
  }


  //////////////////////////////////////////////////////////////

  // TODO these need to be added as regular API, but the naming and 
  // implementation needs to be improved first. (e.g. actually keeping track
  // of whether the matrix is in fact identity internally.)

  
  protected boolean isIdentity() {
    return ((m00 == 1) && (m01 == 0) && (m02 == 0) &&
            (m10 == 0) && (m11 == 1) && (m12 == 0));
  }


  // TODO make this more efficient, or move into Matrix2D
  protected boolean isWarped() {
    return ((m00 != 1) || (m01 != 0) && 
            (m10 != 0) || (m11 != 1));
  }


  //////////////////////////////////////////////////////////////
  

  private final float sin(float angle) {
    return (float)Math.sin(angle);
  }

  private final float cos(float angle) {
    return (float)Math.cos(angle);
  }

  private final float tan(float angle) {
    return (float)Math.tan(angle);
  }
}
