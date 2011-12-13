package remixlab.remixcam.geom;

/**
 * 4x4 matrix implementation.
 * <p>
 * This class has been almost entirely taken from Processing.
 * 
 * @author pierre
 */
public class Matrix3D {

	public float mat [] = new float[16];
	

  // locally allocated version to avoid creating new memory
  protected Matrix3D inverseCopy;


  public Matrix3D() {
    reset();
  }


  public Matrix3D(float _m00, float _m01, float _m02,
                  float _m10, float _m11, float _m12) {
    set(_m00, _m01, _m02, 0,
        _m10, _m11, _m12, 0,
        0,   0,   1,   0,
        0,   0,   0,   1);
  }


  public Matrix3D(float _m00, float _m01, float _m02, float _m03,
                  float _m10, float _m11, float _m12, float _m13,
                  float _m20, float _m21, float _m22, float _m23,
                  float _m30, float _m31, float _m32, float _m33) {
    set(_m00, _m01, _m02, _m03,
        _m10, _m11, _m12, _m13,
        _m20, _m21, _m22, _m23,
        _m30, _m31, _m32, _m33);
  }


  public Matrix3D(Matrix3D matrix) {
    set(matrix);
  } 
  
  
  public final float[][] get3x3UpperLeftMatrixFromMatrix3D() {
  	return get3x3UpperLeftMatrixFromMatrix3D(this);
  }
  
  /**
	 * Utility function that returns the 3x3 upper left sub-matrix of the given
	 * PMatrix3D.
	 */
	public static final float[][] get3x3UpperLeftMatrixFromMatrix3D(Matrix3D pM) {
		float[][] m = new float[3][3];
		m[0][0] = pM.mat[0];
		m[0][1] = pM.mat[1];
		m[0][2] = pM.mat[2];
		m[1][0] = pM.mat[4];
		m[1][1] = pM.mat[5];
		m[1][2] = pM.mat[6];
		m[2][0] = pM.mat[8];
		m[2][1] = pM.mat[9];
		m[2][2] = pM.mat[10];
		return m;
	}

  public void reset() {
    set(1, 0, 0, 0,
        0, 1, 0, 0,
        0, 0, 1, 0,
        0, 0, 0, 1);
  }


  /**
   * Returns a copy of this Matrix.
   */
  public Matrix3D get() {
    Matrix3D outgoing = new Matrix3D();
    outgoing.set(this);
    return outgoing;
  }


  /**
   * Copies the matrix contents into a 16 entry float array.
   * If target is null (or not the correct size), a new array will be created.
   */
  public float[] get(float[] target) {
    if ((target == null) || (target.length != 16)) {
      target = new float[16];
    }
    target[0] = mat[0];
    target[1] = mat[1];
    target[2] = mat[2];
    target[3] = mat[3];

    target[4] = mat[4];
    target[5] = mat[5];
    target[6] = mat[6];
    target[7] = mat[7];

    target[8] = mat[8];
    target[9] = mat[9];
    target[10] = mat[10];
    target[11] = mat[11];

    target[12] = mat[12];
    target[13] = mat[13];
    target[14] = mat[14];
    target[15] = mat[15];

    return target;
  }


  public void set(Matrix3D src) {     
      set(src.mat[0], src.mat[1], src.mat[2], src.mat[3],
          src.mat[4], src.mat[5], src.mat[6], src.mat[7],
          src.mat[8], src.mat[9], src.mat[10], src.mat[11],
          src.mat[12], src.mat[13], src.mat[14], src.mat[15]);
    
  }


  public void set(float[] source) {
    if (source.length == 6) {
      set(source[0], source[1], source[2],
          source[3], source[4], source[5]);

    } else if (source.length == 16) {
      mat[0] = source[0];
      mat[1] = source[1];
      mat[2] = source[2];
      mat[3] = source[3];

      mat[4] = source[4];
      mat[5] = source[5];
      mat[6] = source[6];
      mat[7] = source[7];

      mat[8] = source[8];
      mat[9] = source[9];
      mat[10] = source[10];
      mat[11] = source[11];

      mat[12] = source[12];
      mat[13] = source[13];
      mat[14] = source[14];
      mat[15] = source[15];
    }
  }


  public void set(float _m00, float _m01, float _m02,
                  float _m10, float _m11, float _m12) {
    set(_m00, _m01, 0, _m02,
        _m10, _m11, 0, _m12,
        0, 0, 1, 0,
        0, 0, 0, 1);
  }


  public void set(float _m00, float _m01, float _m02, float _m03,
                  float _m10, float _m11, float _m12, float _m13,
                  float _m20, float _m21, float _m22, float _m23,
                  float _m30, float _m31, float _m32, float _m33) {
    this.mat[0] = _m00; this.mat[1] = _m01; this.mat[2] = _m02; this.mat[3] = _m03;
    this.mat[4] = _m10; this.mat[5] = _m11; this.mat[6] = _m12; this.mat[7] = _m13;
    this.mat[8] = _m20; this.mat[9] = _m21; this.mat[10] = _m22; this.mat[11] = _m23;
    this.mat[12] = _m30; this.mat[13] = _m31; this.mat[14] = _m32; this.mat[15] = _m33;
  }


  public void translate(float tx, float ty) {
    translate(tx, ty, 0);
  }

//  public void invTranslate(float tx, float ty) {
//    invTranslate(tx, ty, 0);
//  }


  public void translate(float tx, float ty, float tz) {
    mat[3] += tx*mat[0] + ty*mat[1] + tz*mat[2];
    mat[7] += tx*mat[4] + ty*mat[5] + tz*mat[6];
    mat[11] += tx*mat[8] + ty*mat[9] + tz*mat[10];
    mat[15] += tx*mat[12] + ty*mat[13] + tz*mat[14];
  }


  public void rotate(float angle) {
    rotateZ(angle);
  }


  public void rotateX(float angle) {
    float c = cos(angle);
    float s = sin(angle);
    apply(1, 0, 0, 0,  0, c, -s, 0,  0, s, c, 0,  0, 0, 0, 1);
  }


  public void rotateY(float angle) {
    float c = cos(angle);
    float s = sin(angle);
    apply(c, 0, s, 0,  0, 1, 0, 0,  -s, 0, c, 0,  0, 0, 0, 1);
  }


  public void rotateZ(float angle) {
    float c = cos(angle);
    float s = sin(angle);
    apply(c, -s, 0, 0,  s, c, 0, 0,  0, 0, 1, 0,  0, 0, 0, 1);
  }


  public void rotate(float angle, float v0, float v1, float v2) {
    // TODO should make sure this vector is normalized

    float c = cos(angle);
    float s = sin(angle);
    float t = 1.0f - c;

    apply((t*v0*v0) + c, (t*v0*v1) - (s*v2), (t*v0*v2) + (s*v1), 0,
          (t*v0*v1) + (s*v2), (t*v1*v1) + c, (t*v1*v2) - (s*v0), 0,
          (t*v0*v2) - (s*v1), (t*v1*v2) + (s*v0), (t*v2*v2) + c, 0,
          0, 0, 0, 1);
  }


  public void scale(float s) {
    //apply(s, 0, 0, 0,  0, s, 0, 0,  0, 0, s, 0,  0, 0, 0, 1);
    scale(s, s, s);
  }


  public void scale(float sx, float sy) {
    //apply(sx, 0, 0, 0,  0, sy, 0, 0,  0, 0, 1, 0,  0, 0, 0, 1);
    scale(sx, sy, 1);
  }


  public void scale(float x, float y, float z) {
    //apply(x, 0, 0, 0,  0, y, 0, 0,  0, 0, z, 0,  0, 0, 0, 1);
    mat[0] *= x;  mat[1] *= y;  mat[2] *= z;
    mat[4] *= x;  mat[5] *= y;  mat[6] *= z;
    mat[8] *= x;  mat[9] *= y;  mat[10] *= z;
    mat[12] *= x;  mat[13] *= y;  mat[14] *= z;
  }


  public void shearX(float angle) {
    float t = (float) Math.tan(angle);
    apply(1, t, 0, 0,
          0, 1, 0, 0,
          0, 0, 1, 0,
          0, 0, 0, 1);
  }


  public void shearY(float angle) {
    float t = (float) Math.tan(angle);
    apply(1, 0, 0, 0,
          t, 1, 0, 0,
          0, 0, 1, 0,
          0, 0, 0, 1);
  }

  public void apply(Matrix3D source) {
    apply(source.mat[0], source.mat[1], source.mat[2], source.mat[3],
          source.mat[4], source.mat[5], source.mat[6], source.mat[7],
          source.mat[8], source.mat[9], source.mat[10], source.mat[11],
          source.mat[12], source.mat[13], source.mat[14], source.mat[15]);
  }


  public void apply(float n00, float n01, float n02,
                    float n10, float n11, float n12) {
    apply(n00, n01, 0, n02,
          n10, n11, 0, n12,
          0, 0, 1, 0,
          0, 0, 0, 1);
  }
  
  public static void mult(Matrix3D a, Matrix3D b, Matrix3D c) { 
  	c.mat[0] = a.mat[0]*b.mat[0] + a.mat[1]*b.mat[4] + a.mat[2]*b.mat[8] + a.mat[3]*b.mat[12];
    c.mat[1] = a.mat[0]*b.mat[1] + a.mat[1]*b.mat[5] + a.mat[2]*b.mat[9] + a.mat[3]*b.mat[13];
    c.mat[2] = a.mat[0]*b.mat[2] + a.mat[1]*b.mat[6] + a.mat[2]*b.mat[10] + a.mat[3]*b.mat[14];
    c.mat[3] = a.mat[0]*b.mat[3] + a.mat[1]*b.mat[7] + a.mat[2]*b.mat[11] + a.mat[3]*b.mat[15];

    c.mat[4] = a.mat[4]*b.mat[0] + a.mat[5]*b.mat[4] + a.mat[6]*b.mat[8] + a.mat[7]*b.mat[12];
    c.mat[5] = a.mat[4]*b.mat[1] + a.mat[5]*b.mat[5] + a.mat[6]*b.mat[9] + a.mat[7]*b.mat[13];
    c.mat[6] = a.mat[4]*b.mat[2] + a.mat[5]*b.mat[6] + a.mat[6]*b.mat[10] + a.mat[7]*b.mat[14];
    c.mat[7] = a.mat[4]*b.mat[3] + a.mat[5]*b.mat[7] + a.mat[6]*b.mat[11] + a.mat[7]*b.mat[15];

    c.mat[8] = a.mat[8]*b.mat[0] + a.mat[9]*b.mat[4] + a.mat[10]*b.mat[8] + a.mat[11]*b.mat[12];
    c.mat[9] = a.mat[8]*b.mat[1] + a.mat[9]*b.mat[5] + a.mat[10]*b.mat[9] + a.mat[11]*b.mat[13];
    c.mat[10] = a.mat[8]*b.mat[2] + a.mat[9]*b.mat[6] + a.mat[10]*b.mat[10] + a.mat[11]*b.mat[14];
    c.mat[11] = a.mat[8]*b.mat[3] + a.mat[9]*b.mat[7] + a.mat[10]*b.mat[11] + a.mat[11]*b.mat[15];

    c.mat[12] = a.mat[12]*b.mat[0] + a.mat[13]*b.mat[4] + a.mat[14]*b.mat[8] + a.mat[15]*b.mat[12];
    c.mat[13] = a.mat[12]*b.mat[1] + a.mat[13]*b.mat[5] + a.mat[14]*b.mat[9] + a.mat[15]*b.mat[13];
    c.mat[14] = a.mat[12]*b.mat[2] + a.mat[13]*b.mat[6] + a.mat[14]*b.mat[10] + a.mat[15]*b.mat[14];
    c.mat[15] = a.mat[12]*b.mat[3] + a.mat[13]*b.mat[7] + a.mat[14]*b.mat[11] + a.mat[15]*b.mat[15];
  }


  public void apply(float n00, float n01, float n02, float n03,
                    float n10, float n11, float n12, float n13,
                    float n20, float n21, float n22, float n23,
                    float n30, float n31, float n32, float n33) {

    float r00 = mat[0]*n00 + mat[1]*n10 + mat[2]*n20 + mat[3]*n30;
    float r01 = mat[0]*n01 + mat[1]*n11 + mat[2]*n21 + mat[3]*n31;
    float r02 = mat[0]*n02 + mat[1]*n12 + mat[2]*n22 + mat[3]*n32;
    float r03 = mat[0]*n03 + mat[1]*n13 + mat[2]*n23 + mat[3]*n33;

    float r10 = mat[4]*n00 + mat[5]*n10 + mat[6]*n20 + mat[7]*n30;
    float r11 = mat[4]*n01 + mat[5]*n11 + mat[6]*n21 + mat[7]*n31;
    float r12 = mat[4]*n02 + mat[5]*n12 + mat[6]*n22 + mat[7]*n32;
    float r13 = mat[4]*n03 + mat[5]*n13 + mat[6]*n23 + mat[7]*n33;

    float r20 = mat[8]*n00 + mat[9]*n10 + mat[10]*n20 + mat[11]*n30;
    float r21 = mat[8]*n01 + mat[9]*n11 + mat[10]*n21 + mat[11]*n31;
    float r22 = mat[8]*n02 + mat[9]*n12 + mat[10]*n22 + mat[11]*n32;
    float r23 = mat[8]*n03 + mat[9]*n13 + mat[10]*n23 + mat[11]*n33;

    float r30 = mat[12]*n00 + mat[13]*n10 + mat[14]*n20 + mat[15]*n30;
    float r31 = mat[12]*n01 + mat[13]*n11 + mat[14]*n21 + mat[15]*n31;
    float r32 = mat[12]*n02 + mat[13]*n12 + mat[14]*n22 + mat[15]*n32;
    float r33 = mat[12]*n03 + mat[13]*n13 + mat[14]*n23 + mat[15]*n33;

    mat[0] = r00; mat[1] = r01; mat[2] = r02; mat[3] = r03;
    mat[4] = r10; mat[5] = r11; mat[6] = r12; mat[7] = r13;
    mat[8] = r20; mat[9] = r21; mat[10] = r22; mat[11] = r23;
    mat[12] = r30; mat[13] = r31; mat[14] = r32; mat[15] = r33;
  }

  /**
   * Apply another matrix to the left of this one.
   */
  public void preApply(Matrix3D left) {
    preApply(left.mat[0], left.mat[1], left.mat[2], left.mat[3],
             left.mat[4], left.mat[5], left.mat[6], left.mat[7],
             left.mat[8], left.mat[9], left.mat[10], left.mat[11],
             left.mat[12], left.mat[13], left.mat[14], left.mat[15]);
  }


  public void preApply(float n00, float n01, float n02,
                       float n10, float n11, float n12) {
    preApply(n00, n01, 0, n02,
             n10, n11, 0, n12,
             0, 0, 1, 0,
             0, 0, 0, 1);
  }


  public void preApply(float n00, float n01, float n02, float n03,
                       float n10, float n11, float n12, float n13,
                       float n20, float n21, float n22, float n23,
                       float n30, float n31, float n32, float n33) {

    float r00 = n00*mat[0] + n01*mat[4] + n02*mat[8] + n03*mat[12];
    float r01 = n00*mat[1] + n01*mat[5] + n02*mat[9] + n03*mat[13];
    float r02 = n00*mat[2] + n01*mat[6] + n02*mat[10] + n03*mat[14];
    float r03 = n00*mat[3] + n01*mat[7] + n02*mat[11] + n03*mat[15];

    float r10 = n10*mat[0] + n11*mat[4] + n12*mat[8] + n13*mat[12];
    float r11 = n10*mat[1] + n11*mat[5] + n12*mat[9] + n13*mat[13];
    float r12 = n10*mat[2] + n11*mat[6] + n12*mat[10] + n13*mat[14];
    float r13 = n10*mat[3] + n11*mat[7] + n12*mat[11] + n13*mat[15];

    float r20 = n20*mat[0] + n21*mat[4] + n22*mat[8] + n23*mat[12];
    float r21 = n20*mat[1] + n21*mat[5] + n22*mat[9] + n23*mat[13];
    float r22 = n20*mat[2] + n21*mat[6] + n22*mat[10] + n23*mat[14];
    float r23 = n20*mat[3] + n21*mat[7] + n22*mat[11] + n23*mat[15];

    float r30 = n30*mat[0] + n31*mat[4] + n32*mat[8] + n33*mat[12];
    float r31 = n30*mat[1] + n31*mat[5] + n32*mat[9] + n33*mat[13];
    float r32 = n30*mat[2] + n31*mat[6] + n32*mat[10] + n33*mat[14];
    float r33 = n30*mat[3] + n31*mat[7] + n32*mat[11] + n33*mat[15];

    mat[0] = r00; mat[1] = r01; mat[2] = r02; mat[3] = r03;
    mat[4] = r10; mat[5] = r11; mat[6] = r12; mat[7] = r13;
    mat[8] = r20; mat[9] = r21; mat[10] = r22; mat[11] = r23;
    mat[12] = r30; mat[13] = r31; mat[14] = r32; mat[15] = r33;
  }


  //////////////////////////////////////////////////////////////


  public Vector3D mult(Vector3D source, Vector3D target) {
    if (target == null) {
      target = new Vector3D();
    }
    target.vec[0] = mat[0]*source.vec[0] + mat[1]*source.vec[1] + mat[2]*source.vec[2] + mat[3];
    target.vec[1] = mat[4]*source.vec[0] + mat[5]*source.vec[1] + mat[6]*source.vec[2] + mat[7];
    target.vec[2] = mat[8]*source.vec[0] + mat[9]*source.vec[1] + mat[10]*source.vec[2] + mat[11];
//    float tw = mat[12]*source.x + mat[13]*source.y + mat[14]*source.z + mat[15];
//    if (tw != 0 && tw != 1) {
//      target.div(tw);
//    }
    return target;
  }


  /*
  public Vector3D cmult(Vector3D source, Vector3D target) {
    if (target == null) {
      target = new Vector3D();
    }
    target.x = mat[0]*source.x + mat[4]*source.y + mat[8]*source.z + mat[12];
    target.y = mat[1]*source.x + mat[5]*source.y + mat[9]*source.z + mat[13];
    target.z = mat[2]*source.x + mat[6]*source.y + mat[10]*source.z + mat[14];
    float tw = mat[3]*source.x + mat[7]*source.y + mat[11]*source.z + mat[15];
    if (tw != 0 && tw != 1) {
      target.div(tw);
    }
    return target;
  }
  */


  /**
   * Multiply a three or four element vector against this matrix. If out is
   * null or not length 3 or 4, a new float array (length 3) will be returned.
   */
  public float[] mult(float[] source, float[] target) {
    if (target == null || target.length < 3) {
      target = new float[3];
    }
    if (source == target) {
      throw new RuntimeException("The source and target vectors used in " +
                                 "Matrix3D.mult() cannot be identical.");
    }
    if (target.length == 3) {
      target[0] = mat[0]*source[0] + mat[1]*source[1] + mat[2]*source[2] + mat[3];
      target[1] = mat[4]*source[0] + mat[5]*source[1] + mat[6]*source[2] + mat[7];
      target[2] = mat[8]*source[0] + mat[9]*source[1] + mat[10]*source[2] + mat[11];
      //float w = mat[12]*source[0] + mat[13]*source[1] + mat[14]*source[2] + mat[15];
      //if (w != 0 && w != 1) {
      //  target[0] /= w; target[1] /= w; target[2] /= w;
      //}
    } else if (target.length > 3) {
      target[0] = mat[0]*source[0] + mat[1]*source[1] + mat[2]*source[2] + mat[3]*source[3];
      target[1] = mat[4]*source[0] + mat[5]*source[1] + mat[6]*source[2] + mat[7]*source[3];
      target[2] = mat[8]*source[0] + mat[9]*source[1] + mat[10]*source[2] + mat[11]*source[3];
      target[3] = mat[12]*source[0] + mat[13]*source[1] + mat[14]*source[2] + mat[15]*source[3];
    }
    return target;
  }


  public float multX(float x, float y) {
    return mat[0]*x + mat[1]*y + mat[3];
  }


  public float multY(float x, float y) {
    return mat[4]*x + mat[5]*y + mat[7];
  }


  public float multX(float x, float y, float z) {
    return mat[0]*x + mat[1]*y + mat[2]*z + mat[3];
  }


  public float multY(float x, float y, float z) {
    return mat[4]*x + mat[5]*y + mat[6]*z + mat[7];
  }


  public float multZ(float x, float y, float z) {
    return mat[8]*x + mat[9]*y + mat[10]*z + mat[11];
  }


  public float multW(float x, float y, float z) {
    return mat[12]*x + mat[13]*y + mat[14]*z + mat[15];
  }


  public float multX(float x, float y, float z, float w) {
    return mat[0]*x + mat[1]*y + mat[2]*z + mat[3]*w;
  }


  public float multY(float x, float y, float z, float w) {
    return mat[4]*x + mat[5]*y + mat[6]*z + mat[7]*w;
  }


  public float multZ(float x, float y, float z, float w) {
    return mat[8]*x + mat[9]*y + mat[10]*z + mat[11]*w;
  }


  public float multW(float x, float y, float z, float w) {
    return mat[12]*x + mat[13]*y + mat[14]*z + mat[15]*w;
  }


  /**
   * Transpose this matrix.
   */
  public void transpose() {
    float temp;
    temp = mat[1]; mat[1] = mat[4]; mat[4] = temp;
    temp = mat[2]; mat[2] = mat[8]; mat[8] = temp;
    temp = mat[3]; mat[3] = mat[12]; mat[12] = temp;
    temp = mat[6]; mat[6] = mat[9]; mat[9] = temp;
    temp = mat[7]; mat[7] = mat[13]; mat[13] = temp;
    temp = mat[11]; mat[11] = mat[14]; mat[14] = temp;
  }
  
  
  /**
   * Invert this matrix into {@code m}, i.e., doesn't modify this matrix.
   * <p>
   * {@code m} should be non-null.
   */
  public boolean invert(Matrix3D m) {
  	float determinant = determinant();
    if (determinant == 0) {
      return false;
    }    
    

    // first row
    float t00 =  determinant3x3(mat[5], mat[6], mat[7], mat[9], mat[10], mat[11], mat[13], mat[14], mat[15]);
    float t01 = -determinant3x3(mat[4], mat[6], mat[7], mat[8], mat[10], mat[11], mat[12], mat[14], mat[15]);
    float t02 =  determinant3x3(mat[4], mat[5], mat[7], mat[8], mat[9], mat[11], mat[12], mat[13], mat[15]);
    float t03 = -determinant3x3(mat[4], mat[5], mat[6], mat[8], mat[9], mat[10], mat[12], mat[13], mat[14]);

    // second row
    float t10 = -determinant3x3(mat[1], mat[2], mat[3], mat[9], mat[10], mat[11], mat[13], mat[14], mat[15]);
    float t11 =  determinant3x3(mat[0], mat[2], mat[3], mat[8], mat[10], mat[11], mat[12], mat[14], mat[15]);
    float t12 = -determinant3x3(mat[0], mat[1], mat[3], mat[8], mat[9], mat[11], mat[12], mat[13], mat[15]);
    float t13 =  determinant3x3(mat[0], mat[1], mat[2], mat[8], mat[9], mat[10], mat[12], mat[13], mat[14]);

    // third row
    float t20 =  determinant3x3(mat[1], mat[2], mat[3], mat[5], mat[6], mat[7], mat[13], mat[14], mat[15]);
    float t21 = -determinant3x3(mat[0], mat[2], mat[3], mat[4], mat[6], mat[7], mat[12], mat[14], mat[15]);
    float t22 =  determinant3x3(mat[0], mat[1], mat[3], mat[4], mat[5], mat[7], mat[12], mat[13], mat[15]);
    float t23 = -determinant3x3(mat[0], mat[1], mat[2], mat[4], mat[5], mat[6], mat[12], mat[13], mat[14]);

    // fourth row
    float t30 = -determinant3x3(mat[1], mat[2], mat[3], mat[5], mat[6], mat[7], mat[9], mat[10], mat[11]);
    float t31 =  determinant3x3(mat[0], mat[2], mat[3], mat[4], mat[6], mat[7], mat[8], mat[10], mat[11]);
    float t32 = -determinant3x3(mat[0], mat[1], mat[3], mat[4], mat[5], mat[7], mat[8], mat[9], mat[11]);
    float t33 =  determinant3x3(mat[0], mat[1], mat[2], mat[4], mat[5], mat[6], mat[8], mat[9], mat[10]);

     // transpose and divide by the determinant
     m.mat[0] = t00 / determinant;
     m.mat[1] = t10 / determinant;
     m.mat[2] = t20 / determinant;
     m.mat[3] = t30 / determinant;

     m.mat[4] = t01 / determinant;
     m.mat[5] = t11 / determinant;
     m.mat[6] = t21 / determinant;
     m.mat[7] = t31 / determinant;

     m.mat[8] = t02 / determinant;
     m.mat[9] = t12 / determinant;
     m.mat[10] = t22 / determinant;
     m.mat[11] = t32 / determinant;

     m.mat[12] = t03 / determinant;
     m.mat[13] = t13 / determinant;
     m.mat[14] = t23 / determinant;
     m.mat[15] = t33 / determinant;
     
     return true;
  }

  /**
   * Invert this matrix.
   * @return true if successful
   */
  public boolean invert() {
    float determinant = determinant();
    if (determinant == 0) {
      return false;
    }

    // first row
    float t00 =  determinant3x3(mat[5], mat[6], mat[7], mat[9], mat[10], mat[11], mat[13], mat[14], mat[15]);
    float t01 = -determinant3x3(mat[4], mat[6], mat[7], mat[8], mat[10], mat[11], mat[12], mat[14], mat[15]);
    float t02 =  determinant3x3(mat[4], mat[5], mat[7], mat[8], mat[9], mat[11], mat[12], mat[13], mat[15]);
    float t03 = -determinant3x3(mat[4], mat[5], mat[6], mat[8], mat[9], mat[10], mat[12], mat[13], mat[14]);

    // second row
    float t10 = -determinant3x3(mat[1], mat[2], mat[3], mat[9], mat[10], mat[11], mat[13], mat[14], mat[15]);
    float t11 =  determinant3x3(mat[0], mat[2], mat[3], mat[8], mat[10], mat[11], mat[12], mat[14], mat[15]);
    float t12 = -determinant3x3(mat[0], mat[1], mat[3], mat[8], mat[9], mat[11], mat[12], mat[13], mat[15]);
    float t13 =  determinant3x3(mat[0], mat[1], mat[2], mat[8], mat[9], mat[10], mat[12], mat[13], mat[14]);

    // third row
    float t20 =  determinant3x3(mat[1], mat[2], mat[3], mat[5], mat[6], mat[7], mat[13], mat[14], mat[15]);
    float t21 = -determinant3x3(mat[0], mat[2], mat[3], mat[4], mat[6], mat[7], mat[12], mat[14], mat[15]);
    float t22 =  determinant3x3(mat[0], mat[1], mat[3], mat[4], mat[5], mat[7], mat[12], mat[13], mat[15]);
    float t23 = -determinant3x3(mat[0], mat[1], mat[2], mat[4], mat[5], mat[6], mat[12], mat[13], mat[14]);

    // fourth row
    float t30 = -determinant3x3(mat[1], mat[2], mat[3], mat[5], mat[6], mat[7], mat[9], mat[10], mat[11]);
    float t31 =  determinant3x3(mat[0], mat[2], mat[3], mat[4], mat[6], mat[7], mat[8], mat[10], mat[11]);
    float t32 = -determinant3x3(mat[0], mat[1], mat[3], mat[4], mat[5], mat[7], mat[8], mat[9], mat[11]);
    float t33 =  determinant3x3(mat[0], mat[1], mat[2], mat[4], mat[5], mat[6], mat[8], mat[9], mat[10]);

    // transpose and divide by the determinant
    mat[0] = t00 / determinant;
    mat[1] = t10 / determinant;
    mat[2] = t20 / determinant;
    mat[3] = t30 / determinant;

    mat[4] = t01 / determinant;
    mat[5] = t11 / determinant;
    mat[6] = t21 / determinant;
    mat[7] = t31 / determinant;

    mat[8] = t02 / determinant;
    mat[9] = t12 / determinant;
    mat[10] = t22 / determinant;
    mat[11] = t32 / determinant;

    mat[12] = t03 / determinant;
    mat[13] = t13 / determinant;
    mat[14] = t23 / determinant;
    mat[15] = t33 / determinant;

    return true;
  }

  /**
   * Calculate the determinant of a 3x3 matrix.
   * @return result
   */
  private float determinant3x3(float t00, float t01, float t02,
                               float t10, float t11, float t12,
                               float t20, float t21, float t22) {
    return (t00 * (t11 * t22 - t12 * t21) +
            t01 * (t12 * t20 - t10 * t22) +
            t02 * (t10 * t21 - t11 * t20));
  }


  /**
   * @return the determinant of the matrix
   */
  public float determinant() {
    float f =
      mat[0]
      * ((mat[5] * mat[10] * mat[15] + mat[6] * mat[11] * mat[13] + mat[7] * mat[9] * mat[14])
         - mat[7] * mat[10] * mat[13]
         - mat[5] * mat[11] * mat[14]
         - mat[6] * mat[9] * mat[15]);
    f -= mat[1]
      * ((mat[4] * mat[10] * mat[15] + mat[6] * mat[11] * mat[12] + mat[7] * mat[8] * mat[14])
         - mat[7] * mat[10] * mat[12]
         - mat[4] * mat[11] * mat[14]
         - mat[6] * mat[8] * mat[15]);
    f += mat[2]
      * ((mat[4] * mat[9] * mat[15] + mat[5] * mat[11] * mat[12] + mat[7] * mat[8] * mat[13])
         - mat[7] * mat[9] * mat[12]
         - mat[4] * mat[11] * mat[13]
         - mat[5] * mat[8] * mat[15]);
    f -= mat[3]
      * ((mat[4] * mat[9] * mat[14] + mat[5] * mat[10] * mat[12] + mat[6] * mat[8] * mat[13])
         - mat[6] * mat[9] * mat[12]
         - mat[4] * mat[10] * mat[13]
         - mat[5] * mat[8] * mat[14]);
    return f;
  }


  //////////////////////////////////////////////////////////////

  // REVERSE VERSIONS OF MATRIX OPERATIONS

  // These functions should not be used, as they will be removed in the future.


  protected void invTranslate(float tx, float ty, float tz) {
    preApply(1, 0, 0, -tx,
             0, 1, 0, -ty,
             0, 0, 1, -tz,
             0, 0, 0, 1);
  }


  protected void invRotateX(float angle) {
    float c = cos(-angle);
    float s = sin(-angle);
    preApply(1, 0, 0, 0,  0, c, -s, 0,  0, s, c, 0,  0, 0, 0, 1);
  }


  protected void invRotateY(float angle) {
    float c = cos(-angle);
    float s = sin(-angle);
    preApply(c, 0, s, 0,  0, 1, 0, 0,  -s, 0, c, 0,  0, 0, 0, 1);
  }


  protected void invRotateZ(float angle) {
    float c = cos(-angle);
    float s = sin(-angle);
    preApply(c, -s, 0, 0,  s, c, 0, 0,  0, 0, 1, 0,  0, 0, 0, 1);
  }


  protected void invRotate(float angle, float v0, float v1, float v2) {
    //TODO should make sure this vector is normalized

    float c = cos(-angle);
    float s = sin(-angle);
    float t = 1.0f - c;

    preApply((t*v0*v0) + c, (t*v0*v1) - (s*v2), (t*v0*v2) + (s*v1), 0,
             (t*v0*v1) + (s*v2), (t*v1*v1) + c, (t*v1*v2) - (s*v0), 0,
             (t*v0*v2) - (s*v1), (t*v1*v2) + (s*v0), (t*v2*v2) + c, 0,
             0, 0, 0, 1);
  }


  protected void invScale(float x, float y, float z) {
    preApply(1/x, 0, 0, 0,  0, 1/y, 0, 0,  0, 0, 1/z, 0,  0, 0, 0, 1);
  }


  protected boolean invApply(float n00, float n01, float n02, float n03,
                             float n10, float n11, float n12, float n13,
                             float n20, float n21, float n22, float n23,
                             float n30, float n31, float n32, float n33) {
    if (inverseCopy == null) {
      inverseCopy = new Matrix3D();
    }
    inverseCopy.set(n00, n01, n02, n03,
                    n10, n11, n12, n13,
                    n20, n21, n22, n23,
                    n30, n31, n32, n33);
    if (!inverseCopy.invert()) {
      return false;
    }
    preApply(inverseCopy);
    return true;
  }

  //////////////////////////////////////////////////////////////

  private final float sin(float angle) {
    return (float) Math.sin(angle);
  }

  private final float cos(float angle) {
    return (float) Math.cos(angle);
  }
}
