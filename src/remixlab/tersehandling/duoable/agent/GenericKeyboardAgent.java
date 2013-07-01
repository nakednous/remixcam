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
import remixlab.tersehandling.duoable.profile.GenericKeyboardProfile;
import remixlab.tersehandling.duoable.profile.Duoable;
import remixlab.tersehandling.duoable.profile.KeyDuoable;
import remixlab.tersehandling.event.BaseEvent;

public class GenericKeyboardAgent<K extends GenericKeyboardProfile<?>> extends GenericActionableAgent<K> {	
	public GenericKeyboardAgent(K k, TerseHandler scn, String n) {
		super(k, scn, n);
	}
	
	public K keyboardProfile() {
		return profile();
	}

	public void setKeyboardProfile(K kprofile) {
		setProfile(profile);
	}
	
	@Override
	public void handle(BaseEvent event) {
		if(event == null || !handler.isAgentRegistered(this)) return;
		if(event instanceof Duoable<?>)
			handler.enqueueEventTuple(new EventGrabberDuobleTuple(event, keyboardProfile().handle((Duoable<?>)event), grabber()));
	}
	
	public void handleKey(BaseEvent event) {
		if(event == null || !handler.isAgentRegistered(this)) return;	
		if(event instanceof KeyDuoable<?>)
			handler.enqueueEventTuple(new EventGrabberDuobleTuple(event, keyboardProfile().handleKey((KeyDuoable<?>)event), grabber()));
	}
}
