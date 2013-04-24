package remixlab.remixcam.profile;

import remixlab.remixcam.core.AbstractScene;
import remixlab.remixcam.core.AbstractScene.DeviceAction;

public class WheeledArcballCameraProfile extends CameraProfile {
	public WheeledArcballCameraProfile(AbstractScene scn, String n) {
		super(scn, n);
		setCameraMouseBinding(LEFT, AbstractScene.DeviceAction.ROTATE);
	  //should work only iFrame is an instance of drivable
		setFrameWheelBinding( DeviceAction.ZOOM );		
		setCameraWheelBinding( DeviceAction.ZOOM );	
		arcballDefaultShortcuts();		
	}
}
