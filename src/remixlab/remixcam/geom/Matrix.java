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
 * This class has been almost entirely taken from Processing. It adds stuff to
 * convert between similar geometric types.
 * 
 * @author pierre
 */
public interface Matrix {
  
  public void reset();
  
  /**
   * Returns a copy of this Matrix.
   */
  public Matrix get();  

  /**
   * Copies the matrix contents into a float array.
   * If target is null (or not the correct size), a new array will be created.
   */
  public float[] get(float[] target);
  
  
  public void set(Matrix src);

  public void set(float[] source);

  public void set(float m00, float m01, float m02, 
                  float m10, float m11, float m12);

  public void set(float m00, float m01, float m02, float m03,
                  float m10, float m11, float m12, float m13,
                  float m20, float m21, float m22, float m23,
                  float m30, float m31, float m32, float m33);

  
  public void translate(float tx, float ty);
  
  public void translate(float tx, float ty, float tz);

  public void rotate(float angle);

  public void rotateX(float angle);

  public void rotateY(float angle);

  public void rotateZ(float angle);

  public void rotate(float angle, float v0, float v1, float v2);

  public void scale(float s);

  public void scale(float sx, float sy);

  public void scale(float x, float y, float z);
  
  public void shearX(float angle);
  
  public void shearY(float angle);

  /** 
   * Multiply this matrix by another.
   */
  public void apply(Matrix source);

  public void apply(Matrix2D source);

  public void apply(Matrix3D source);

  public void apply(float n00, float n01, float n02, 
                    float n10, float n11, float n12);

  public void apply(float n00, float n01, float n02, float n03,
                    float n10, float n11, float n12, float n13,
                    float n20, float n21, float n22, float n23,
                    float n30, float n31, float n32, float n33);

  /**
   * Apply another matrix to the left of this one.
   */
  public void preApply(Matrix2D left);

  public void preApply(Matrix3D left);

  public void preApply(float n00, float n01, float n02, 
                       float n10, float n11, float n12);

  public void preApply(float n00, float n01, float n02, float n03,
                       float n10, float n11, float n12, float n13,
                       float n20, float n21, float n22, float n23,
                       float n30, float n31, float n32, float n33);

  
  /** 
   * Multiply a Vector3D by this matrix. 
   */
  public Vector3D mult(Vector3D source, Vector3D target);
  
  
  /** 
   * Multiply a multi-element vector against this matrix. 
   */
  public float[] mult(float[] source, float[] target);
  
  
//  public float multX(float x, float y);
//  public float multY(float x, float y);
  
//  public float multX(float x, float y, float z);
//  public float multY(float x, float y, float z);
//  public float multZ(float x, float y, float z);  
  
  
  /**
   * Transpose this matrix.
   */
  public void transpose();

  
  /**
   * Invert this matrix.
   * @return true if successful
   */
  public boolean invert();
  
  
  /**
   * @return the determinant of the matrix
   */
  public float determinant();
}