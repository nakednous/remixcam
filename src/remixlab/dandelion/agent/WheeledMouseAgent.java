package remixlab.dandelion.agent;

import remixlab.dandelion.core.Constants;
import remixlab.dandelion.core.AbstractScene;
import remixlab.tersehandling.duoable.profile.GenericMotionProfile;

public class WheeledMouseAgent extends GenericWheeledMouseAgent<GenericMotionProfile<Constants.DOF_1Action>> {

	public WheeledMouseAgent(AbstractScene scn, String n) {
		super(scn, n);
		wheelProfile = new GenericMotionProfile<Constants.DOF_1Action>();
		frameWheelProfile = new GenericMotionProfile<Constants.DOF_1Action>();		
		wheelProfile.setBinding(DOF_1Action.ZOOM);
		frameWheelProfile.setBinding(DOF_1Action.ZOOM);
	}

}
