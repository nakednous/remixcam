/**
 *                     ProScene (version 1.9.90)      
 *    Copyright (c) 2010-2011 by National University of Colombia
 *                 @author Jean Pierre Charalambos      
 *           http://www.disi.unal.edu.co/grupos/remixlab/
 *                           
 * This java package provides classes to ease the creation of interactive 3D
 * scenes in Processing.
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

package remixlab.remixcam.renderers;

import java.util.List;

import remixlab.remixcam.core.*;
import remixlab.remixcam.geom.*;

/**
 * probar a derivar de un renderer sin stack. Solo transformaciones vs 
 * matrices y 2d vs 3d y tratar de incluir en el las operaciones para la
 * creacion del beginScreenDrawing dentro de P2D.
 * 
 * @author pierre
 *
 */
public abstract class MatrixStackRenderer extends MatrixRenderer {
	public class MatrixStack implements Constants {  
		private static final int MATRIX_STACK_DEPTH = 32;
		
	  private static final String ERROR_PUSHMATRIX_OVERFLOW =
	    "Too many calls to pushMatrix().";
	  private static final String ERROR_PUSHMATRIX_UNDERFLOW =
	    "Too many calls to popMatrix(), and not enough to pushMatrix().";  
	  
		float[][] matrixStack = new float[MATRIX_STACK_DEPTH][16];
	  int matrixStackDepth;

	  float[][] pmatrixStack = new float[MATRIX_STACK_DEPTH][16];
	  int pmatrixStackDepth;  
	  
	  Matrix3D projection, modelview;
	  
	  public MatrixStack() {
	  	modelview = new Matrix3D();
	  	projection = new Matrix3D();
	  }
	  
	  //////////////////////////////////////////////////////////////

	  // MATRIX STACK

	  public void pushMatrix() {
	  	if (matrixStackDepth == MATRIX_STACK_DEPTH) {
	  		throw new RuntimeException(ERROR_PUSHMATRIX_OVERFLOW);
	  	}
	  	modelview.get(matrixStack[matrixStackDepth]);
	  	matrixStackDepth++;
	  }
	  

	  public void popMatrix() {
	  	if (matrixStackDepth == 0) {
	  		throw new RuntimeException(ERROR_PUSHMATRIX_UNDERFLOW);
	  	}
	  	matrixStackDepth--;
	  	modelview.set(matrixStack[matrixStackDepth]);    
	  }
	  
	  public void pushProjection() {
	  	if (pmatrixStackDepth == MATRIX_STACK_DEPTH) {
	  		throw new RuntimeException(ERROR_PUSHMATRIX_OVERFLOW);
	  	}
	  	projection.get(pmatrixStack[pmatrixStackDepth]);
	  	pmatrixStackDepth++;    
	  }

	  public void popProjection() {    
	  	if (pmatrixStackDepth == 0) {
	  		throw new RuntimeException(ERROR_PUSHMATRIX_UNDERFLOW);
	  	}
	  	pmatrixStackDepth--;
	  	projection.set(pmatrixStack[pmatrixStackDepth]);    
	  }  

	  //////////////////////////////////////////////////////////////

	  // MATRIX TRANSFORMATIONS

	  public void translate(float tx, float ty) {
	    translate(tx, ty, 0);
	  }

	  public void translate(float tx, float ty, float tz) {
	  	modelview.translate(tx, ty, tz);
	  }

	  /**
	   * Two dimensional rotation. Same as rotateZ (this is identical
	   * to a 3D rotation along the z-axis) but included for clarity --
	   * it'd be weird for people drawing 2D graphics to be using rotateZ.
	   * And they might kick our a-- for the confusion.
	   */
	  public void rotate(float angle) {
	    rotateZ(angle);
	  }

	  public void rotateX(float angle) {
	  	modelview.rotateX(angle);
	  }

	  public void rotateY(float angle) {
	  	modelview.rotateY(angle);
	  }

	  public void rotateZ(float angle) {
	  	modelview.rotateZ(angle);    
	  }

	  /**
	   * Rotate around an arbitrary vector, similar to glRotate(),
	   * except that it takes radians (instead of degrees).
	   */
	  public void rotate(float angle, float v0, float v1, float v2) {
	  	modelview.rotate(angle, v0, v1, v2);   
	  }

	  /**
	   * Same as scale(s, s, s).
	   */
	  public void scale(float s) {
	    scale(s, s, s);
	  }

	  /**
	   * Same as scale(sx, sy, 1).
	   */
	  public void scale(float sx, float sy) {
	    scale(sx, sy, 1);
	  }

	  /**
	   * Scale in three dimensions.
	   */
	  public void scale(float x, float y, float z) {
	  	modelview.scale(x, y, z);
	  }  

	  //////////////////////////////////////////////////////////////

	  // MATRIX MORE!
	  
	  public void resetMatrix() {
	  	modelview.reset();
	  }
	  
	  public void resetProjection() {
	  	projection.reset();
	  }
	  
	  public void multiplyMatrix(Matrix3D source) {
	  	applyMatrix(source);
	  }
	  
	  public void multiplyProjection(Matrix3D source) {
	  	applyProjection(source);
	  }

	  /**
	   * Apply a 4x4 transformation matrix. Same as glMultMatrix().
	   */
	  public void applyMatrix(Matrix3D source) {
	  	modelview.apply(source);
	  }
	  
	  public void applyProjection(Matrix3D source) {
	  	projection.apply(source);
	  }  
	  
	  /**
	   * 16 consecutive values that are used as the elements of a 4 x 4 column-major matrix.
	   */
	  public void applyMatrix(float [] source) {
	  	modelview.apply(source);    	
	  }
	  
	  /**
	   * 16 consecutive values that are used as the elements of a 4 x 4 column-major matrix.
	   */
	  public void applyProjection(float [] source) {
	  	projection.apply(source);    	
	  }

	  /**
	   * 16 consecutive values that are used as the elements of a 4 x 4 column-major matrix.
	   */
	  public void applyMatrix(float m0, float m1, float m2, float m3,
	                          float m4, float m5, float m6, float m7,
	                          float m8, float m9, float m10, float m11,
	                          float m12, float m13, float m14, float m15) {
	  	modelview.apply(m0, m1, m2, m3,
	                    m4, m5, m6, m7,
	                    m8, m9, m10, m11,
	                    m12, m13, m14, m15);        
	  }
	  
	  public void applyProjection(float m0, float m1, float m2, float m3,
	      float m4, float m5, float m6, float m7,
	      float m8, float m9, float m10, float m11,
	      float m12, float m13, float m14, float m15) {
	  	
	  	projection.apply(m0, m1, m2, m3,
	                     m4, m5, m6, m7,
	                     m8, m9, m10, m11,
	                     m12, m13, m14, m15);
	  }
	  
	  /**
	   * first "index" is row, second is column, e.g., n20 corresponds to the element located
	   * at the third row and first column of the matrix.
	   */
	  public void applyMatrixTransposed(float n00, float n01, float n02, float n03,
	  		                              float n10, float n11, float n12, float n13,
	  		                              float n20, float n21, float n22, float n23,
	  		                              float n30, float n31, float n32, float n33) {
	  	
	  	modelview.applyTransposed(n00, n01, n02, n03,
	  				                    n10, n11, n12, n13,
	  				                    n20, n21, n22, n23,
	  				                    n30, n31, n32, n33);
	  }
	  
	  public void applyProjectionTransposed(float n00, float n01, float n02, float n03,
	      float n10, float n11, float n12, float n13,
	      float n20, float n21, float n22, float n23,
	      float n30, float n31, float n32, float n33) {
	  	
	  	projection.applyTransposed(n00, n01, n02, n03,
	  														 n10, n11, n12, n13,
	  														 n20, n21, n22, n23,
	  														 n30, n31, n32, n33);
	  }

	  //////////////////////////////////////////////////////////////

	  // MATRIX GET/SET/PRINT

	  public Matrix3D getMatrix() {
	  	return modelview.get();        
	  }
	  
	  public Matrix3D getProjection() {
	  	return projection.get();        
	  }

	  public Matrix3D getMatrix(Matrix3D target) {
	    if (target == null)
	      target = new Matrix3D();
	    target.set(modelview);
	    return target;
	  }
	  
	  public Matrix3D getProjection(Matrix3D target) {
	    if (target == null)
	      target = new Matrix3D();
	    target.set(projection);    
	    return target;
	  }
	  
	  public void loadMatrix(Matrix3D source) {
	  	modelview.set(source);
	  }
	  
	  public void loadProjection(Matrix3D source) {
	  	projection.set(source);
	  }

	  /**
	   * Set the current transformation to the contents of the specified source.
	   */
	  public void setMatrix(Matrix3D source) {  	
	  	loadMatrix(source);
	  }
	  
	  public void setProjection(Matrix3D source) {  	
	  	loadProjection(source);
	  }
	  
	  /**
	   * Same as glFrustum().
	   */
	  // /**
	  //TODO where is this needed? Camera matrix handling is done through the camera class
	  public void frustum(float left, float right, float bottom, float top, float znear, float zfar) {  	
	  	//Same as glFrustum(), except that it wipes out (rather than
	    //multiplies against) the current perspective matrix.
	    //<P>
	    //Implementation based on the explanation in the OpenGL blue book.
	    //projection.set((2*znear)/(right-left),    0,                      (right+left)/(right-left),  0,
	    //               0,                         (2*znear)/(top-bottom), (top+bottom)/(top-bottom),  0,
	    //               0,                         0,                      -(zfar+znear)/(zfar-znear), -(2*zfar*znear)/(zfar-znear),
	    //               0,                         0,                      -1,                         0);
	    
	  	
	  	// TODO: revisar con respecto a la doc de OpenGL
	  	
	  	applyProjection((2*znear)/(right-left),    0,                         0,                            0,
	                    0,                         (2*znear)/(top-bottom),    0,                            0,
	                    (right+left)/(right-left), (top+bottom)/(top-bottom), -(zfar+znear)/(zfar-znear),  -1,
	                    0,                         0,                         -(2*zfar*znear)/(zfar-znear), 0);
	  }
	  // */

	  /**
	   * Print the current model (or "transformation") matrix.
	   */
	  public void printMatrix() {
	  	modelview.print();        
	  }
	  
	  public void printProjection() {
	  	projection.print();        
	  }
	}
	
	// ---	
	
	Matrix3D proj;
	MatrixStack mStack;

	public MatrixStackRenderer(AbstractScene scn, Drawerable dw) {
		super(scn, dw);
		mStack = new MatrixStack();
		proj = new Matrix3D();
	}
	
	/**
	 * Convenience function that simply calls {@code loadViewMatrix(true)}.
	 * 
	 * @see #loadProjectionMatrix()
	 * @see #loadProjectionMatrix(boolean)
	 * @see #loadProjectionMatrixStereo(boolean)
	 * @see #loadViewMatrix(boolean)
	 * @see #loadViewMatrixStereo(boolean)
	 */
	/**
	public void loadViewMatrix() {
		loadViewMatrix(true);
	}
	*/
	
	/*! Loads the OpenGL \c GL_MODELVIEW matrix with the modelView matrix corresponding to the Camera.

	 Calls computeModelViewMatrix() to compute the Camera's modelView matrix.

	 This method is used by QGLViewer::preDraw() (called before user's QGLViewer::draw() method) to
	 set the \c GL_MODELVIEW matrix according to the viewer's QGLViewer::camera() position() and
	 orientation().

	 As a result, the vertices used in QGLViewer::draw() can be defined in the so called world
	 coordinate system. They are multiplied by this matrix to get converted to the Camera coordinate
	 system, before getting projected using the \c GL_PROJECTION matrix (see loadProjectionMatrix()).

	 When \p reset is \c true (default), the method loads (overwrites) the \c GL_MODELVIEW matrix. Setting
	 \p reset to \c false simply calls \c glMultMatrixd (might be useful for some applications).

	 Overload this method or simply call glLoadMatrixd() at the beginning of QGLViewer::draw() if you
	 want your Camera to use an exotic modelView matrix. See also loadProjectionMatrix().

	 getModelViewMatrix() returns the 4x4 modelView matrix.

	 \attention glMatrixMode is set to \c GL_MODELVIEW

	 \attention If you use several OpenGL contexts and bypass the Qt main refresh loop, you should call
	 QGLWidget::makeCurrent() before this method in order to activate the right OpenGL context. */
	/**
	public void loadViewMatrix(boolean reset) {	 
		Matrix3D viewMat = scene.pinhole().getViewMatrix(true);
		if (reset)
	    loadMatrix(viewMat);
	  else
	    multiplyMatrix(viewMat);
	}
  // */
	
	/*! Same as loadProjectionMatrix() but for a stereo setup.

	 Only the Camera::PERSPECTIVE type() is supported for stereo mode. See
	 QGLViewer::setStereoDisplay().

	 Uses focusDistance(), IODistance(), and physicalScreenWidth() to compute cameras
	 offset and asymmetric frustums.

	 When \p leftBuffer is \c true, computes the projection matrix associated to the left eye (right eye
	 otherwise). See also loadViewMatrixStereo().

	 See the <a href="../examples/stereoViewer.html">stereoViewer</a> and the <a
	 href="../examples/contribs.html#anaglyph">anaglyph</a> examples for an illustration.

	 To retrieve this matrix, use a code like:
	 \code
	 glMatrixMode(GL_PROJECTION);
	 glPushMatrix();
	 loadProjectionMatrixStereo(left_or_right);
	 glGetFloatv(GL_PROJECTION_MATRIX, m);
	 glPopMatrix();
	 \endcode
	 Note that getProjectionMatrix() always returns the mono-vision matrix.

	 \attention glMatrixMode is set to \c GL_PROJECTION. */
	/**
	public void loadProjectionMatrixStereo(boolean leftBuffer) {
	  float left, right, bottom, top;
	  float screenHalfWidth, halfWidth, side, shift, delta;

	  resetProjection();

	  switch (scene.camera().type())  {
	    case PERSPECTIVE:
	      // compute half width of screen,
	      // corresponding to zero parallax plane to deduce decay of cameras
	      screenHalfWidth = scene.camera().focusDistance() * (float) Math.tan(scene.camera().horizontalFieldOfView() / 2.0f);
	      shift = screenHalfWidth * scene.camera().IODistance() / scene.camera().physicalScreenWidth();
	      // should be * current y  / y total
	      // to take into account that the window doesn't cover the entire screen

	      // compute half width of "view" at znear and the delta corresponding to
	      // the shifted camera to deduce what to set for asymmetric frustums
	      halfWidth = scene.camera().zNear() * (float) Math.tan(scene.camera().horizontalFieldOfView() / 2.0f);
	      delta  = shift * scene.camera().zNear() / scene.camera().focusDistance();
	      side   = leftBuffer ? -1.0f : 1.0f;

	      left   = -halfWidth + side * delta;
	      right  =  halfWidth + side * delta;
	      top    = halfWidth / scene.camera().aspectRatio();
	      bottom = -top;
	      frustum(left, right, bottom, top, scene.camera().zNear(), scene.camera().zFar());
	      break;

	    case ORTHOGRAPHIC:
	      //qWarning("Camera::setProjectionMatrixStereo: Stereo not available with Ortho mode");
	      break;
	    }
	}  
	*/

	/*! Same as loadModelViewMatrix() but for a stereo setup.

	 Only the Camera::PERSPECTIVE type() is supported for stereo mode. See
	 QGLViewer::setStereoDisplay().

	 The modelView matrix is almost identical to the mono-vision one. It is simply translated along its
	 horizontal axis by a value that depends on stereo parameters (see focusDistance(),
	 IODistance(), and physicalScreenWidth()).

	 When \p leftBuffer is \c true, computes the modelView matrix associated to the left eye (right eye
	 otherwise).

	 loadProjectionMatrixStereo() explains how to retrieve to resulting matrix.

	 See the <a href="../examples/stereoViewer.html">stereoViewer</a> and the <a
	 href="../examples/contribs.html#anaglyph">anaglyph</a> examples for an illustration.

	 \attention glMatrixMode is set to \c GL_MODELVIEW. */
	/**
	public void loadViewMatrixStereo(boolean leftBuffer) {	  
	  float halfWidth = scene.camera().focusDistance() * (float) Math.tan(scene.camera().horizontalFieldOfView() / 2.0f);
	  float shift     = halfWidth * scene.camera().IODistance() / scene.camera().physicalScreenWidth(); // * current window width / full screen width

	  Matrix3D viewMat = scene.pinhole().getViewMatrix(true);
	  
	  if (leftBuffer)
	    viewMat.mat[12] = viewMat.mat[12]-shift;
	  else
	  	viewMat.mat[12] = viewMat.mat[12]+shift;
	  loadMatrix(viewMat);
	}
	*/
	
	/**
	 * Convinience function that simply calls {@code loadProjectionMatrix(true)}.
	 * 
	 * @see #loadProjectionMatrix(boolean)
	 * @see #loadProjectionMatrixStereo(boolean)
	 * @see #loadViewMatrix()
	 * @see #loadViewMatrix(boolean)
	 * @see #loadViewMatrixStereo(boolean)
	 */
	/**
	public void loadProjectionMatrix() {
		loadProjectionMatrix(true);
	} 
	*/
	
	/**
	 * // TODO complete the doc
	 * Loads the PROJECTION matrix with the Camera projection matrix.
	 * <p>
	 * The Camera projection matrix is computed using {@link #computeProjectionMatrix()}.
	 * <p>
	 * When reset {@code true} (default), the method clears the previous projection matrix
	 * by calling {@link remixlab.remixcam.core.AbstractScene#loadIdentity()} before setting
	 * the matrix. Setting reset {@code false} is useful for SELECT mode, to combine the
	 * pushed matrix with a picking matrix. See Scene.beginSelection() (pending) for details.
	 * <p>
	 * This method is used by QGLViewer::preDraw() (called before user's QGLViewer::draw()
	 * method) to set the PROJECTION matrix according to the viewer's QGLViewer::camera()
	 * settings.
	 * <p>
	 * Use {@link #getProjectionMatrix()} to retrieve this matrix. Overload this method if
	 * you want your Camera to se an exotic projection matrix.. 
	 * <p>
	 * \attention \c glMatrixMode is set to \c GL_PROJECTION.
	 * \attention If you use several OpenGL contexts and bypass the Qt main refresh loop, you should call
	 * QGLWidget::makeCurrent() before this method in order to activate the right OpenGL context.
	 *
	 * @see #loadProjectionMatrix()
	 * @see #loadProjectionMatrixStereo(boolean)
	 * @see #loadViewMatrix()
	 * @see #loadViewMatrix(boolean)
	 * @see #loadViewMatrixStereo(boolean)
	 */
	/**
	public void loadProjectionMatrix(boolean reset) {
	  if (reset)
	  	resetProjection();
	  
	  Matrix3D projectionMat = scene.pinhole().getProjectionMatrix(true);	  
	  multiplyProjection(projectionMat);
	}	
	*/

	@Override
	protected void setProjectionMatrix() {
    resetProjection();	  
	  Matrix3D projectionMat = scene.pinhole().getProjectionMatrix(true);	  
	  multiplyProjection(projectionMat);
	}
	
	@Override
  protected void setModelViewMatrix() {
		scene.pinhole().computeViewMatrix();	  	  
	  resetMatrix();
	  scene.pinhole().computeProjectionViewMatrix();
	}
		
	// /**
	@Override
	public void beginScreenDrawing() {
		//resetProjection();
		//camera().ortho(0f, width(), height(), 0.0f, 0.0f, -1.0f);
		//multiplyProjection(camera().getProjectionMatrix());
		
		// next two same as the prv three?
		if( this.is3D() )
			((Camera) scene.pinhole()).ortho(0f, width(), height(), 0.0f, 0.0f, -1.0f);
		else {
		//TODO implement 2D case
		}
		loadProjection(scene.pinhole().getProjectionMatrix());
		pushMatrix();
		resetMatrix();
		
		/**
		if( this.is3D() )
			((Camera) scene.pinhole()).ortho(0f, width(), height(), 0.0f, 0.0f, -1.0f);
		else {
	    pushProjection();
	 		//float cameraZ = (pg2d().height/2.0f) / PApplet.tan(scene.viewWindow().fieldOfView() /2.0f);
	    //TODO check this dummy value
	    float cameraZ = (height()/2.0f) / (float) Math.tan(QUARTER_PI/2.0f);
	    float cameraNear = cameraZ / 2.0f;
	    float cameraFar = cameraZ * 2.0f;
	    //renderer().ortho(-width/2, width/2, -height/2, height/2, cameraNear, cameraFar);
	    //TODO check this line:  
	    //pg2d().projection.set((scene.viewWindow()).getOrtho(-pg2d().width/2, pg2d().width/2, -pg2d().height/2, pg2d().height/2, cameraNear, cameraFar));
	    //pg2d().projection.set((scene.viewWindow()).getOrtho(-pg2d().width/2, pg2d().width/2, -pg2d().height/2, pg2d().height/2, cameraNear, cameraFar).getTransposed(new float[16]));
	    pg2d().projection.set(get2DOrtho(-width()/2, width()/2, -height()/2, height()/2, cameraNear, cameraFar));
	    //pg2d().projection.set(get2DOrtho(0, pg2d().width, 0, pg2d().height, cameraNear, cameraFar));
	    pushMatrix();
	 	  // Camera needs to be reset!
	    //hack: it's trickier, but works ;)
	    //renderer().camera();
	    //TODO check this line:
	    //pg2d().modelview.set(((P5Camera)scene.viewWindow()).getCamera());
	    pg2d().modelview.set(get2DModelView());    
		}
		loadProjection(scene.pinhole().getProjectionMatrix());
		pushMatrix();
		resetMatrix();
		*/	
	}
	
	@Override
	public void pushMatrix() {
		mStack.pushMatrix();
	}

	@Override
	public void popMatrix() {
		mStack.popMatrix();
	}	

	@Override
	public void translate(float tx, float ty) {
		mStack.translate(tx, ty);
	}

	@Override
	public void translate(float tx, float ty, float tz) {
		mStack.translate(tx, ty, tz);
	}

	@Override
	public void rotate(float angle) {
		mStack.rotate(angle);
	}

	@Override
	public void rotateX(float angle) {
		mStack.rotateX(angle);
	}

	@Override
	public void rotateY(float angle) {
		mStack.rotateY(angle);
	}

	@Override
	public void rotateZ(float angle) {
		mStack.rotateZ(angle);
	}

	@Override
	public void rotate(float angle, float vx, float vy, float vz) {
		mStack.rotate(angle, vx, vy, vz);
	}

	@Override
	public void scale(float s) {
		mStack.scale(s);
	}

	@Override
	public void scale(float sx, float sy) {
		mStack.scale(sx, sy);
	}

	@Override
	public void scale(float x, float y, float z) {
		mStack.scale(x, y, z);
	}	

	@Override
	public void resetMatrix() {
		mStack.resetMatrix();
	}	

	@Override
	public void loadMatrix(Matrix3D source) {
		resetMatrix();
    applyMatrix(source);
	}
		
	@Override
	public void multiplyMatrix(Matrix3D source) {
		this.applyMatrix(source);
	}
	
	@Override
	public void multiplyProjection(Matrix3D source) {
		this.applyProjection(source);
	}

	@Override
	public void applyMatrix(Matrix3D source) {		
		scene.pinhole().setViewMatrix(Matrix3D.mult(scene.pinhole().getViewMatrix(), source));
	}
	
	@Override
	public void applyMatrixRowMajorOrder(float n00, float n01, float n02, float n03,
			                                 float n10, float n11, float n12, float n13,
			                                 float n20, float n21, float n22, float n23,
			                                 float n30, float n31, float n32, float n33) {
		applyMatrix(new Matrix3D(new float[] {n00, n01, n02, n03, n10, n11, n12, n13, n20, n21, n22,	n23, n30, n31, n32, n33}, true));
	}
	
	@Override
	public abstract void frustum(float left, float right, float bottom, float top,	float znear, float zfar);

	@Override
	public Matrix3D getMatrix() {
		return scene.pinhole().getViewMatrix();
	}
	
	@Override
	public Matrix3D getMatrix(Matrix3D target) {
		return scene.pinhole().getViewMatrix(target);
	}
	
	@Override
	public void setMatrix(Matrix3D source) {
		resetMatrix();
		applyMatrix(source);
	}
	
	@Override
	public void setProjection(Matrix3D source) {
		resetProjection();
		applyProjection(source);
	}

	@Override
	public void printMatrix() {
		scene.pinhole().getViewMatrix().print();
	}
	
	@Override
	public void printProjection() {
		scene.pinhole().getProjectionMatrix().print();
	}
	
	//---

	// /**
	@Override
	public abstract void cylinder(float w, float h);
	
	@Override
	public abstract void hollowCylinder(int detail, float w, float h, Vector3D m, Vector3D n);

	@Override
	public abstract void cone(int detail, float x, float y, float r, float h);

	@Override
	public abstract void cone(int detail, float x, float y, float r1, float r2, float h);

	@Override
	public abstract void drawAxis(float length);

	@Override
	public abstract void drawGrid(float size, int nbSubdivisions);
	
	@Override
	public abstract void drawDottedGrid(float size, int nbSubdivisions);

	@Override
	public abstract void drawCamera(Camera camera, boolean drawFarPlane, float scale);
	
	@Override
	public abstract void drawKFIViewport(float scale);

	@Override
	public abstract void drawZoomWindowHint();

	@Override
	public abstract void drawScreenRotateLineHint();

	@Override
	public abstract void drawArcballReferencePointHint();

	@Override
	public abstract void drawCross(float px, float py, float size);

	@Override
	public abstract void drawFilledCircle(int subdivisions, Vector3D center, float radius);

	@Override
	public abstract void drawFilledSquare(Vector3D center, float edge);

	@Override
	public abstract void drawShooterTarget(Vector3D center, float length);

	@Override
	public abstract void drawPath(List<GeomFrame> path, int mask, int nbFrames, int nbSteps, float scale);
	// */

	@Override
	public void pushProjection() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void popProjection() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resetProjection() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadProjection(Matrix3D source) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void applyProjection(Matrix3D source) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void applyProjectionRowMajorOrder(float n00, float n01, float n02,
			float n03, float n10, float n11, float n12, float n13, float n20,
			float n21, float n22, float n23, float n30, float n31, float n32,
			float n33) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Matrix3D getProjection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Matrix3D getProjection(Matrix3D target) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public abstract void drawViewWindow(ViewWindow camera, float scale);			
}
