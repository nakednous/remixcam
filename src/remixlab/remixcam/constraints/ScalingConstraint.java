package remixlab.remixcam.constraints;

import remixlab.remixcam.geom.*;

public class ScalingConstraint extends Constraint {
	public enum Type {
		FREE, FORBIDDEN, ISOTROPIC, DIRECTIONAL
	};
	
	private Type sclConstraintType;
	private Vector3D sclConstraintValues;
	
	/**
	 * 
	 * Default constructor.
	 * <p>
	 * {@link #translationConstraintType()} and {@link #rotationConstraintType()}
	 * are set to {@link Type#FREE}. {@link #translationConstraintDirection()} and
	 * {@link #rotationConstraintDirection()} are set to (0,0,0).
	 */
	public ScalingConstraint() {		
		this.sclConstraintType = Type.FREE;
		sclConstraintValues = new Vector3D();
	}
	
	public Type scalingConstraintType() {
		return sclConstraintType;
	}
	
	public void setScalingConstraintType(Type type) {
		sclConstraintType = type;
	}
	
	public Vector3D scalingConstraintDirection() {
		return sclConstraintValues;
	}
	
	public void setScalingConstraint(Type type, Vector3D direction) {
		setScalingConstraintType(type);
		setScalingConstraintValues(direction);
	}
	
	public void setScalingConstraintValues(Vector3D direction) {
		sclConstraintValues.x( Geom.nonZero(direction.x()) ? (Geom.positive(direction.x()) ? 1 : -1 ) : 0 );
		sclConstraintValues.y( Geom.nonZero(direction.y()) ? (Geom.positive(direction.y()) ? 1 : -1 ) : 0 );
		sclConstraintValues.z( Geom.nonZero(direction.z()) ? (Geom.positive(direction.z()) ? 1 : -1 ) : 0 );
	}
	
	@Override
	public Vector3D constrainScaling(Vector3D scaling, GeomFrame frame) {			
		Vector3D res = new Vector3D(scaling.vec[0], scaling.vec[1], scaling.vec[2]);
		if( Geom.zero(res.x()) ) res.x(1);
		if( Geom.zero(res.y()) ) res.y(1);
		if( Geom.zero(res.z()) ) res.z(1);
		switch (scalingConstraintType()) {
		case FREE:
			break;
		case FORBIDDEN:
			res = new Vector3D(1.0f, 1.0f, 1.0f);
			break;
		case DIRECTIONAL:
			//sclConstraintValues is of the shape (0:1:-1, 0:1:-1, 0:1:-1)
			res.x(( sclConstraintValues.x() == 0 ) ? 1 : ( sclConstraintValues.x() * res.x() < 0 ) ? -res.x() : res.x() );
			res.y(( sclConstraintValues.y() == 0 ) ? 1 : ( sclConstraintValues.y() * res.y() < 0 ) ? -res.y() : res.y() );
			res.z(( sclConstraintValues.z() == 0 ) ? 1 : ( sclConstraintValues.z() * res.z() < 0 ) ? -res.z() : res.z() );
			break;
		case ISOTROPIC:
			float iso = Math.abs(Math.min(res.x(), Math.min(res.y(), res.z())));
			res.set(iso, iso, iso);
			break;
		}
		return res;
	}
}
