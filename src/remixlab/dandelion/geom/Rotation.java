/**
 *                     RemixCam (version 0.70.0)      
 *      Copyright (c) 2013 by National University of Colombia
 *                 @author Jean Pierre Charalambos      
 *              https://github.com/nakednous/remixcam
 *                           
 * This java library provides classes to ease the creation of interactive 3D
 * scenes in various languages and frameworks such as JOGL, WebGL and Processing.
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

package remixlab.dandelion.geom;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

import remixlab.dandelion.core.Constants;

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
	
	public Rotation(Vec from, Vec to) {
		fromTo(from, to);
	}
	
	public Rotation(Point center, Point prev, Point curr) {
		Vec from = new Vec(prev.x - center.x, prev.y - center.y);
		Vec to = new Vec(curr.x - center.x, curr.y - center.y);
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
	public Vec rotate(Vec v) {
		float cosB = (float)Math.cos((float)angle());
		float sinB = (float)Math.sin((float)angle());
		return new Vec( ((v.x()*cosB) - (v.y()*sinB)), ((v.x()*sinB) + (v.y()*cosB)));
	}

	@Override
	public Vec inverseRotate(Vec v) {
		float cosB = (float)Math.cos(-(float)angle());
		float sinB = (float)Math.sin(-(float)angle());
		return new Vec( ((v.x()*cosB) - (v.y()*sinB)), ((v.x()*sinB) + (v.y()*cosB)));
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
	public Mat matrix() {
		float cosB = (float)Math.cos((double)angle());
		float sinB = (float)Math.sin((double)angle());
		
		return new Mat(cosB, sinB, 0, 0,
                       -sinB, cosB, 0, 0,
                           0,    0, 1, 0,
                           0,    0, 0, 1);
	}

	@Override
	public Mat inverseMatrix() {
		float cosB = (float)Math.cos(-(float)angle());
		float sinB = (float)Math.sin(-(float)angle());
		
		return new Mat(cosB, sinB, 0, 0,
                       -sinB, cosB, 0, 0,
                           0,    0, 1, 0,
                           0,    0, 0, 1);
	}

	@Override
	public void fromMatrix(Mat glMatrix) {
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
	public void fromTo(Vec from, Vec to) {
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
