package remixlab.proscene;

import java.util.List;

import processing.core.*;
import processing.opengl.*;

/**
import remixlab.remixcam.core.*;
import remixlab.remixcam.geom.*;
*/

// /*
import remixlab.remixcam.geom.GeomFrame;
import remixlab.remixcam.geom.Matrix3D;
//import remixlab.remixcam.geom.Quaternion;
// */

public class P5Renderer2D extends P5Renderer {	
	public P5Renderer2D(Scene scn, PGraphicsOpenGL renderer) {
		super(scn, renderer, new P5Drawing2D(scn));
	}
	
	@Override
	public boolean is3D() {
		return false;
	}
	
	public PGraphics2D pg2d() {
	  return (PGraphics2D) pg();	
	}	

	@Override
	public void setProjection(Matrix3D source) {
		PMatrix3D pM = new PMatrix3D();
		pM.set(source.getTransposed(new float[16]));		
		pg2d().projection.set(pM);		
	}

	@Override
	public void drawPath(List<GeomFrame> path, int mask, int nbFrames, int nbSteps, float scale) {
		// TODO Auto-generated method stub
		
	}	

	@Override
	public void setMatrix(Matrix3D source) {
		PMatrix3D pM = new PMatrix3D();
		pM.set(source.getTransposed(new float[16]));
		//pg2d().setMatrix(pM);
    pg2d().modelview.set(pM);
	}	

	/**
	 * Sets the processing camera projection matrix from {@link #camera()}. Calls
	 * {@code PApplet.perspective()} or {@code PApplet.orhto()} depending on the
	 * {@link remixlab.remixcam.core.Camera#type()}.
	 */
	@Override
	protected void setProjectionMatrix() {
	  // All options work seemlessly
		/**		
		// Option 1
		Matrix3D mat = new Matrix3D();		
		scene.viewWindow().getProjectionMatrix(mat, true);
		mat.transpose();		
		float[] target = new float[16];
		pg2d().projection.set(mat.get(target));		
		// */	  

		/**		
		// Option 2		
		pg2d().projection.set(scene.viewWindow().getProjectionMatrix(true).getTransposed(new float[16]));
		// */

		// /**
	  // option 3 (new, Andres suggestion)
		//TODO: optimize me set per value basis
		// /**		
		proj = scene.viewWindow().getProjectionMatrix(true);
		pg2d().setProjection(new PMatrix3D( proj.mat[0],  proj.mat[4], proj.mat[8],  proj.mat[12],
	                                      proj.mat[1],  proj.mat[5], proj.mat[9],  proj.mat[13],
	                                      proj.mat[2],  proj.mat[6], proj.mat[10], proj.mat[14],
	                                      proj.mat[3],  proj.mat[7], proj.mat[11], proj.mat[15] ));
		// */

		/**
		proj = scene.viewWindow().getProjectionMatrix(true);
		pg2d().flush();
	  pg2d().projection.set( proj.mat[0], proj.mat[4],                                  proj.mat[8],  proj.mat[12],
		                       proj.mat[1], isLeftHanded() ? proj.mat[5] : -proj.mat[5], proj.mat[9],  proj.mat[13],
		                       proj.mat[2], proj.mat[6],                                  proj.mat[10], proj.mat[14],
		                       proj.mat[3], proj.mat[7],                                  proj.mat[11], proj.mat[15] );
		pg2d().updateProjmodelview();
		// */
	}

	/**
	 * Sets the processing camera matrix from {@link #camera()}. Simply calls
	 * {@code PApplet.camera()}.
	 */
	@Override
	protected void setModelViewMatrix() {
	  // The two options work seamlessly
		/**		
		// Option 1
		Matrix3D mat = new Matrix3D();		
		scene.viewWindow().getViewMatrix(mat, true);
		mat.transpose();// experimental
		float[] target = new float[16];
		pg2d().modelview.set(mat.get(target));
		// */

		// /**		
		// Option 2
		pg2d().modelview.set(scene.viewWindow().getViewMatrix(true).getTransposed(new float[16]));						
		// Finally, caches projmodelview
		//pg2d().projmodelview.set(scene.viewWindow().getProjectionViewMatrix(true).getTransposed(new float[16]));		
		Matrix3D.mult(proj, scene.viewWindow().view(), scene.viewWindow().projectionView());
		pg2d().projmodelview.set(scene.viewWindow().getProjectionViewMatrix(false).getTransposed(new float[16]));
	  // */
	}
}
