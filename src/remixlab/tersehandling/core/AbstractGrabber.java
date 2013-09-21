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

/**
 * Default implementation of the Grabbable interface which eases
 * implementation by simply overriding {@link #grabsAgent(Agent)}.
 * 
 * @author pierre
 */
public abstract class AbstractGrabber implements Grabbable {
	@Override
	public boolean grabsAgent(Agent agent) {
		return agent.grabber() == this;
	}
}
