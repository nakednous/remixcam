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

package remixlab.tersehandling.duoable.profile;

import java.util.Map;

import remixlab.tersehandling.core.Copyable;
import remixlab.tersehandling.core.EventConstants;
import remixlab.tersehandling.shortcut.*;

public class GenericProfile<K extends Shortcut, A extends Actionable<?>> implements EventConstants, Copyable {
	/**
	protected AbstractScene scene;
	protected String nm;
	*/
	protected Bindings<K, A> bindings;
	
	public GenericProfile() {
		bindings = new Bindings<K, A>();
	}
	
	protected GenericProfile(GenericProfile<K, A> other) {
		bindings = new Bindings<K, A>();    
    for (Map.Entry<K, A> entry : other.bindings.map().entrySet()) {
    	K key = entry.getKey();
    	A value = entry.getValue();
    	bindings.setBinding(key, value);
    }
	}
	
	@Override
	public GenericProfile<K,A> get() {
		return new GenericProfile<K, A>(this);
	}
	
	/**
	public void handle(DLEvent<A> e) {
		if(e != null)
			e.setAction(binding(e.shortcut()));
	}
	// */
	
	/**
	//TODO testing
	public Duoble<A> handle(Duoble<A> event) {
		if(event != null)
			event.setAction(binding(event.shortcut()));
		return event;
	}	
	// */
	
	public Actionable<?> handle(Duoable<?> event) {
		if(event != null)
			return binding(event.shortcut());
		return null;
	}	

	public String bindingsDescription() {
		return bindings.description();
	}
	
	/**
	 * Removes all camera keyboard shortcuts.
	 */
	public void removeAllBindings() {
		bindings.removeAllBindings();
	}
	
	public Actionable<?> binding(Shortcut k) {
  	return bindings.binding(k);
  }
}
