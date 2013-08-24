/**
 *                  TerseHandling (version 0.70.0)
 *           Copyright (c) 2013 by Jean Pierre Charalambos
 *                 @author Jean Pierre Charalambos
 *             https://github.com/nakednous/tersehandling
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

package remixlab.tersehandling.generic.agent;

import remixlab.tersehandling.core.*;
import remixlab.tersehandling.event.*;
import remixlab.tersehandling.generic.event.*;
import remixlab.tersehandling.generic.profile.*;

public class GenericWheeledMotionAgent<W extends GenericMotionProfile<?>,
									   M extends GenericMotionProfile<?>,
									   C extends GenericClickProfile<?>> extends GenericMotionAgent<M,C> {
	
	protected W wheelProfile;
	
	public GenericWheeledMotionAgent(W w, M p, C c, TerseHandler scn, String n) {
		super(p, c, scn, n);
		wheelProfile = w;
	}
	
	public W wheelProfile() {
		return wheelProfile;
	}
	
	public void setWheelProfile(W profile) {
		wheelProfile = profile;
	}
	
	@Override
	public String info() {
		String description = new String();
		description += name();
		description += "\n";
		if( clickProfile().description().length() != 0 ) {
			description += "Click shortcuts\n";
			description += clickProfile().description();
		}
		if( motionProfile().description().length() != 0 ) {
			description += "Motion shortcuts\n";
			description += motionProfile().description();
		}
		if( wheelProfile().description().length() != 0 ) {
			description += "Wheel shortcuts\n";
			description += wheelProfile().description();
		}
		return description;
	}
	
	@Override
	public void handle(TerseEvent event) {
		//overkill but feels safer ;)
		if(event == null || !handler.isAgentRegistered(this) || grabber() == null) return;		
		if(event instanceof Duoable<?>) {
			if(event instanceof ClickEvent)
				if( foreignGrabber() )
					handler.enqueueEventTuple(new EventGrabberTuple(event, grabber()));
				else
					handler.enqueueEventTuple(new EventGrabberDuobleTuple(event, clickProfile().handle((Duoable<?>)event), grabber()));
			else
				if(event instanceof MotionEvent) {
					((MotionEvent)event).modulate(sens);
					if( foreignGrabber() )
						handler.enqueueEventTuple(new EventGrabberTuple(event, grabber()));
					else
						if( event instanceof GenericDOF1Event )
							handler.enqueueEventTuple(new EventGrabberDuobleTuple(event, wheelProfile().handle((Duoable<?>)event), grabber()));
						else
							handler.enqueueEventTuple(new EventGrabberDuobleTuple(event, motionProfile().handle((Duoable<?>)event), grabber()));			
			}
		}
	}
}
