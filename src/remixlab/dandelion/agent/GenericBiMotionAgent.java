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

import remixlab.dandelion.core.AbstractScene;
import remixlab.dandelion.core.Constants;
import remixlab.dandelion.core.InteractiveCameraFrame;
import remixlab.dandelion.core.InteractiveFrame;
import remixlab.tersehandling.core.TerseHandler;
import remixlab.tersehandling.generic.agent.GenericMotionAgent;
import remixlab.tersehandling.generic.profile.GenericClickProfile;
import remixlab.tersehandling.generic.profile.GenericMotionProfile;

public class GenericBiMotionAgent<P extends GenericMotionProfile<?>> extends GenericMotionAgent<P, GenericClickProfile<Constants.DOF2ClickAction>> {
	protected P camProfile;
	protected GenericClickProfile<Constants.DOF2ClickAction> camClickProfile;
	
	public GenericBiMotionAgent(P fProfile, P cProfile, GenericClickProfile<Constants.DOF2ClickAction> c,
																											GenericClickProfile<Constants.DOF2ClickAction> d,
																											TerseHandler scn, String n) {
		super(fProfile, c, scn, n);
		camProfile = cProfile;
		camClickProfile = d;
	}
	
	public P cameraProfile() {
		return camProfile;
	}
	
	public P frameProfile() {
		return profile();
	}
	
	public GenericClickProfile<Constants.DOF2ClickAction> cameraClickProfile() {
		return camClickProfile;
	}
	
	public GenericClickProfile<Constants.DOF2ClickAction> frameClickProfile() {
		return clickProfile;
	}
	
	public void setCameraProfile(P profile) {
		camProfile = profile;
	}
	
	public void setFrameProfile(P profile) {
		setProfile(profile);
	}
	
	@Override
	public void setClickProfile(GenericClickProfile<Constants.DOF2ClickAction> profile) {
		clickProfile = profile;
	}
	
	@Override
	public P motionProfile() {
		if( grabber() instanceof InteractiveCameraFrame )
			return cameraProfile();
		if( grabber() instanceof InteractiveFrame )
			return frameProfile();					
		return null;
	}
	
	@Override
	public GenericClickProfile<Constants.DOF2ClickAction> clickProfile() {
		if( grabber() instanceof InteractiveCameraFrame )
			return cameraClickProfile();
		if( grabber() instanceof InteractiveFrame )
			return frameClickProfile();					
		return null;
	}
	
	@Override
	protected boolean foreignGrabber() {
		return !( grabber() instanceof InteractiveFrame ) && !( grabber() instanceof AbstractScene);
	}
}
