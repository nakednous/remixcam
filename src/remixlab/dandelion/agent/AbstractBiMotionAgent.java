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

import remixlab.tersehandling.core.BasicScene;
import remixlab.tersehandling.duoable.agent.AbstractMotionAgent;
import remixlab.tersehandling.duoable.profile.AbstractClickProfile;
import remixlab.tersehandling.duoable.profile.AbstractMotionProfile;
import remixlab.tersehandling.duoable.profile.Duoble;
import remixlab.tersehandling.event.*;

public abstract class AbstractBiMotionAgent extends AbstractMotionAgent {
	protected AbstractMotionProfile<?> camProfile;
	protected float[] sens;
	
	public AbstractBiMotionAgent(BasicScene scn, String n) {
		super(scn, n);	
	}
	
	public AbstractMotionProfile<?> cameraProfile() {
		return camProfile;
	}
	
	public AbstractMotionProfile<?> frameProfile() {
		return (AbstractMotionProfile<?>) profile();
	}
	
	public AbstractClickProfile<?> clickProfile() {
		return clickProfile;
	}
	
	public void setCameraProfile(AbstractMotionProfile<?>	profile) {
		camProfile = profile;
	}
	
	public void setFrameProfile(AbstractMotionProfile<?> profile) {
		setProfile(profile);
	}
	
	public void setClickProfile(AbstractClickProfile<?> profile) {
		clickProfile = profile;
	}
	
	public float [] sensitivities() {
		return sens;
	}
	
	@Override
	public void handle(GenericEvent event) {
		//overkill but feels safer ;)
		if(event == null || !scene.isAgentRegistered(this))	return;		
		if(event instanceof Duoble<?>) {
			if(event instanceof GenericClickEvent)
				scene.enqueueEventTuple(new EventGrabberDuobleTuple(event, clickProfile().handle((Duoble<?>)event), grabber()));
			else
				if(event instanceof GenericMotionEvent) {
					((GenericMotionEvent)event).modulate(sens);
					if (trackedGrabber() != null )
						scene.enqueueEventTuple(new EventGrabberDuobleTuple(event, frameProfile().handle((Duoble<?>)event), trackedGrabber()));						
					else 
						scene.enqueueEventTuple(new EventGrabberDuobleTuple(event, cameraProfile().handle((Duoble<?>)event), defaultGrabber()));			
			}
		}
	}	
}
