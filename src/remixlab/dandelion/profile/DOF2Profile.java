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

package remixlab.dandelion.profile;

import java.util.Map;

import remixlab.dandelion.core.*;
import remixlab.duoable.profile.AbstractMotionProfile;
import remixlab.duoable.profile.Bindings;
import remixlab.tersehandling.shortcut.ButtonShortcut;

public class DOF2Profile extends AbstractMotionProfile<Constants.DOF_2Action> implements Constants {
	public DOF2Profile() {
		super();
	}
	
	protected DOF2Profile(DOF2Profile other) {
		bindings = new Bindings<ButtonShortcut, DOF_2Action>();    
    for (Map.Entry<ButtonShortcut, DOF_2Action> entry : other.bindings.map().entrySet()) {
    	ButtonShortcut key = entry.getKey().get();
    	DOF_2Action value = entry.getValue();
    	bindings.setBinding(key, value);
    }
	}
	
  @Override
	public DOF2Profile get() {
		return new DOF2Profile(this);
	}
  
  /**
	@Override
	public void handle(DLEvent<DOF_2Action> e) {
		//super.handle((DLKeyEvent)e);
		super.handle(e);
	}
	*/
  
	/**
	public DOF2Profile(AbstractScene scn, String n) {
		super(scn, n);
	}
	*/
}