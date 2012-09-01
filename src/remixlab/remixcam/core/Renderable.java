package remixlab.remixcam.core;

import java.util.List;

import remixlab.remixcam.geom.*;

public interface Renderable {
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
  
  public void loadMatrix(Matrix3D source);
  public void loadProjection(Matrix3D source);
  public void multiplyMatrix(Matrix3D source);
  public void multiplyProjection(Matrix3D source);
  public void applyMatrix(Matrix3D source);
  public void applyProjection(Matrix3D source);
  
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
  
  public void frustum(float left, float right, float bottom, float top, float znear, float zfar);
  
  public Matrix3D getMatrix();
  
  /**
   * Copy the current modelview matrix into the specified target.
   * Pass in null to create a new matrix.
   */
  public Matrix3D getMatrix(Matrix3D target);
  
  public Matrix3D getProjection();
  
  /**
   * Copy the current projection matrix into the specified target.
   * Pass in null to create a new matrix.
   */
  public Matrix3D getProjection(Matrix3D target);
  
  /**
   * Set the current modelview matrix to the contents of another.
   */
  public void setMatrix(Matrix3D source);
  
  /**
   * Print the current modelview matrix.
   */
  public void printMatrix();
  
  /**
   * Set the current projection matrix to the contents of another.
   */
  public void setProjection(Matrix3D source);
  
  /**
   * Print the current projection matrix.
   */
  public void printProjection();  
  
  //drawing
  
  // 3D
  
  /**
	 * Draws a cylinder of width {@code w} and height {@code h}, along the 
	 * positive {@code z} axis. 
	 */
  public void cylinder(float w, float h);
  
  /**
	 * Draws a cone along the positive {@code z} axis, with its base centered
	 * at {@code (x,y)}, height {@code h}, and radius {@code r}. 
	 * 
	 * @see #cone(int, float, float, float, float, float)
	 */
  public void cone(int detail, float x, float y, float r, float h);
  
  /**
	 * Draws a truncated cone along the positive {@code z} axis,
	 * with its base centered at {@code (x,y)}, height {@code h}, and radii
	 * {@code r1} and {@code r2} (basis and height respectively).
	 * 
	 * @see #cone(int, float, float, float, float)
	 */
  public void cone(int detail, float x, float y, float r1, float r2, float h);
  
  // 2D
  
  /**
	 * Draws an axis of length {@code length} which origin correspond to the
	 * world coordinate system origin.
	 * 
	 * @see #drawGrid(float, int)
	 */
	public void drawAxis(float length);
	
	/**
	 * Draws a grid in the XY plane, centered on (0,0,0) (defined in the current
	 * coordinate system).
	 * <p>
	 * {@code size} and {@code nbSubdivisions} define its geometry.
	 * 
	 * @see #drawAxis(float)
	 */
	public void drawGrid(float size, int nbSubdivisions);
	
	public void drawDottedGrid(float size, int nbSubdivisions);
	
	//TODO pend
	//public void drawViewPort(ViewPort camera, float scale);
	
	/**
	 * Draws a representation of the {@code camera} in the 3D virtual world.
	 * <p>
	 * The near and far planes are drawn as quads, the frustum is drawn using
	 * lines and the camera up vector is represented by an arrow to disambiguate
	 * the drawing.
	 * <p>
	 * When {@code drawFarPlane} is {@code false}, only the near plane is drawn.
	 * {@code scale} can be used to scale the drawing: a value of 1.0 (default)
	 * will draw the Camera's frustum at its actual size.
	 * <p>
	 * <b>Note:</b> The drawing of a Scene's own Scene.camera() should not be
	 * visible, but may create artifacts due to numerical imprecisions.
	 */
	public void drawCamera(Camera camera, boolean drawFarPlane, float scale);
	
	public void drawViewWindow(ViewWindow window, float scale);	
	
	public void drawKFIViewport(float scale);
	
	/**
	 * Draws a rectangle on the screen showing the region where a zoom operation
	 * is taking place.
	 */	
	public void drawZoomWindowHint();
	
	/**
	 * Draws visual hint (a line on the screen) when a screen rotation is taking
	 * place.
	 */
	public void drawScreenRotateLineHint();
	
	/**
	 * Draws visual hint (a cross on the screen) when the
	 * {@link #arcballReferencePoint()} is being set.
	 * <p>
	 * Simply calls {@link #drawCross(float, float)} on {@code
	 * camera().projectedCoordinatesOf(arcballReferencePoint())} {@code x} and
	 * {@code y} coordinates.
	 * 
	 * @see #drawCross(float, float)
	 */	
	public void drawArcballReferencePointHint();
	
	/**
	 * Draws a cross on the screen centered under pixel {@code (px, py)}, and edge
	 * of size {@code size}.
	 * 
	 * @see #drawArcballReferencePointHint()
	 */
	public void drawCross(float px, float py, float size);
	
	/**
	 * Draws a filled circle using screen coordinates.
	 * 
	 * @param subdivisions
	 *          Number of triangles approximating the circle. 
	 * @param center
	 *          Circle screen center.
	 * @param radius
	 *          Circle screen radius.
	 */	
	public void drawFilledCircle(int subdivisions, Vector3D center, float radius);
	
	/**
	 * Draws a filled square using screen coordinates.
	 * 
	 * @param center
	 *          Square screen center.
	 * @param edge
	 *          Square edge length.
	 */
	public void drawFilledSquare(Vector3D center, float edge);
	
	/**
	 * Draws the classical shooter target on the screen.
	 * 
	 * @param center
	 *          Center of the target on the screen
	 * @param length
	 *          Length of the target in pixels
	 */
	public void drawShooterTarget(Vector3D center, float length);
		
	public void drawPath(List<SimpleFrame> path, int mask, int nbFrames, int nbSteps, float scale);	
}
