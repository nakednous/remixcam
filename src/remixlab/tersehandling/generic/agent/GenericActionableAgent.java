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

import java.util.LinkedList;

import remixlab.tersehandling.core.Agent;
import remixlab.tersehandling.core.EventGrabberTuple;
import remixlab.tersehandling.core.Grabbable;
import remixlab.tersehandling.core.TerseHandler;
import remixlab.tersehandling.event.TerseEvent;
import remixlab.tersehandling.generic.profile.Actionable;
import remixlab.tersehandling.generic.profile.Duoable;
import remixlab.tersehandling.generic.profile.GenericProfile;

public class GenericActionableAgent<P extends GenericProfile<?,?>> extends Agent {
	public class EventGrabberDuobleTuple extends EventGrabberTuple {
		public EventGrabberDuobleTuple(TerseEvent e, Actionable<?> a, Grabbable g) {
	  	super(e,g);
	  	if(event instanceof Duoable)
	  		((Duoable<?>)event).setAction(a);
	  	else
	  		System.out.println("Action will not be handled by grabber using this event type. Supply a Duoble event");
	  }
		
		@Override
		public boolean enqueue(LinkedList<EventGrabberTuple> queue) {
			if( event().isNull() )
				return false;
			if(event instanceof Duoable) {				
				if( ((Duoable<?>)event).action() != null ) {
					queue.add(this);
					return true;
				}
				else
					return false;
			}		
			else
				return super.enqueue(queue);	  	
	  }
	}
	
	protected P profile;
	
	public GenericActionableAgent(P p, TerseHandler scn, String n) {
		super(scn, n);
		profile = p;
	}

	public P profile() {
		return profile;
	}
	
	public void setProfile(P p) {
		profile = p;
	}
	
	protected boolean foreignGrabber() {
		return false;
	}
	
	@Override
	public String info() {
		String description = new String();
		description += name();
		description += "\n";
		if( profile().description().length() != 0 ) {
			description += "Shortcuts\n";
			description += profile().description();
		}
		return description;
	}
	
	@Override
	public void handle(TerseEvent event) {		
		//overkill but feels safer ;)
		if(event == null || !handler.isAgentRegistered(this) || grabber() == null) return;
		if(event instanceof Duoable<?>)
			if( foreignGrabber() )
				enqueueEventTuple(new EventGrabberTuple(event, grabber()));
			else
				enqueueEventTuple(new EventGrabberDuobleTuple(event, profile().handle((Duoable<?>)event), grabber()));
	}
}
