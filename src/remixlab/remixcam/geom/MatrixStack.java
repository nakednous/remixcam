package remixlab.remixcam.geom;

import remixlab.remixcam.core.*;

/**
 * This class has been almost entirely taken from Processing.
 * 
 * @author pierre
 */
public class MatrixStack {
	
  // types of transformation matrices
  
  private static final int PROJECTION = 0;
  private static final int MODELVIEW  = 1;
  
	private static final int MATRIX_STACK_DEPTH = 32;
	
  private static final String ERROR_PUSHMATRIX_OVERFLOW =
    "Too many calls to pushMatrix().";
  private static final String ERROR_PUSHMATRIX_UNDERFLOW =
    "Too many calls to popMatrix(), and not enough to pushMatrix().";
  
  
	float[][] matrixStack = new float[MATRIX_STACK_DEPTH][16];
  int matrixStackDepth;

  protected int matrixMode = MODELVIEW;
  float[][] pmatrixStack = new float[MATRIX_STACK_DEPTH][16];
  int pmatrixStackDepth;
  
  Camera camera;
  Matrix3D projection, modelview;
  
  public MatrixStack(Camera cam) {
  	camera = cam;
  	projection = camera.projection();
  	modelview = camera.modelview();
  }
  
  //////////////////////////////////////////////////////////////

  // MATRIX STACK

  public void pushMatrix() {
    if (matrixMode == PROJECTION) {
      if (pmatrixStackDepth == MATRIX_STACK_DEPTH) {
        throw new RuntimeException(ERROR_PUSHMATRIX_OVERFLOW);
      }
      projection.get(pmatrixStack[pmatrixStackDepth]);
      pmatrixStackDepth++;      
    } else {    
      if (matrixStackDepth == MATRIX_STACK_DEPTH) {
        throw new RuntimeException(ERROR_PUSHMATRIX_OVERFLOW);
      }
      modelview.get(matrixStack[matrixStackDepth]);
      matrixStackDepth++;
    }
  }


  public void popMatrix() {
    if (matrixMode == PROJECTION) {
      if (pmatrixStackDepth == 0) {
        throw new RuntimeException(ERROR_PUSHMATRIX_UNDERFLOW);
      }
      pmatrixStackDepth--;
      projection.set(pmatrixStack[pmatrixStackDepth]);
    } else {      
      if (matrixStackDepth == 0) {
        throw new RuntimeException(ERROR_PUSHMATRIX_UNDERFLOW);
      }
      matrixStackDepth--;
      modelview.set(matrixStack[matrixStackDepth]);
    }
  }


  //////////////////////////////////////////////////////////////

  // MATRIX TRANSFORMATIONS


  public void translate(float tx, float ty) {
    translate(tx, ty, 0);
  }


  public void translate(float tx, float ty, float tz) {
    if (matrixMode == PROJECTION) {
      projection.translate(tx, ty, tz);
    } else {    	
      modelview.translate(tx, ty, tz);
    }
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
    if (matrixMode == PROJECTION) {
      projection.rotateX(angle);
    } else { 
      modelview.rotateX(angle);
    }
  }


  public void rotateY(float angle) {
    if (matrixMode == PROJECTION) {
      projection.rotateY(angle);
    } else {    
      modelview.rotateY(angle);
    }
  }


  public void rotateZ(float angle) {
    if (matrixMode == PROJECTION) {
      projection.rotateZ(angle);
    } else {    
      modelview.rotateZ(angle);
    }    
  }


  /**
   * Rotate around an arbitrary vector, similar to glRotate(),
   * except that it takes radians (instead of degrees).
   */
  public void rotate(float angle, float v0, float v1, float v2) {
    if (matrixMode == PROJECTION) {
      projection.rotate(angle, v0, v1, v2);
    } else {    
      modelview.rotate(angle, v0, v1, v2);
    }
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
    if (matrixMode == PROJECTION) {
      projection.scale(x, y, z);
    } else {    
      modelview.scale(x, y, z);
    }
  }
  
  
  public void shearX(float angle) {
    float t = (float) Math.tan(angle);
    applyMatrix(1, t, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1);
  }


  public void shearY(float angle) {
    float t = (float) Math.tan(angle);
    applyMatrix(1, 0, 0, 0,
                t, 1, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1);
  }  
  


  //////////////////////////////////////////////////////////////

  // MATRIX MORE!


  public void resetMatrix() {
    if (matrixMode == PROJECTION) {
      projection.reset();
    } else {        
      modelview.reset();
    }
  }



  public void applyMatrix(float n00, float n01, float n02,
                          float n10, float n11, float n12) {
    applyMatrix(n00, n01, n02, 0,
                n10, n11, n12, 0,
                0,   0,   1,   0,
                0,   0,   0,   1);
  }


  public void applyMatrix(Matrix3D source) {
    applyMatrix(source.mat[0], source.mat[4], source.mat[8], source.mat[12],
                source.mat[1], source.mat[5], source.mat[9], source.mat[13],
                source.mat[2], source.mat[6], source.mat[10], source.mat[14],
                source.mat[3], source.mat[7], source.mat[11], source.mat[15]);
  }


  /**
   * Apply a 4x4 transformation matrix. Same as glMultMatrix().
   * This call will be slow because it will try to calculate the
   * inverse of the transform. So avoid it whenever possible.
   */
  public void applyMatrix(float n00, float n01, float n02, float n03,
                          float n10, float n11, float n12, float n13,
                          float n20, float n21, float n22, float n23,
                          float n30, float n31, float n32, float n33) {
    if (matrixMode == PROJECTION) {
      projection.apply(n00, n01, n02, n03,
                       n10, n11, n12, n13,
                       n20, n21, n22, n23,
                       n30, n31, n32, n33);
    } else {
      modelview.apply(n00, n01, n02, n03,
                      n10, n11, n12, n13,
                      n20, n21, n22, n23,
                      n30, n31, n32, n33);
    }
  }



  //////////////////////////////////////////////////////////////

  // MATRIX GET/SET/PRINT


  public Matrix3D getMatrix() {
    if (matrixMode == PROJECTION) {
      return projection.get();
    } else {
      return modelview.get();  
    }    
  }


  //public Matrix2D getMatrix(Matrix2D target)


  public Matrix3D getMatrix(Matrix3D target) {
    if (target == null) {
      target = new Matrix3D();
    }
    if (matrixMode == PROJECTION) {
      target.set(projection);
    } else {
      target.set(modelview);
    }
    return target;
  }


  //public void setMatrix(Matrix source)  


  /**
   * Set the current transformation to the contents of the specified source.
   */
  public void setMatrix(Matrix3D source) {
    // not efficient, but at least handles the inverse stuff.
    resetMatrix();
    applyMatrix(source);
  }


  /**
   * Print the current model (or "transformation") matrix.
   */
  public void printMatrix() {
    if (matrixMode == PROJECTION) {
    	// TODO implement me!
      //projection.print();
    } else {
    	// TODO implement me!
      //modelview.print();  
    }    
  }
  
  public void matrixMode(int mode) {    
    if (mode == PROJECTION) {
      matrixMode = PROJECTION;
    } else if (matrixMode == MODELVIEW) {
      matrixMode = MODELVIEW;
    } else {
      System.out.println("Invalid matrix mode. Use PROJECTION or MODELVIEW");
    }
  }
  
}
