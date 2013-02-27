package remixlab.proscene;

import java.util.List;

import processing.core.*;
import processing.opengl.*;

/**
import remixlab.remixcam.core.*;
import remixlab.remixcam.geom.*;
*/

// /*
import remixlab.remixcam.core.AbstractScene;
import remixlab.remixcam.geom.GeomFrame;
import remixlab.remixcam.geom.Matrix3D;
//import remixlab.remixcam.geom.Quaternion;
// */

public class Renderer2D extends Renderer {	
	public Renderer2D(AbstractScene scn, PGraphics2D renderer) {
		super(scn, renderer);
	}	
	
	public PGraphics2D pg2d() {
	  return (PGraphics2D) pg();	
	}
	
  //--	
	protected PMatrix3D get2DOrtho(float left, float right, float bottom, float top, float near, float far) {
		float x = +2.0f / (right - left);
		float y = +2.0f / (top - bottom);
		float z = -2.0f / (far - near);
		
		float tx = -(right + left) / (right - left);
		float ty = -(top + bottom) / (top - bottom);
		float tz = -(far + near)   / (far - near);
		
		// The minus sign is needed to invert the Y axis.
		return new PMatrix3D(x,  0, 0, tx,
                         0, -y, 0, ty,
                         0,  0, z, tz,
                         0,  0, 0,  1);
	}
	
	protected PMatrix3D get2DModelView() {
		 return get2DModelView(scene().width()/2f, scene.height()/2f, (scene().height()/2f) / (float)Math.tan(PI*60 / 360), scene().width()/2f, scene.height()/2f, 0, 0, 1, 0);
	}
	
	protected PMatrix3D get2DModelView(float eyeX, float eyeY, float eyeZ,
     float centerX, float centerY, float centerZ,
     float upX, float upY, float upZ) {
				
		// Calculating Z vector
		float z0 = eyeX - centerX;
		float z1 = eyeY - centerY;
		float z2 = eyeZ - centerZ;
		float mag = PApplet.sqrt(z0 * z0 + z1 * z1 + z2 * z2);
		if (nonZero(mag)) {
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
		mag = PApplet.sqrt(x0 * x0 + x1 * x1 + x2 * x2);
		if (nonZero(mag)) {
			x0 /= mag;
			x1 /= mag;
			x2 /= mag;
		}
		
		mag = PApplet.sqrt(y0 * y0 + y1 * y1 + y2 * y2);
		if (nonZero(mag)) {
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
		
		PMatrix3D mv = new PMatrix3D(x0, x1, x2, 0,
                                y0, y1, y2, 0,
                                z0, z1, z2, 0,
                                 0,  0,  0, 1);
		
		float tx = -eyeX;
		float ty = -eyeY;
		float tz = -eyeZ;
		
		mv.translate(tx, ty, tz);
		
		return mv;
	}
		
	protected boolean nonZero(float a) {
		return 0x0.000002P-126f <= Math.abs(a);
  }
	// --
	
	// /**
	@Override
	public void bindMatrices() {
		setProjectionMatrix();
		setModelViewMatrix();
	}
	
	@Override
	public void beginScreenDrawing() {
    pg2d().hint(DISABLE_DEPTH_TEST);
    pg2d().pushProjection();
 		//float cameraZ = (pg2d().height/2.0f) / PApplet.tan(scene.viewWindow().fieldOfView() /2.0f);
    //TODO check this dummy value
    float cameraZ = (pg2d().height/2.0f) / PApplet.tan(PApplet.QUARTER_PI/2.0f);
    float cameraNear = cameraZ / 2.0f;
    float cameraFar = cameraZ * 2.0f;
    //renderer().ortho(-width/2, width/2, -height/2, height/2, cameraNear, cameraFar);
    //TODO check this line:  
    //pg2d().projection.set((scene.viewWindow()).getOrtho(-pg2d().width/2, pg2d().width/2, -pg2d().height/2, pg2d().height/2, cameraNear, cameraFar));
    //pg2d().projection.set((scene.viewWindow()).getOrtho(-pg2d().width/2, pg2d().width/2, -pg2d().height/2, pg2d().height/2, cameraNear, cameraFar).getTransposed(new float[16]));
    pg2d().projection.set(get2DOrtho(-pg2d().width/2, pg2d().width/2, -pg2d().height/2, pg2d().height/2, cameraNear, cameraFar));
    //pg2d().projection.set(get2DOrtho(0, pg2d().width, 0, pg2d().height, cameraNear, cameraFar));
    pg2d().pushMatrix();
 	  // Camera needs to be reset!
    //hack: it's trickier, but works ;)
    //renderer().camera();
    //TODO check this line:
    //pg2d().modelview.set(((P5Camera)scene.viewWindow()).getCamera());
    pg2d().modelview.set(get2DModelView());    
	}
	
	@Override
	public void endScreenDrawing() {
		pg2d().popProjection();  
		pg2d().popMatrix();		  
		pg2d().hint(ENABLE_DEPTH_TEST);
	}
	// */
	
	/**
	 * Sets the processing camera projection matrix from {@link #camera()}. Calls
	 * {@code PApplet.perspective()} or {@code PApplet.orhto()} depending on the
	 * {@link remixlab.remixcam.core.Camera#type()}.
	 */
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

	@Override
	public void pushProjection() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void popProjection() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resetProjection() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadProjection(Matrix3D source) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void applyProjection(Matrix3D source) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void applyProjectionRowMajorOrder(float n00, float n01, float n02,
			float n03, float n10, float n11, float n12, float n13, float n20,
			float n21, float n22, float n23, float n30, float n31, float n32,
			float n33) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Matrix3D getProjection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Matrix3D getProjection(Matrix3D target) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cylinder(float w, float h) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cone(int detail, float x, float y, float r, float h) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cone(int detail, float x, float y, float r1, float r2, float h) {
		// TODO Auto-generated method stub
		
	}	

	@Override
	public void drawKFIViewport(float scale) {
		// TODO Auto-generated method stub
		
	}	

	@Override
	public void drawPath(List<GeomFrame> path, int mask, int nbFrames,
			int nbSteps, float scale) {
		// TODO Auto-generated method stub
		
	}	
}
