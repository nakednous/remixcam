/*******************************************************************************
 * Dandelion (version 0.9.50)
 * Copyright (c) 2013 Jean Pierre Charalambos.
 * @author Jean Pierre Charalambos
 * https://github.com/remixlab
 * 
 * All rights reserved. Library that eases the creation of interactive
 * scenes released under the terms of the GNU Public License v3.0
 * which available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package remixlab.dandelion.renderer;

import remixlab.dandelion.core.*;
import remixlab.dandelion.geom.*;
import remixlab.tersehandling.core.Util;

public abstract class ProjectionRenderer extends Renderer implements Constants {
	public ProjectionRenderer(AbstractScene scn, Depictable dw) {
		super(scn, dw);
	}
	
	@Override
	public void bindMatrices() {
		setProjectionMatrix();
		setModelViewMatrix();
		scene.viewport().cacheProjViewInvMat();
	}
	
	@Override
	public void beginScreenDrawing() {
		pushProjection();
    float cameraZ = (height()/2.0f) / (float) Math.tan(QUARTER_PI/2.0f);
    float cameraNear = cameraZ / 2.0f;
    float cameraFar = cameraZ * 2.0f;
    setProjection(get2DOrtho(-width()/2, width()/2, -height()/2, height()/2, cameraNear, cameraFar));
    pushMatrix();
    setMatrix(get2DModelView());
    
    /**
		pushProjection();
		float cameraZ = (pg3d().height/2.0f) / PApplet.tan( scene().camera().fieldOfView() /2.0f);
    float cameraNear = cameraZ / 2.0f;
    float cameraFar = cameraZ * 2.0f;
    ortho(0, scene.width(), 0, scene.height(), cameraNear, cameraFar);	
    pushMatrix();
	  // Camera needs to be reset!
    pg3d().camera(); 
    // */
	}
	
	@Override
	public void endScreenDrawing() {
		popProjection();  
		popMatrix();
	}
	
	protected Mat get2DOrtho(float left, float right, float bottom, float top, float near, float far) {
		float x = +2.0f / (right - left);
		float y = +2.0f / (top - bottom);
		float z = -2.0f / (far - near);
		
		float tx = -(right + left) / (right - left);
		float ty = -(top + bottom) / (top - bottom);
		float tz = -(far + near)   / (far - near);
		
		// The minus sign is needed to invert the Y axis.
		/**
		return new PMatrix3D(x,  0, 0, tx,
                         0, -y, 0, ty,
                         0,  0, z, tz,
                         0,  0, 0,  1);
                         */
		return new Mat(x,  0,  0,  0,
				                0, -y,  0,  0,
				                0,  0,  z,  0,
				                tx, ty, tz, 1);
	}
	
	protected Mat get2DModelView() {
		 return get2DModelView(scene().width()/2f, scene.height()/2f, (scene().height()/2f) / (float)Math.tan(PI*60 / 360), scene().width()/2f, scene.height()/2f, 0, 0, 1, 0);
	}
	
	protected Mat get2DModelView(float eyeX, float eyeY, float eyeZ,
     float centerX, float centerY, float centerZ,
     float upX, float upY, float upZ) {
				
		// Calculating Z vector
		float z0 = eyeX - centerX;
		float z1 = eyeY - centerY;
		float z2 = eyeZ - centerZ;
		float mag = (float) Math.sqrt(z0 * z0 + z1 * z1 + z2 * z2);
		if (Util.nonZero(mag)) {
			z0 /= mag;
			z1 /= mag;
			z2 /= mag;
		}
		/**
		cameraEyeX = eyeX;
		cameraEyeY = eyeY;
		cameraEyeZ = eyeZ;
		*/
		
		// Calculating Y vector
		float y0 = upX;
		float y1 = upY;
		float y2 = upZ;
		
		// Computing X vector as Y cross Z
		float x0 =  y1 * z2 - y2 * z1;
		float x1 = -y0 * z2 + y2 * z0;
		float x2 =  y0 * z1 - y1 * z0;
		
		// Recompute Y = Z cross X
		y0 =  z1 * x2 - z2 * x1;
		y1 = -z0 * x2 + z2 * x0;
		y2 =  z0 * x1 - z1 * x0;
		
		// Cross product gives area of parallelogram, which is < 1.0 for
		// non-perpendicular unit-length vectors; so normalize x, y here:
		mag = (float) Math.sqrt(x0 * x0 + x1 * x1 + x2 * x2);
		if (Util.nonZero(mag)) {
			x0 /= mag;
			x1 /= mag;
			x2 /= mag;
		}
		
		mag = (float) Math.sqrt(y0 * y0 + y1 * y1 + y2 * y2);
		if (Util.nonZero(mag)) {
			y0 /= mag;
			y1 /= mag;
			y2 /= mag;
		}
		
		/**
		modelview.set(x0, x1, x2, 0,
				          y0, y1, y2, 0,
                 z0, z1, z2, 0,
                  0,  0,  0, 1);
					
		float tx = -eyeX;
		float ty = -eyeY;
		float tz = -eyeZ;
		modelview.translate(tx, ty, tz);			
		
		modelviewInv.set(modelview);
		modelviewInv.invert();
		
		camera.set(modelview);
		cameraInv.set(modelviewInv);
		
		calcProjmodelview();
		*/			
		
		/**
		PMatrix3D mv = new PMatrix3D(x0, x1, x2, 0,
                                y0, y1, y2, 0,
                                z0, z1, z2, 0,
                                 0,  0,  0, 1);
                                 */
		Mat mv = new Mat(x0, y0, z0, 0,
				                       x1, y1, z1, 0,
				                       x2, y2, z2, 0,
				                        0,  0,  0, 1);
		
		float tx = -eyeX;
		float ty = -eyeY;
		float tz = -eyeZ;
		
		mv.translate(tx, ty, tz);
		
		return mv;
	}
}
