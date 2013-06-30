/**
 *                     Dandelion (version 0.70.0)      
 *          Copyright (c) 2013 by Jean Pierre Charalambos
 *                 @author Jean Pierre Charalambos      
 *              https://github.com/nakednous/remixcam
 *                           
 * This library provides classes to ease the creation of interactive
 * frame-based, 2d and 3d scenes.
 * 
 * This source file is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option)
 * any later version.
 * 
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 * 
 * A copy of the GNU General Public License is available on the World Wide Web
 * at <http://www.gnu.org/copyleft/gpl.html>. You can also obtain it by
 * writing to the Free Software Foundation, 51 Franklin Street, Suite 500
 * Boston, MA 02110-1335, USA.
 */

package remixlab.dandelion.agent;

import remixlab.dandelion.core.*;
import remixlab.dandelion.event.DOF6Event;
import remixlab.tersehandling.core.Grabbable;
import remixlab.tersehandling.duoable.profile.GenericClickProfile;
import remixlab.tersehandling.duoable.profile.GenericMotionProfile;

public class HIDAgent extends GenericBiMotionAgent<GenericMotionProfile<Constants.DOF_6Action>> implements Constants {
	public HIDAgent(AbstractScene scn, String n) {
		super(new GenericMotionProfile<Constants.DOF_6Action>(),
			    new GenericMotionProfile<Constants.DOF_6Action>(),
			    new GenericClickProfile<Constants.DOF_0Action>(), scn.terseHandler(), n);
	  setDefaultGrabber(scn.pinhole().frame());
		//super(scn.terseHandler(), n);
		setDefaultGrabber(scn.pinhole().frame());
		//camProfile = new GenericMotionProfile<Constants.DOF_6Action>();
		//profile = new GenericMotionProfile<Constants.DOF_6Action>();
		//clickProfile = new GenericClickProfile<Constants.DOF_0Action>();
		sens = new float[6];
		sens[0] = 1f;
		sens[1] = 1f;
		sens[2] = 1f;
		sens[3] = 1f;
		sens[4] = 1f;
		sens[5] = 1f;
		cameraProfile().setBinding(DOF_6Action.NATURAL);
		frameProfile().setBinding(DOF_6Action.NATURAL);
	}
	
	@Override
	public DOF6Event feed() {
		return null;
	}
	
	@Override
	public GenericMotionProfile<Constants.DOF_6Action> cameraProfile() {
		return camProfile;
	}
	
	@Override
	public GenericMotionProfile<Constants.DOF_6Action> frameProfile() {
		return profile;
	}
	
	/**
	@Override
	public GenericClickProfile<Constants.DOF_0Action> clickProfile() {
		return clickProfile;
	}
	*/
	
	@Override
	public boolean addInPool(Grabbable deviceGrabber) {
		if(deviceGrabber == null)
			return false;
		if( (deviceGrabber instanceof InteractiveFrame) && !(deviceGrabber instanceof InteractiveCameraFrame) )
			if (!isInPool(deviceGrabber)) {
				deviceGrabberPool().add(deviceGrabber);
				return true;
			}
		return false;
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
	
	public void setSensitivities(float x, float y, float z, float rx, float ry, float rz) {
		sens[0] = x;
		sens[1] = y;
		sens[2] = z;
		sens[3] = rx;
		sens[4] = ry;
		sens[5] = rz;
	}
}
