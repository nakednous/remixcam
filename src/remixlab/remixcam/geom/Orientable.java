package remixlab.remixcam.geom;

import remixlab.remixcam.core.Copyable;


public interface Orientable extends Copyable {	
	@Override
	public Orientable get();	
	public float angle();
	public void negate();
	public void compose(Orientable o);
	public Orientable inverse();
	public DLVector rotate(DLVector v);
	public DLVector inverseRotate(DLVector v);	
	public float[][] rotationMatrix();
	public float[][] inverseRotationMatrix();
	public DLMatrix matrix();
	public DLMatrix inverseMatrix();
	public void fromMatrix(DLMatrix glMatrix);
	public void fromRotationMatrix(float[][] m);
	public float normalize();
	public void fromTo(DLVector from, DLVector to);
}
