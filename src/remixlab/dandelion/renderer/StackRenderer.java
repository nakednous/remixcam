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

package remixlab.dandelion.renderer;

import remixlab.dandelion.core.*;
import remixlab.dandelion.geom.*;

public abstract class StackRenderer extends Renderer {	
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
	  
	  Mat projection, modelview;
	  
	  public MatrixStack() {
	  	modelview = new Mat();
	  	projection = new Mat();
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

	  /**
	   * Apply a 4x4 transformation matrix. Same as glMultMatrix().
	   */
	  public void applyMatrix(Mat source) {
	  	modelview.apply(source);
	  }
	  
	  public void applyProjection(Mat source) {
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

	  public Mat getMatrix() {
	  	return modelview.get();        
	  }
	  
	  public Mat getProjection() {
	  	return projection.get();        
	  }

	  public Mat getMatrix(Mat target) {
	    if (target == null)
	      target = new Mat();
	    target.set(modelview);
	    return target;
	  }
	  
	  public Mat getProjection(Mat target) {
	    if (target == null)
	      target = new Mat();
	    target.set(projection);    
	    return target;
	  }
	  
	  /**
	  public void setMatrix(Matrix3D source) {  	
	  	modelview.set(source);
	  }
	  
	  public void setProjection(Matrix3D source) {
	  	projection.set(source);
	  }
	  */
	  
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
	
	protected MatrixStack mStack;

	public StackRenderer(AbstractScene scn, Depictable dw) {
		super(scn, dw);
		mStack = new MatrixStack();
	}
	
	//__
	
	@Override
	public void pushMatrix() {
		mStack.pushMatrix();
	}
	
	@Override
	public void popMatrix() {
		mStack.popMatrix();
	}
	
	@Override
	public void resetMatrix() {
		mStack.resetMatrix();
	}
	
	@Override
	public Mat getMatrix() {
		return mStack.getMatrix();
  }
  
	@Override
  public Mat getMatrix(Mat target) {
		return mStack.getMatrix(target);
  }
	
	//TODO implement!
	/**
	@Override
  public void setMatrix(Matrix3D source) {
		mStack.setMatrix(source);
  }
  */
	
	@Override
  public void printMatrix() {
		mStack.printMatrix();  	
  }
	
	@Override
	public void applyMatrix(Mat source) {
		mStack.applyMatrix(source);
	}
	
	//TODO implement
	/**
	@Override
	public void applyMatrixRowMajorOrder(float n00, float n01, float n02, float n03,
      float n10, float n11, float n12, float n13,
      float n20, float n21, float n22, float n23,
      float n30, float n31, float n32, float n33) {
		mStack.applyMatrixRowMajorOrder(n00, n01, n02, n03, n10, n11, n12, n13, n20, n21, n22, n23, n30, n31, n32, n33);
	}
	*/
	
	@Override
	public void pushProjection() {
		mStack.pushProjection();
	}
	
	@Override
	public void popProjection() {
		mStack.popProjection();
	}	
	
	@Override
	public void resetProjection() {
		mStack.resetProjection();
	}
	
	@Override
  public Mat getProjection() {
		return mStack.getProjection();
	}
  
	@Override
  public Mat getProjection(Mat target) {
		return mStack.getProjection(target);
	}
  
	//TODO implement me
	/**
	@Override
  public void setProjection(Matrix3D source) {
		mStack.setProjection(source);
	}
	*/
  
	@Override
  public void printProjection() {
		mStack.printProjection();
	}
	
	@Override
	public void applyProjection(Mat source) {
		mStack.applyProjection(source);
	}
  
	//TODO implement me
	/**
  @Override
	public void applyProjectionRowMajorOrder(float n00, float n01, float n02,
			float n03, float n10, float n11, float n12, float n13, float n20,
			float n21, float n22, float n23, float n30, float n31, float n32,
			float n33) {
		mStack.applyProjectionRowMajorOrder
	}
	*/
	
	//
	
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
	
	//--
	
	@Override
	public void bindMatrices() {
		setProjectionMatrix();
		setModelViewMatrix();
		scene.pinhole().cacheProjViewInvMat();
	}
	
	@Override
	protected void setProjectionMatrix() {
    resetProjection();	  
	  Mat projectionMat = scene.pinhole().getProjectionMatrix(true);	  
	  applyProjection(projectionMat);
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
		setProjection(scene.pinhole().getProjectionMatrix());
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
	public void endScreenDrawing() {
		popProjection();  
		popMatrix();
	}
}
