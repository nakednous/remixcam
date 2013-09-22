/*******************************************************************************
 * TerseHandling (version 0.9.50)
 * Copyright (c) 2013 Jean Pierre Charalambos.
 * @author Jean Pierre Charalambos
 * https://github.com/remixlab
 *   
 * All rights reserved. Library that eases the creation of interactive
 * scenes released under the terms of the GNU Public License v3.0
 * which is available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
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
