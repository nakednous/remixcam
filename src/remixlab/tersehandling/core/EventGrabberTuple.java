/*******************************************************************************
 * TerseHandling (version 0.9.50)
 * Copyright (c) 2013 Jean Pierre Charalambos.
 * @author Jean Pierre Charalambos
 * https://github.com/remixlab
 * 
 * All rights reserved. Library that eases the creation of interactive
 * scenes released under the terms of the GNU Public License v3.0
 * which available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package remixlab.tersehandling.core;

import java.util.LinkedList;

import remixlab.tersehandling.event.TerseEvent;

public class EventGrabberTuple {
	protected TerseEvent event;
	protected Grabbable grabber;
	
	public EventGrabberTuple(TerseEvent e, Grabbable g) {
		event = e;
		grabber = g;
	}
	
	public boolean perform() {
  	if(grabber != null) {
  		grabber.performInteraction(event);
  		return true;
  	}
  	return false;
  }
  
  public TerseEvent event() {
  	return event;
  }
  
  public Grabbable grabber() {
  	return grabber;
  }
  
  public boolean enqueue(LinkedList<EventGrabberTuple> queue) {
  	if (!event().isNull()) {
  		queue.add(this);
  		return true;
  	}
  	return false;
  }
}
