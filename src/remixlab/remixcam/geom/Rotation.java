package remixlab.remixcam.geom;

import remixlab.remixcam.core.Constants;

public class Rotation implements Constants, Orientable {
	protected float angle;
	
	public Rotation() {
		angle = 0;
	}
	
	public Rotation(float a) {
		angle = a;
	}
	
	public Rotation(Vector3D from, Vector3D to) {
		fromTo(from, to);
	}
	
	protected Rotation(Rotation a1) {
		this.angle = a1.angle();
	}
	
	@Override
	public Rotation get() {
		return new Rotation(this);
	}
	
	@Override
	public float angle() {
		return angle;
	}

	@Override
	public void negate() {
		angle = -angle;
	}

	@Override
	public Orientable inverse() {
		return new Rotation(-angle());
	}

	@Override
	public Vector3D rotate(Vector3D v) {
		float cosB = (float)Math.cos((float)angle());
		float sinB = (float)Math.sin((float)angle());
		return new Vector3D( ((v.x()*cosB) - (v.y()*sinB)), ((v.x()*sinB) + (v.y()*cosB)));
	}

	@Override
	public Vector3D inverseRotate(Vector3D v) {
		float cosB = (float)Math.cos(-(float)angle());
		float sinB = (float)Math.cos(-(float)angle());
		return new Vector3D( ((v.x()*cosB) - (v.y()*sinB)), ((v.x()*sinB) + (v.y()*cosB)));
	}

	@Override
	public float[][] rotationMatrix() {
		float [][] mat = new float [4][4];
		float [][] result = new float [3][3];
		matrix().getTransposed(mat);		
	  for (int i=0; i<3; ++i)
	    for (int j=0; j<3; ++j)
	      result[i][j] = mat[i][j];
	  return result;
	}

	@Override
	public float[][] inverseRotationMatrix() {
		float [][] mat = new float [4][4];
		float [][] result = new float [3][3];
		inverseMatrix().getTransposed(mat);		
	  for (int i=0; i<3; ++i)
	    for (int j=0; j<3; ++j)
	      result[i][j] = mat[i][j];		
	  return result;
	}

	@Override
	public Matrix3D matrix() {
		float cosB = (float)Math.cos((double)angle());
		float sinB = (float)Math.sin((double)angle());
		
		return new Matrix3D(cosB, sinB, 0, 0,
                       -sinB, cosB, 0, 0,
                           0,    0, 1, 0,
                           0,    0, 0, 1);
	}

	@Override
	public Matrix3D inverseMatrix() {
		float cosB = (float)Math.cos(-(float)angle());
		float sinB = (float)Math.sin(-(float)angle());
		
		return new Matrix3D(cosB, sinB, 0, 0,
                       -sinB, cosB, 0, 0,
                           0,    0, 1, 0,
                           0,    0, 0, 1);
	}

	@Override
	public void fromMatrix(Matrix3D glMatrix) {
		angle = (float)Math.acos(glMatrix.m00());
	}

	@Override
	public void fromRotationMatrix(float[][] m) {
		angle = (float)Math.acos(m[0][0]);		
	}
	
	@Override
	public final void compose(Orientable r) {
		float res = angle + r.angle();
		angle = angle + r.angle();
		angle = res;
	}
	
	public final static Orientable compose(Orientable r1, Orientable r2) {		
		return new Rotation(r1.angle() + r2.angle());
	}

	@Override
	public float normalize() {
		if ( Math.abs(angle) > TWO_PI ) {
			angle = angle % TWO_PI;
		}
		if( angle < 0 )
			angle = TWO_PI + angle;
		return angle;
	}

	@Override
	public void fromTo(Vector3D from, Vector3D to) {
		float fromSqNorm = from.squaredNorm();
		float toSqNorm = to.squaredNorm();		
		if ((fromSqNorm < 1E-10f) || (toSqNorm < 1E-10f)) {
			angle = 0;
		} else {
			angle = (float) Math.asin((float) Math.sqrt(1	/ (fromSqNorm * toSqNorm)));

			//TODO check normalization?
			/**
			if (from.dot(to) < 0.0)
				angle = PI - angle;
			*/
		}
	}
}
