package remixlab.remixcam.profile;

import remixlab.remixcam.core.*;

public class DOF6Profile extends DOFProfile<Constants.DOF_6Action> {
	public DOF6Profile(AbstractScene scn, String n) {
		super(scn, n);
	}
	
	@Override
	public void setDefaultBindings() {
		//setBinding(DOF_2Action.ZOOM);
	}	
}