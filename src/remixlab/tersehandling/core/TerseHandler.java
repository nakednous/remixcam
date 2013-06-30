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

package remixlab.tersehandling.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

//import remixlab.duoable.profile.Duoble;
import remixlab.tersehandling.event.BaseEvent;

public class TerseHandler {
  //D E V I C E S	  &   E V E N T S
  protected HashMap<String, Agent> agents;
	protected LinkedList<EventGrabberTuple> eventTupleQueue;
	
	public TerseHandler() {
		//agents
		agents = new HashMap<String, Agent>();
		//events
		eventTupleQueue = new LinkedList<EventGrabberTuple>();
	}
	
	// to be called at the end of the main drawing loop
	public void handle() {
		// 1. Agents
		for (Agent device : agents.values())
			device.handle(device.feed());
		
		// 2. Low level events 
		while( !eventTupleQueue.isEmpty() )
    	eventTupleQueue.remove().perform();
	}
	
	// call when grabber is null
	//public abstract void defaultPerformer(GenericEvent e);
	
	/**
	 * Returns an array of the camera profile objects that are currently
	 * registered at the Scene.
	 */
	public Agent [] getAgents() {
		return agents.values().toArray(new Agent[0]);
	}
	
	/**
	 * Adds an HIDevice to the scene.
	 * 
	 * @see #unregisterProfile(HIDevice)
	 * @see #removeAllDevices()
	 */
	public void registerAgent(Agent agent) {
		if(!isAgentRegistered(agent))
			agents.put(agent.name(), agent);
		else {
			System.out.println("Nothing done. A device with the same name is already registered. Current profile names are:");
			for (Agent dev : agents.values())
				System.out.println(dev.name());
		}
	}
	
	public boolean isAgentRegistered(Agent agent) {
		return agents.containsKey(agent.name());
	}
	
	public boolean isAgentRegistered(String name) {
		return agents.containsKey(name);
	}
	
	public Agent getAgent(String name) {
		return agents.get(name);
	}
	
	/**
	 * Removes the device from the scene.
	 * 
	 * @see #registerProfile(HIDevice)
	 * @see #removeAllDevices()
	 */
	public Agent unregisterAgent(Agent device) {
		return agents.remove(device.name());
	}

	public Agent unregisterAgent(String name) {
		return agents.remove(name);
	}
	
	/**
	 * Removes all registered devices from the scene.
	 * 
	 * @see #registerProfile(HIDevice)
	 * @see #unregisterProfile(HIDevice)
	 */
	public void unregisterAllAgents() {
		agents.clear();
	}
	
	/**
	 * Adds an HIDevice to the scene.
	 * 
	 * @see #unregisterProfile(AbstractHIDevice)
	 * @see #removeAllDevices()
	 */
	/**
	public void enqueueEventTuple(EventGrabberTuple eventTuple) {
		if(!eventTupleQueue.contains(eventTuple))
			if( !eventTuple.event().isNull() )
				if( eventTuple instanceof Duoble ) {
					if (((Duoble<?>)eventTuple.event()).getAction() != null)
						eventTupleQueue.add(eventTuple);
				}
				else
					eventTupleQueue.add(eventTuple);
	}
	*/
	
	public LinkedList<EventGrabberTuple> eventTupleQueue() {
		return eventTupleQueue;
	}
	
	public void enqueueEventTuple(EventGrabberTuple eventTuple) {		
		if(!eventTupleQueue.contains(eventTuple))
			eventTuple.enqueue(eventTupleQueue);
	}
	
	/**
	 * Removes the device from the scene.
	 * 
	 * @see #registerProfile(AbstractHIDevice)
	 * @see #removeAllDevices()
	 */
	public void removeEventTuple(BaseEvent event) {
		eventTupleQueue.remove(event);
	}
	
	/**
	 * Removes all registered devices from the scene.
	 * 
	 * @see #registerProfile(AbstractHIDevice)
	 * @see #unregisterProfile(AbstractHIDevice)
	 */
	public void removeAllEventTuples() {
		eventTupleQueue.clear();
	}
	
	// 3. Device grabber handling
	
	/**
	 * Returns a list containing references to all the active MouseGrabbers.
	 * <p>
	 * Used to parse all the MouseGrabbers and to check if any of them
	 * {@link remixlab.tersehandling.core.Grabbable#grabsAgent()} using
	 * {@link remixlab.tersehandling.core.Grabbable#checkIfGrabsDevice(int, int, Camera)}.
	 * <p>
	 * You should not have to directly use this list. Use
	 * {@link #removeFromDeviceGrabberPool(Grabbable)} and
	 * {@link #addInDeviceGrabberPool(Grabbable)} to modify this list.
	 */
	public List<Grabbable> deviceGrabberPool() {
		List<Grabbable> msGrabberPool = new ArrayList<Grabbable>();
		for (Agent device : agents.values())
			for (Grabbable grabber : device.deviceGrabberPool())
				if(!msGrabberPool.contains(grabber))
					msGrabberPool.add(grabber);
				
		return msGrabberPool;
	}
	
	/**
	 * Returns true if the mouseGrabber is currently in the {@link #deviceGrabberPool()} list.
	 * <p>
	 * When set to false using {@link #removeFromDeviceGrabberPool(Grabbable)}, the Scene no longer
	 * {@link remixlab.tersehandling.core.Grabbable#checkIfGrabsDevice(int, int, Camera)} on this mouseGrabber.
	 * Use {@link #addInDeviceGrabberPool(Grabbable)} to insert it back.
	 */
	public boolean isInDeviceGrabberPool(Grabbable deviceGrabber) {
		return deviceGrabberPool().contains(deviceGrabber);
	}
	
	/**
	 * Adds the mouseGrabber in the {@link #deviceGrabberPool()}.
	 * <p>
	 * All created InteractiveFrames (which are MouseGrabbers) are automatically added in the
	 * {@link #deviceGrabberPool()} by their constructors. Trying to add a
	 * mouseGrabber that already {@link #isInDeviceGrabberPool(Grabbable)} has no effect.
	 * <p>
	 * Use {@link #removeFromDeviceGrabberPool(Grabbable)} to remove the mouseGrabber from
	 * the list, so that it is no longer tested with
	 * {@link remixlab.tersehandling.core.Grabbable#checkIfGrabsDevice(int, int, Camera)}
	 * by the Scene, and hence can no longer grab mouse focus. Use
	 * {@link #isInDeviceGrabberPool(Grabbable)} to know the current state of the MouseGrabber.
	 */
	public void addInDeviceGrabberPool(Grabbable deviceGrabber) {
		for (Agent device : agents.values())
			if( !device.isInPool(deviceGrabber) )
				device.addInPool(deviceGrabber);
	}
	
	/**
	public void enforceGrabber(Grabbable deviceGrabber) {
		for (AbstractAgent device : agents.values())
			device.enforceGrabber(deviceGrabber);
	}
	
	public void releaseGrabber() {
		for (AbstractAgent device : agents.values())
			device.unsetGrabber();
	}
	*/

	/**
	 * Removes the mouseGrabber from the {@link #deviceGrabberPool()}.
	 * <p>
	 * See {@link #addInDeviceGrabberPool(Grabbable)} for details. Removing a mouseGrabber
	 * that is not in {@link #deviceGrabberPool()} has no effect.
	 */
	public void removeFromDeviceGrabberPool(Grabbable deviceGrabber) {
		for (Agent device : agents.values())
			device.removeFromPool(deviceGrabber);
	}

	/**
	 * Clears the {@link #deviceGrabberPool()}.
	 * <p>
	 * Use this method only if it is faster to clear the
	 * {@link #deviceGrabberPool()} and then to add back a few MouseGrabbers
	 * than to remove each one independently.
	 */
	public void clearDeviceGrabberPool() {
		for (Agent device : agents.values())
			device.clearPool();
	}
}
