package remixlab.remixcam.geom;

import remixlab.remixcam.core.Copyable;

public interface Orientable extends Copyable {	
	@Override
	public Orientable get();	
	public float angle();
	public void negate();
	public void compose(Orientable o);
	public Orientable inverse();
	public Vector3D rotate(Vector3D v);
	public Vector3D inverseRotate(Vector3D v);	
	public float[][] rotationMatrix();
	public float[][] inverseRotationMatrix();
	public Matrix3D matrix();
	public Matrix3D inverseMatrix();
	public void fromMatrix(Matrix3D glMatrix);
	public void fromRotationMatrix(float[][] m);
	public float normalize();
	public void fromTo(Vector3D from, Vector3D to);
}
