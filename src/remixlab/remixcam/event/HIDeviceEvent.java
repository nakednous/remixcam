package remixlab.remixcam.event;

import java.util.ArrayList;

import remixlab.remixcam.core.AbstractScene;
import remixlab.remixcam.geom.Vector3D;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

public class HIDeviceEvent extends DLEvent {
	public boolean absolute = true;	
	public boolean cursor = true;
	
	protected Vector3D rotation, rotSens;
	protected Vector3D translation, transSens;
	
  //absolute mode
	protected Vector3D prevRotation, prevTranslation;
	
	//protected Vector3D t;
	//protected Quaternion q;
	protected float tx;
  protected float ty;
  protected float tz;
	protected float roll;
  protected float pitch;
  protected float yaw;

	//protected Quaternion quaternion;
  
  public HIDeviceEvent() {
  	translation = new Vector3D();
		transSens = new Vector3D(1, 1, 1);
		rotation = new Vector3D();
		rotSens = new Vector3D(1, 1, 1);		
		//quaternion = new Quaternion();
		//t = new Vector3D();
    //q = new Quaternion();
    tx = translation.vec[0] * transSens.vec[0];
    ty = translation.vec[1] * transSens.vec[1];
    tz = translation.vec[2] * transSens.vec[2];
  	roll = rotation.vec[0] * rotSens.vec[0];
    pitch = rotation.vec[1] * rotSens.vec[1];
    yaw = rotation.vec[2] * rotSens.vec[2];  
  }
	
  public HIDeviceEvent(Integer modifiers) {
  	this();  
  	//setModifiers(modifiers);  	  
  }
  
  protected HIDeviceEvent(HIDeviceEvent other) {
  	super(other);
  	//setMode(Mode.ABSOLUTE); 
	}
  
  @Override
	public HIDeviceEvent get() {
		return new HIDeviceEvent(this);
	}
  
  /**
   * Sets the device type.
   */
  /**
  public void setMode(Mode m) {
  	if(m == Mode.ABSOLUTE) {
  		if(prevTranslation == null)
  			prevTranslation = new Vector3D();
  		if(prevRotation == null)
  			prevRotation = new Vector3D();
    }
  	mode = m;
  }
  */
  
  /**
   * Return the device type.
   */
  /**
  public Mode mode() {
  	return mode;
  }	
  */
}
