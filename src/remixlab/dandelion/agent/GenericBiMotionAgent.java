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

import remixlab.dandelion.core.Constants;
import remixlab.tersehandling.core.TerseHandler;
import remixlab.tersehandling.generic.agent.GenericMotionAgent;
import remixlab.tersehandling.generic.profile.GenericClickProfile;
import remixlab.tersehandling.generic.profile.GenericMotionProfile;
import remixlab.tersehandling.generic.profile.Duoable;
import remixlab.tersehandling.event.*;

public class GenericBiMotionAgent<P extends GenericMotionProfile<?>> extends GenericMotionAgent<P, GenericClickProfile<Constants.DOF0Action>> {
	protected P camProfile;
	
	public GenericBiMotionAgent(P fProfile, P cProfile, GenericClickProfile<Constants.DOF0Action> c, TerseHandler scn, String n) {
		super(fProfile, c, scn, n);
		camProfile = cProfile;
	}
	
	public P cameraProfile() {
		return camProfile;
	}
	
	public P frameProfile() {
		return profile();
	}
	
	public GenericClickProfile<Constants.DOF0Action> clickProfile() {
		return clickProfile;
	}
	
	public void setCameraProfile(P profile) {
		camProfile = profile;
	}
	
	public void setFrameProfile(P profile) {
		setProfile(profile);
	}
	
	public void setClickProfile(GenericClickProfile<Constants.DOF0Action> profile) {
		clickProfile = profile;
	}
	
	@Override
	public void handle(TerseEvent event) {
		//overkill but feels safer ;)
		if(event == null || !handler.isAgentRegistered(this))	return;		
		if(event instanceof Duoable<?>) {
			if(event instanceof ClickEvent)
				handler.enqueueEventTuple(new EventGrabberDuobleTuple(event, clickProfile().handle((Duoable<?>)event), grabber()));
			else
				if(event instanceof MotionEvent) {
					((MotionEvent)event).modulate(sens);
					if (trackedGrabber() != null )
						handler.enqueueEventTuple(new EventGrabberDuobleTuple(event, frameProfile().handle((Duoable<?>)event), trackedGrabber()));						
					else 
						handler.enqueueEventTuple(new EventGrabberDuobleTuple(event, cameraProfile().handle((Duoable<?>)event), defaultGrabber()));			
			}
		}
	}	
}