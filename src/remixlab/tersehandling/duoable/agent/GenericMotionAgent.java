/**
 *                  TerseHandling (version 0.70.0)      
 *           Copyright (c) 2013 by Jean Pierre Charalambos
 *                 @author Jean Pierre Charalambos      
 *              https://github.com/nakednous/remixcam
 *                           
 * This library provides classes to ease the creation of interactive scenes.
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

package remixlab.tersehandling.duoable.agent;

import remixlab.tersehandling.core.TerseHandler;
import remixlab.tersehandling.duoable.profile.GenericClickProfile;
import remixlab.tersehandling.duoable.profile.GenericProfile;
import remixlab.tersehandling.duoable.profile.Duoable;
import remixlab.tersehandling.event.*;

public class GenericMotionAgent<P extends GenericProfile<?,?>, C extends GenericClickProfile<?>> extends GenericActionableAgent<P> {
	protected C clickProfile;
	protected float[] sens;
	
	public GenericMotionAgent(P p, C c, TerseHandler scn, String n) {
		super(p, scn, n);
		clickProfile = c;
		sens = new float[] {1f, 1f, 1f, 1f, 1f, 1f};
	}
	
	public P motionProfile() {
		return profile();
	}
	
	public void setMotionProfile(P profile) {
		setProfile(profile);
	}
	
	public C clickProfile() {
		return clickProfile;
	}
	
	public void setClickProfile(C profile) {
		clickProfile = profile;
	}
	
	public void setSensitivities(float x) {
		setSensitivities(x,0,0,0,0,0);
	}
	
	public void setSensitivities(float x, float y) {
		setSensitivities(x,y,0,0,0,0);
	}
	
	public void setSensitivities(float x, float y, float z) {
		setSensitivities(x,y,z,0,0,0);
	}
	
	public void setSensitivities(float x, float y, float z, float rx, float ry,	float rz) {
		sens[0] = x;
		sens[1] = y;
		sens[2] = z;
		sens[3] = rx;
		sens[4] = ry;
		sens[5] = rz;
	}
	
	public float [] sensitivities() {
		return sens;
	}
	
	@Override
	public void handle(BaseEvent event) {
		//overkill but feels safer ;)
		if(event == null || !handler.isAgentRegistered(this))	return;		
		if(event instanceof Duoable<?>) {
			if(event instanceof ClickEvent)
				handler.enqueueEventTuple(new EventGrabberDuobleTuple(event, clickProfile().handle((Duoable<?>)event), grabber()));
			else
				if(event instanceof MotionEvent) {
					((MotionEvent)event).modulate(sens);
					if (grabber() != null )
						handler.enqueueEventTuple(new EventGrabberDuobleTuple(event, motionProfile().handle((Duoable<?>)event), grabber()));			
			}
		}
	}	
}