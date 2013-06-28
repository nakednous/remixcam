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
import remixlab.duoable.profile.AbstractKeyboardProfile;
import remixlab.duoable.profile.Bindings;
import remixlab.tersehandling.shortcut.*;

public class KeyboardProfile extends AbstractKeyboardProfile<Constants.DOF_0Action> implements Constants {
	public KeyboardProfile() {
		super();
	}
	
	protected KeyboardProfile(KeyboardProfile other) {
		bindings = new Bindings<KeyboardShortcut, DOF_0Action>();    
    for (Map.Entry<KeyboardShortcut, DOF_0Action> entry : other.bindings.map().entrySet()) {
    	KeyboardShortcut key = entry.getKey().get();
    	DOF_0Action value = entry.getValue();
    	bindings.setBinding(key, value);
    }
	}
	
  @Override
	public KeyboardProfile get() {
		return new KeyboardProfile(this);
	}
}
