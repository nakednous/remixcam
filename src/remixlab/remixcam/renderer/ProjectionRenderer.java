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

package remixlab.remixcam.renderer;

import remixlab.remixcam.core.*;
import remixlab.remixcam.geom.*;

public abstract class ProjectionRenderer extends Renderer implements Constants {
	public ProjectionRenderer(AbstractScene scn, Drawerable dw) {
		super(scn, dw);
	}
	
	@Override
	public void bindMatrices() {
		setProjectionMatrix();
		setModelViewMatrix();
		scene.pinhole().cacheProjViewInvMat();
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
	
	protected DLMatrix get2DOrtho(float left, float right, float bottom, float top, float near, float far) {
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
		return new DLMatrix(x,  0,  0,  0,
				                0, -y,  0,  0,
				                0,  0,  z,  0,
				                tx, ty, tz, 1);
	}
	
	protected DLMatrix get2DModelView() {
		 return get2DModelView(scene().width()/2f, scene.height()/2f, (scene().height()/2f) / (float)Math.tan(PI*60 / 360), scene().width()/2f, scene.height()/2f, 0, 0, 1, 0);
	}
	
	protected DLMatrix get2DModelView(float eyeX, float eyeY, float eyeZ,
     float centerX, float centerY, float centerZ,
     float upX, float upY, float upZ) {
				
		// Calculating Z vector
		float z0 = eyeX - centerX;
		float z1 = eyeY - centerY;
		float z2 = eyeZ - centerZ;
		float mag = (float) Math.sqrt(z0 * z0 + z1 * z1 + z2 * z2);
		if (Geom.nonZero(mag)) {
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
		if (Geom.nonZero(mag)) {
			x0 /= mag;
			x1 /= mag;
			x2 /= mag;
		}
		
		mag = (float) Math.sqrt(y0 * y0 + y1 * y1 + y2 * y2);
		if (Geom.nonZero(mag)) {
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
		DLMatrix mv = new DLMatrix(x0, y0, z0, 0,
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
