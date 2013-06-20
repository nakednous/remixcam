/**
 *                     RemixCam (version 0.70.0)      
 *      Copyright (c) 2013 by National University of Colombia
 *                 @author Jean Pierre Charalambos      
 *              https://github.com/nakednous/remixcam
 *                           
 * This java library provides classes to ease the creation of interactive 3D
 * scenes in various languages and frameworks such as JOGL, WebGL and Processing.
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

package remixlab.remixcam.device;

import remixlab.remixcam.core.*;
import remixlab.remixcam.event.*;
import remixlab.remixcam.interactivity.ClickEvent;
import remixlab.remixcam.profile.*;

public abstract class AbstractMotionDevice extends AbstractDevice implements Constants {
	protected AbstractMotionProfile<?> camProfile, frameProfile;
	protected AbstractClickProfile<?> clickProfile;
	protected float[] sens;
	
	public AbstractMotionDevice(AbstractScene scn, String n) {
		super(scn, n);	
	}
	
	public AbstractMotionProfile<?> cameraProfile() {
		return camProfile;
	}
	
	public AbstractMotionProfile<?> frameProfile() {
		return camProfile;
	}
	
	public AbstractClickProfile<?> clickProfile() {
		return clickProfile;
	}
	
	public void setCameraProfile(AbstractMotionProfile<?>	profile) {
		camProfile = profile;
	}
	
	public void setFrameProfile(AbstractMotionProfile<?> profile) {
		frameProfile = profile;
	}
	
	public void setClickProfile(AbstractClickProfile<?> profile) {
		clickProfile = profile;
	}
	
	public float [] sensitivities() {
		return sens;
	}
	
	// /**
	@Override
	public void handle(GenericEvent<?> event) {
		if(event == null)	return;
		event.setAgent(this);
		if( event instanceof ClickEvent )
			clickProfile.handle(event);
		else {
			if(scene.aliveInteractiveFrame() != null)
				frameProfile.handle(event);
			else
				camProfile.handle(event);
			((GenericMotionEvent<?>)event).modulate(sens);
		}
		if( scene.isDeviceRegistered(this) ) event.enqueue(scene);
	}
	// */
	
	 /**
	@Override
	public void handle(DLEvent event) {
		if( event instanceof DLClickEvent )
			clickProfile.handle(event);
		else
			if(scene.activeFrame() != null)
				frameProfile.handle(event);
			else
				camProfile.handle(event);
		event.enqueue(scene);
	}
	// */
}
