/*******************************************************************************
 * dandelion (version 1.0.0-alpha.1)
 * Copyright (c) 2013 National University of Colombia, https://github.com/remixlab
 * @author Jean Pierre Charalambos, http://otrolado.info/
 *     
 * All rights reserved. Library that eases the creation of interactive
 * scenes, released under the terms of the GNU Public License v3.0
 * which is available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package remixlab.dandelion.helper;

import remixlab.dandelion.core.*;
import remixlab.dandelion.geom.*;

public abstract class MatrixHelper implements MatrixHelpable, Constants {
	protected AbstractScene scene;
	
	public MatrixHelper(AbstractScene scn) {
		scene = scn;
	}
	
	@Override
	public AbstractScene scene() {
		return scene;
	}
	
	public boolean isRightHanded() {
		return scene.isRightHanded();
	}
	
	public boolean isLeftHanded() {
		return scene.isLeftHanded();
	}
	
  public int width() {
  	return scene.width();
  }
  
  public int height() {
  	return scene.height();
  }
	
	@Override
	public void bind() {
		scene.viewport().computeProjection();
		scene.viewport().computeView();
		//scene.viewport().updateProjectionView();

		Vec pos = scene.viewport().position();
		Orientable quat = scene.viewport().frame().orientation();

		if( scene.is2D() ) {
			translate(scene.width() / 2, scene.height() / 2);
			if(scene.isRightHanded()) scale(1,-1);		
			scale(scene.viewport().frame().inverseMagnitude().x(), 
				    scene.viewport().frame().inverseMagnitude().y());		
			rotate(-quat.angle());		
			translate(-pos.x(), -pos.y());
		}
		else {
			//TODO how to handle 3d case without projection, seems impossible
			//setProjection();
			Vec axis = ((Quat)quat).axis();
			// third value took from P5 docs, (see: http://processing.org/reference/camera_.html)
			// Also changed default cam fov (i.e., Math.PI / 3.0f) to match that of P5 (see: http://processing.org/reference/perspective_.html)
			// previously it was: 
			translate(scene.width() / 2, scene.height() / 2,  (scene.height() / 2) / (float) Math.tan(PI / 6));
			if(scene.isRightHanded()) scale(1,-1,1);
			rotate(-quat.angle(), axis.x(), axis.y(), axis.z());		
			translate(-pos.x(), -pos.y(), -pos.z());
		}
	}
	
	@Override
	public void beginScreenDrawing() {
		Vec pos = scene.viewport().position();
		Orientable quat = scene.viewport().frame().orientation();		
		
		pushModelView();
		
		if( scene.is2D() ) {
		  translate(pos.x(), pos.y());
		  rotate(quat.angle());		
		  scale(scene.window().frame().magnitude().x(),
		        scene.window().frame().magnitude().y());
	    if(scene.isRightHanded()) scale(1,-1);
		  translate(-scene.width()/2, -scene.height()/2);			
		}
		else {
			Vec axis = ((Quat)quat).axis();
			translate(pos.x(), pos.y(), pos.z());
			rotate(quat.angle(), axis.x(), axis.y(), axis.z());
			//projection
		  //TODO how to handle 3d case without projection, seems imposible
			//unsetProjection();
		  if(scene.isRightHanded()) scale(1,-1,1);
		  translate(-scene.width() / 2, -scene.height() / 2,  -(scene.height() / 2) / (float) Math.tan(PI / 6));
		}
	}
	
	@Override
	public void endScreenDrawing() {
		popModelView();
	}
	
	@Override
	public Mat getProjection() {
		return scene.viewport().getProjection(false);
	}

	@Override
	public Mat getProjection(Mat target) {
		if (target == null)
			target = new Mat();
		target.set(scene.viewport().getProjection(false));
		return target;
	}
}
