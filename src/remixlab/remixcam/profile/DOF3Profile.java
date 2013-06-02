package remixlab.remixcam.profile;

import remixlab.remixcam.core.*;

public class DOF3Profile extends AbstractMotionProfile<Constants.DOF_3Action> {
	public DOF3Profile(AbstractScene scn, String n) {
		super(scn, n);
	}
	
	@Override
	public void setDefaultBindings() {
		//setBinding(DOF_2Action.ZOOM);
	}	
}