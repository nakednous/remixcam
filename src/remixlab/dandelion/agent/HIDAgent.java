/*******************************************************************************
 * dandelion (version 1.0.0)
 * Copyright (c) 2014 National University of Colombia, https://github.com/remixlab
 * @author Jean Pierre Charalambos, http://otrolado.info/
 *     
 * All rights reserved. Library that eases the creation of interactive
 * scenes, released under the terms of the GNU Public License v3.0
 * which is available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package remixlab.dandelion.agent;

import remixlab.dandelion.core.*;
//import remixlab.tersehandling.core.Grabbable;
import remixlab.tersehandling.generic.event.GenericDOF6Event;
import remixlab.tersehandling.generic.profile.GenericClickProfile;
import remixlab.tersehandling.generic.profile.GenericMotionProfile;

public class HIDAgent extends GenericWheeledBiMotionAgent<GenericMotionProfile<Constants.DOF6Action>> {
	public HIDAgent(AbstractScene scn, String n) {
		super(new GenericMotionProfile<WheelAction>(),
			    new GenericMotionProfile<WheelAction>(),
				  new GenericMotionProfile<DOF6Action>(),
			    new GenericMotionProfile<DOF6Action>(),
			    new GenericClickProfile<ClickAction>(),
			    new GenericClickProfile<ClickAction>(), scn, n);
		cameraProfile().setBinding(TH_NOMODIFIER_MASK, TH_NOBUTTON, DOF6Action.TRANSLATE_ROTATE);
		frameProfile().setBinding(TH_NOMODIFIER_MASK, TH_NOBUTTON, DOF6Action.TRANSLATE_ROTATE);
	}
	
	@Override
	public GenericDOF6Event<Constants.DOF6Action> feed() {
		return null;
	}
	
	@Override
	public GenericMotionProfile<Constants.DOF6Action> cameraProfile() {
		return camProfile;
	}
	
	@Override
	public GenericMotionProfile<Constants.DOF6Action> frameProfile() {
		return profile;
	}
	
	public void setXTranslationSensitivity(float s) {
		sens[0] = s;
	}
	
	public void setYTranslationSensitivity(float s) {
		sens[1] = s;
	}
	
	public void setZTranslationSensitivity(float s) {
		sens[2] = s;
	}
	
	public void setXRotationSensitivity(float s) {
		sens[3] = s;
	}
	
	public void setYRotationSensitivity(float s) {
		sens[4] = s;
	}
	
	public void setZRotationSensitivity(float s) {
		sens[5] = s;
	}
}
