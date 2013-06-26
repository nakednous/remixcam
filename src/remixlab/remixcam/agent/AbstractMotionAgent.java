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

package remixlab.remixcam.agent;

import remixlab.remixcam.core.*;
import remixlab.remixcam.event.*;
import remixlab.remixcam.profile.*;

public abstract class AbstractMotionAgent extends AbstractAgent implements Constants {
	protected AbstractMotionProfile<?> camProfile, frameProfile;
	protected AbstractClickProfile<?> clickProfile;
	protected float[] sens;
	
	public AbstractMotionAgent(AbstractScene scn, String n) {
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
	
	@Override
	public boolean updateGrabber(GenericEvent event) {
		if( event == null )
			return false;
		
	  // fortunately selection mode doesn't need parsing
		if( ((Duoble<?>)event).getAction() != null ) {
			if(((Duoble<?>)event).getAction().action() == ((Duoble<?>)event).getAction().selectionAction() ||
				 ((Duoble<?>)event).getAction().action() == ((Duoble<?>)event).getAction().deselectionAction()) {
				setDeviceGrabber(null);
				if(((Duoble<?>)event).getAction().action() == ((Duoble<?>)event).getAction().selectionAction()) {
					for (Grabbable mg : deviceGrabberPool()) {
						// take whatever. Here the last one
						mg.checkIfGrabsInput(event);
						if (mg.grabsInput()) setDeviceGrabber(mg);
					}
				}				
				return true;
			}
			return false;
		}
		return false;
	}
	
	// /**
	@Override
	public void handle(GenericEvent event) {		
		//overkill but feels safer ;)
		if(event == null)	return;		
		if(updateGrabber(event)) return;		
		
		if(event instanceof Duoble<?>) {
			if(event instanceof GenericClickEvent)
				scene.enqueueEventTuple(new EventGrabberTuple(event, clickProfile().handle((Duoble<?>)event), deviceGrabber()));
			else
				if(event instanceof GenericMotionEvent) {
					((GenericMotionEvent)event).modulate(sens);
					if (deviceGrabber() != null )
						scene.enqueueEventTuple(new EventGrabberTuple(event, frameProfile().handle((Duoble<?>)event), deviceGrabber()));
					else
						scene.enqueueEventTuple(new EventGrabberTuple(event, cameraProfile().handle((Duoble<?>)event), null));
			}
		}
	}	
}
