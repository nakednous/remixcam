package remixlab.remixcam.geom;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

import remixlab.remixcam.core.Constants;

public class Rotation implements Constants, Orientable {
	@Override
	public int hashCode() {
    return new HashCodeBuilder(17, 37).  
    append(this.angle).
    toHashCode();    
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;		
		if (obj.getClass() != getClass()) return false;
				
		Rotation other = (Rotation) obj;
		return new EqualsBuilder()		
		.append(this.angle,  other.angle)
		.isEquals();						
	}	
	
	protected float angle;
	
	public Rotation() {
		angle = 0;
	}
	
	public Rotation(float a) {
		angle = a;
		normalize();
	}
	
	public Rotation(DLVector from, DLVector to) {
		fromTo(from, to);
	}
	
	public Rotation(Point center, Point prev, Point curr) {
		DLVector from = new DLVector(prev.x - center.x, prev.y - center.y);
		DLVector to = new DLVector(curr.x - center.x, curr.y - center.y);
		fromTo(from, to);
	}
	
	protected Rotation(Rotation a1) {
		this.angle = a1.angle();
		normalize();
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
	public DLVector rotate(DLVector v) {
		float cosB = (float)Math.cos((float)angle());
		float sinB = (float)Math.sin((float)angle());
		return new DLVector( ((v.x()*cosB) - (v.y()*sinB)), ((v.x()*sinB) + (v.y()*cosB)));
	}

	@Override
	public DLVector inverseRotate(DLVector v) {
		float cosB = (float)Math.cos(-(float)angle());
		float sinB = (float)Math.sin(-(float)angle());
		return new DLVector( ((v.x()*cosB) - (v.y()*sinB)), ((v.x()*sinB) + (v.y()*cosB)));
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
	public DLMatrix matrix() {
		float cosB = (float)Math.cos((double)angle());
		float sinB = (float)Math.sin((double)angle());
		
		return new DLMatrix(cosB, sinB, 0, 0,
                       -sinB, cosB, 0, 0,
                           0,    0, 1, 0,
                           0,    0, 0, 1);
	}

	@Override
	public DLMatrix inverseMatrix() {
		float cosB = (float)Math.cos(-(float)angle());
		float sinB = (float)Math.sin(-(float)angle());
		
		return new DLMatrix(cosB, sinB, 0, 0,
                       -sinB, cosB, 0, 0,
                           0,    0, 1, 0,
                           0,    0, 0, 1);
	}

	@Override
	public void fromMatrix(DLMatrix glMatrix) {
		//"If both sine and cosine of the angle are already known, ATAN2(sin, cos) gives the angle"
		//http://www.firebirdsql.org/refdocs/langrefupd21-intfunc-atan2.html
		angle = (float)Math.atan2(glMatrix.m10(), glMatrix.m00());
	}

	@Override
	public void fromRotationMatrix(float[][] m) {
		//"If both sine and cosine of the angle are already known, ATAN2(sin, cos) gives the angle"
		//http://www.firebirdsql.org/refdocs/langrefupd21-intfunc-atan2.html
		angle = (float)Math.atan2(m[1][0], m[0][0]);
	}
	
	@Override
	public final void compose(Orientable r) {
		float res = angle + r.angle();
		angle = angle + r.angle();
		angle = res;
		this.normalize();
	}
	
	public final static Orientable compose(Orientable r1, Orientable r2) {		
		return new Rotation(r1.angle() + r2.angle());
	}
	
	public float normalize(boolean onlypos) {
		if(onlypos) {// 0 <-> two_pi
			if ( Math.abs(angle) > TWO_PI ) {
				angle = angle % TWO_PI;
			}
			if( angle < 0 )
				angle = TWO_PI + angle;
		}
		else {// -pi <-> pi
			if ( Math.abs(angle) > PI )
				if(angle >= 0)
					angle = (angle % PI) - PI;
				else
					angle = PI - (angle % PI);
		}
		return angle;
	}

	@Override
	public float normalize() {
		//return normalize(false);
		return angle; // dummy
	}

	@Override	
	public void fromTo(DLVector from, DLVector to) {
		//perp dot product. See:
		//1. http://stackoverflow.com/questions/2150050/finding-signed-angle-between-vectors
		//2. http://mathworld.wolfram.com/PerpDotProduct.html
		float fromNorm = from.mag();
		float toNorm = to.mag();				
		if ((Geom.zero(fromNorm)) || (Geom.zero(toNorm)))
			angle = 0;
		else
			//angle =(float) Math.acos( (double)Vector3D.dot(from, to) / ( fromNorm * toNorm ));
			angle = (float )Math.atan2( from.x()*to.y() - from.y()*to.x(), from.x()*to.x() + from.y()*to.y() );
	}
}
