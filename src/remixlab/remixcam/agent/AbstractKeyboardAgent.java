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

import remixlab.remixcam.core.AbstractScene;
import remixlab.remixcam.core.Duoble;
import remixlab.remixcam.core.Grabbable;
import remixlab.remixcam.core.KeyDuoble;
import remixlab.remixcam.event.GenericEvent;
import remixlab.remixcam.profile.AbstractKeyboardProfile;

public class AbstractKeyboardAgent extends AbstractActionableAgent {
	//protected AbstractKeyboardProfile<?> profile;
	
	public AbstractKeyboardAgent(AbstractScene scn, String n) {
		super(scn, n);
	}
	
	public AbstractKeyboardProfile<?> keyboardProfile() {
		return (AbstractKeyboardProfile<?>) profile();
	}

	public void setKeyboardProfile(AbstractKeyboardProfile<?> kprofile) {
		setProfile(profile);
	}
	
	@Override
	public boolean updateGrabber(GenericEvent event) {
		if( event == null || !scene.isAgentRegistered(this) )
			return false;
		
		if(isGrabberEnforced())	return false;
		
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
	
	@Override
	public void handle(GenericEvent event) {
		if(event == null || !scene.isAgentRegistered(this)) return;
		if(updateGrabber(event)) return;
		if(event instanceof Duoble<?>)
			scene.enqueueEventTuple(new EventGrabberDuobleTuple(event, keyboardProfile().handle((Duoble<?>)event), deviceGrabber()));
	}
	
	public void handleKey(GenericEvent event) {
		if(event == null || !scene.isAgentRegistered(this)) return;
		if(updateGrabber(event)) return;		
		if(event instanceof KeyDuoble<?>)
			scene.enqueueEventTuple(new EventGrabberDuobleTuple(event, keyboardProfile().handleKey((KeyDuoble<?>)event), deviceGrabber()));
	}
}
