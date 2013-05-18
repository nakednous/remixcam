package remixlab.remixcam.profile;

import remixlab.remixcam.action.DOF_6Action;
import remixlab.remixcam.core.AbstractScene;
import remixlab.remixcam.event.*;

public class WheeledArcballCameraProfile extends CameraProfile {
	public WheeledArcballCameraProfile(AbstractScene scn, String n) {
		super(scn, n);
		setCameraMouseBinding(LEFT, DOF_6Action.ROTATE);
	  //should work only iFrame is an instance of drivable
		setFrameWheelBinding( DOF_6Action.ZOOM );		
		setCameraWheelBinding( DOF_6Action.ZOOM );	
		arcballDefaultShortcuts();		
	}
}
