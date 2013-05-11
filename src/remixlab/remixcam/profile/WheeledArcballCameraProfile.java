package remixlab.remixcam.profile;

import remixlab.remixcam.core.AbstractScene;
import remixlab.remixcam.event.*;

public class WheeledArcballCameraProfile extends CameraProfile {
	public WheeledArcballCameraProfile(AbstractScene scn, String n) {
		super(scn, n);
		setCameraMouseBinding(LEFT, DLDeviceAction.ROTATE);
	  //should work only iFrame is an instance of drivable
		setFrameWheelBinding( DLDeviceAction.ZOOM );		
		setCameraWheelBinding( DLDeviceAction.ZOOM );	
		arcballDefaultShortcuts();		
	}
}
