package remixlab.remixcam.geom;

import remixlab.remixcam.core.Copyable;

public interface Primitivable extends Copyable {
	public void link(float [] src);
	public void unLink();
	@Override
	public Primitivable get();
	public float [] get(float [] target);
	public void set(Primitivable source);
	public void set(float [] source);	
	public void reset();
}
