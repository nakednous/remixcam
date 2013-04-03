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
import remixlab.remixcam.renderers.BProjectionRenderer;
//import remixlab.remixcam.geom.Quaternion;
// */

public class P5Renderer2D extends BProjectionRenderer implements PConstants {
	PGraphics pg;
	Matrix3D proj;
	
	public P5Renderer2D(Scene scn, PGraphics renderer) {
		super(scn, new P5Drawing2D(scn));
		pg = renderer;
		proj = new Matrix3D();
	}
	
	@Override
	public boolean is3D() {
		return false;
	}
	
	public PGraphics pg() {
		return pg;
	}
	
	public PGraphics2D pg2d() {
	  return (PGraphics2D) pg();	
	}	

	@Override
	public void pushProjection() {
		pg2d().pushProjection();		
	}

	@Override
	public void popProjection() {
		pg2d().popProjection();
	}

	@Override
	public void resetProjection() {
		pg2d().resetProjection();
	}
	
	@Override
	public Matrix3D getProjection() {
		PMatrix3D pM = pg2d().projection.get();
    return new Matrix3D(pM.get(new float[16]), true);// set it transposed
	}

	@Override
	public Matrix3D getProjection(Matrix3D target) {
		PMatrix3D pM = pg2d().projection.get();
    target.setTransposed(pM.get(new float[16]));
    return target;
	}

	@Override
	public void setProjection(Matrix3D source) {
		PMatrix3D pM = new PMatrix3D();
		pM.set(source.getTransposed(new float[16]));		
		pg2d().projection.set(pM);		
	}

	@Override
	public void applyProjection(Matrix3D source) {
		PMatrix3D pM = new PMatrix3D();
    pM.set(source.getTransposed(new float[16]));
    pg2d().applyProjection(pM);		
	}

	@Override
	public void applyProjectionRowMajorOrder(float n00, float n01, float n02,
			float n03, float n10, float n11, float n12, float n13, float n20,
			float n21, float n22, float n23, float n30, float n31, float n32,
			float n33) {
		pg2d().applyProjection(new PMatrix3D(n00, n01, n02, n03, n10, n11, n12, n13, n20, n21, n22, n23, n30, n31, n32, n33));
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
