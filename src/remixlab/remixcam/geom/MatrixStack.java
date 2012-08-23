package remixlab.remixcam.geom;

import remixlab.remixcam.core.*;

/**
 * This class has been almost entirely taken from Processing.
 * 
 * @author pierre
 */
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
   * 16 consecutive values that are used as the elements of a 4 × 4 column-major matrix.
   */
  public void applyMatrix(float [] source) {
  	modelview.apply(source);    	
  }
  
  /**
   * 16 consecutive values that are used as the elements of a 4 × 4 column-major matrix.
   */
  public void applyProjection(float [] source) {
  	projection.apply(source);    	
  }

  /**
   * 16 consecutive values that are used as the elements of a 4 × 4 column-major matrix.
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
