package remixlab.remixcam.profile;

import remixlab.remixcam.core.*;

public class DOF2Profile extends DOFProfile<Constants.DOF_2Action> {
	public DOF2Profile(AbstractScene scn, String n) {
		super(scn, n);
	}
	
	@Override
	public void setDefaultBindings() {
		//setBinding(DOF_2Action.ZOOM);
	}	
}