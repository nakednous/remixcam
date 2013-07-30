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
import remixlab.tersehandling.generic.event.GenericDOF1Event;
import remixlab.tersehandling.generic.profile.GenericMotionProfile;
import remixlab.tersehandling.generic.profile.Duoable;
import remixlab.tersehandling.core.EventGrabberTuple;
import remixlab.tersehandling.event.*;

public class GenericWheeledMouseAgent<W extends GenericMotionProfile<Constants.DOF1Action>> extends MouseAgent {
	protected W frameWheelProfile;
	protected W wheelProfile;	
	
	public GenericWheeledMouseAgent(W fwProfile, W wProfile, AbstractScene scn, String n) {
		super(scn, n);
		frameWheelProfile = fwProfile;
		wheelProfile = wProfile;
		wheelProfile().setBinding(DOF1Action.ZOOM);
		frameWheelProfile().setBinding(DOF1Action.ZOOM);
	}
	
	public W wheelProfile() {
		return wheelProfile;
	}
	
	public void setWheelProfile(W profile) {
		wheelProfile = profile;
	}
	
	public W frameWheelProfile() {
		return frameWheelProfile;
	}
	
	public void setFrameWheelProfile(W profile) {
		frameWheelProfile = profile;
	}
	
	/**
	@Override
	public void handle(TerseEvent event) {
		//Warning: should be copy pasted from AbstractMotionDevice
		if(event == null || !handler.isAgentRegistered(this))	return;
		if( grabber() != null )
			if(!( grabber() instanceof InteractiveFrame )) {
				handler.enqueueEventTuple(new EventGrabberTuple(event, grabber()));
				return;
			}
		if(event instanceof Duoable<?>) {		
			if(event instanceof ClickEvent)
				handler.enqueueEventTuple(new EventGrabberDuobleTuple(event, clickProfile().handle((Duoable<?>)event), grabber()));
			else
				if(event instanceof MotionEvent) {
					((MotionEvent)event).modulate(sens);
					if(trackedGrabber() != null )
						if( event instanceof GenericDOF1Event )
							handler.enqueueEventTuple(new EventGrabberDuobleTuple(event, frameWheelProfile().handle((Duoable<?>)event), trackedGrabber()));
						else
							handler.enqueueEventTuple(new EventGrabberDuobleTuple(event, frameProfile().handle((Duoable<?>)event), trackedGrabber()));
					else
						if( event instanceof GenericDOF1Event )
							handler.enqueueEventTuple(new EventGrabberDuobleTuple(event, wheelProfile().handle((Duoable<?>)event), defaultGrabber()));
						else
							handler.enqueueEventTuple(new EventGrabberDuobleTuple(event, cameraProfile().handle((Duoable<?>)event), defaultGrabber()));
			}
		}
	}
	// */
	@Override
	public void handle(TerseEvent event) {
		//Warning: should be copy pasted from AbstractMotionDevice
		if(event == null || !handler.isAgentRegistered(this))	return;
		if( grabber() != null )
			if(!( grabber() instanceof InteractiveFrame )) {
				handler.enqueueEventTuple(new EventGrabberTuple(event, grabber()));
				return;
			}
		if(event instanceof Duoable<?>) {		
			if(event instanceof ClickEvent)
				handler.enqueueEventTuple(new EventGrabberDuobleTuple(event, clickProfile().handle((Duoable<?>)event), grabber()));
			else
				if(event instanceof MotionEvent) {
					((MotionEvent)event).modulate(sens);					
					if( grabber() instanceof InteractiveCameraFrame )
						if(trackedGrabber() != null )
							if(event instanceof GenericDOF1Event)
								handler.enqueueEventTuple(new EventGrabberDuobleTuple(event, wheelProfile().handle((Duoable<?>)event), trackedGrabber()));
							else
								handler.enqueueEventTuple(new EventGrabberDuobleTuple(event, cameraProfile().handle((Duoable<?>)event), trackedGrabber()));
						else
							if(event instanceof GenericDOF1Event)
								handler.enqueueEventTuple(new EventGrabberDuobleTuple(event, wheelProfile().handle((Duoable<?>)event), defaultGrabber()));
							else
								handler.enqueueEventTuple(new EventGrabberDuobleTuple(event, cameraProfile().handle((Duoable<?>)event), defaultGrabber()));
					else if( grabber() instanceof InteractiveFrame )
						if(trackedGrabber() != null )
							if(event instanceof GenericDOF1Event)
								handler.enqueueEventTuple(new EventGrabberDuobleTuple(event, frameWheelProfile().handle((Duoable<?>)event), trackedGrabber()));
							else
								handler.enqueueEventTuple(new EventGrabberDuobleTuple(event, frameProfile().handle((Duoable<?>)event), trackedGrabber()));
						else
							if(event instanceof GenericDOF1Event)
								handler.enqueueEventTuple(new EventGrabberDuobleTuple(event, frameWheelProfile().handle((Duoable<?>)event), defaultGrabber()));
							else
								handler.enqueueEventTuple(new EventGrabberDuobleTuple(event, frameProfile().handle((Duoable<?>)event), defaultGrabber()));
			}
		}
	}
}
