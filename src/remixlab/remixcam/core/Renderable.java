package remixlab.remixcam.core;

import remixlab.remixcam.geom.*;

public interface Renderable extends Drawerable {
	//public void setScene(AbstractScene scn);
	
	public AbstractScene scene();
	
	public boolean is2D();
	
	public boolean is3D();
	
	public void bindMatrices();
	
	/**
	 * Computes the world coordinates of an screen object so that drawing can be
	 * done directly with 2D screen coordinates.
	 * <p>
	 * All screen drawing should be enclosed between {@link #beginScreenDrawing()}
	 * and {@link #endScreenDrawing()}. Then you can just begin drawing your
	 * screen shapes (defined between {@code PApplet.beginShape()} and {@code
	 * PApplet.endShape()}).
	 * <p>
	 * <b>Note:</b> To specify a {@code (x,y)} vertex screen coordinate you should 
	 * first call {@code Vector3D p = coords(new Point(x, y))} then do your
	 * drawing as {@code vertex(p.x, p.y, p.z)}.
	 * <p>
	 * <b>Attention:</b> If you want your screen drawing to appear on top of your
	 * 3d scene then draw first all your 3d before doing any call to a 
	 * {@link #beginScreenDrawing()} and {@link #endScreenDrawing()} pair.  
	 * 
	 * @see #endScreenDrawing()	 
	 */
  public void beginScreenDrawing();
	
  /**
	 * Ends screen drawing. See {@link #beginScreenDrawing()} for details.
	 * 
	 * @see #beginScreenDrawing()
	 */
	public void endScreenDrawing();
	
	// matrix
	/**
	 * Push a copy of the modelview matrix onto the stack.
   */
	public void pushMatrix();
	
	/**
	 * Replace the current modelview matrix with the top of the stack.
	 */
	public void popMatrix();
	
	/**
	 * Push a copy of the projection matrix onto the stack.
   */
	public void pushProjection();
	
	/**
	 * Replace the current projection matrix with the top of the stack.
	 */
	public void popProjection();
	
	/**
   * Translate in X and Y.
   */
  public void translate(float tx, float ty);
  
  /**
   * Translate in X, Y, and Z.
   */
  public void translate(float tx, float ty, float tz);
  
  /**
  * Two dimensional rotation.
  *
  * Same as rotateZ (this is identical to a 3D rotation along the z-axis)
  * but included for clarity. It'd be weird for people drawing 2D graphics
  * to be using rotateZ. And they might kick our a-- for the confusion.
  *
  * <A HREF="http://www.xkcd.com/c184.html">Additional background</A>.
  */
  public void rotate(float angle);
  public void rotateX(float angle);
  public void rotateY(float angle);
  
  /**
   * Rotate around the Z axis.
   *
   * The functions rotate() and rotateZ() are identical, it's just that it make
   * sense to have rotate() and then rotateX() and rotateY() when using 3D;
   * nor does it make sense to use a function called rotateZ() if you're only
   * doing things in 2D. so we just decided to have them both be the same.
   */
  public void rotateZ(float angle);
    
  /**
   * Rotate about a vector in space. Same as the glRotatef() function.
   */
  public void rotate(float angle, float vx, float vy, float vz);
  
  /**
   * Scale in all dimensions.
   */
  public void scale(float s);
  
  /**
   * Scale in X and Y. Equivalent to scale(sx, sy, 1).
   *
   * Not recommended for use in 3D, because the z-dimension is just
   * scaled by 1, since there's no way to know what else to scale it by.
   */
  public void scale(float sx, float sy);
  
  /**
   * Scale in X, Y, and Z.
   */
  public void scale(float x, float y, float z);
  
  /**
   * Set the current modelview matrix to identity.
   */
  public void resetMatrix();
  
  /**
   * Set the current projection matrix to identity.
   */
  public void resetProjection();
  
  //public void loadMatrix(Matrix3D source);
  //public void loadProjection(Matrix3D source);
  //public void multiplyMatrix(Matrix3D source);
  //public void multiplyProjection(Matrix3D source);
  public void applyMatrix(DLMatrix source);
  public void applyProjection(DLMatrix source);
  
  /**
   * Apply a 4x4 modelview matrix.
   */
  public void applyMatrixRowMajorOrder(float n00, float n01, float n02, float n03,
                                       float n10, float n11, float n12, float n13,
                                       float n20, float n21, float n22, float n23,
                                       float n30, float n31, float n32, float n33);
  
  /**
   * Apply a 4x4 projection matrix.
   */
  public void applyProjectionRowMajorOrder(float n00, float n01, float n02, float n03,
                                           float n10, float n11, float n12, float n13,
                                           float n20, float n21, float n22, float n23,
                                           float n30, float n31, float n32, float n33);
  
  //public void frustum(float left, float right, float bottom, float top, float znear, float zfar);
  
  public DLMatrix getMatrix();
  
  /**
   * Copy the current modelview matrix into the specified target.
   * Pass in null to create a new matrix.
   */
  public DLMatrix getMatrix(DLMatrix target);
  
  public DLMatrix getProjection();
  
  /**
   * Copy the current projection matrix into the specified target.
   * Pass in null to create a new matrix.
   */
  public DLMatrix getProjection(DLMatrix target);
  
  /**
   * Set the current modelview matrix to the contents of another.
   */
  public void setMatrix(DLMatrix source);
  
  /**
   * Print the current modelview matrix.
   */
  public void printMatrix();
  
  /**
   * Set the current projection matrix to the contents of another.
   */
  public void setProjection(DLMatrix source);
  
  /**
   * Print the current projection matrix.
   */
  public void printProjection();  
  
  
  //TODO testing this two (this is all what is new in the approach: remixlab.remixcam.renderers)
  
  //public void ortho(float left, float right, float bottom, float top, float near, float far);
  
  //public void perspective(float fov, float aspect, float zNear, float zFar);
}
