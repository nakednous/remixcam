package remixlab.proscene.renderers;

//import java.util.List;

import processing.core.*;
import processing.opengl.*;
/**
import remixlab.remixcam.core.*;
import remixlab.remixcam.geom.*;
// */

import remixlab.proscene.Scene;
// /**
//import remixlab.remixcam.core.Camera;
//import remixlab.remixcam.core.ViewWindow;
//import remixlab.remixcam.geom.Vector3D;
import remixlab.remixcam.geom.Matrix3D;
//import remixlab.remixcam.geom.Quaternion;
//import remixlab.remixcam.geom.GeomFrame;
//import remixlab.remixcam.geom.Quaternion;
// */
import remixlab.remixcam.geom.Vector3D;

public class P5Renderer3D extends P5Renderer {
	Vector3D at;	
	
	public P5Renderer3D(Scene scn, PGraphicsOpenGL renderer) {
		super(scn, renderer, new P5Drawing3D(scn));		
		at = new Vector3D();		
	}
	
	public PGraphics3D pg3d() {
	  return (PGraphics3D) pg();	
	}
	
	@Override
	public boolean is3D() {
		return true;
	}
	
	/**
	@Override
	public void bindMatrices() {		
		scene.camera().computeProjectionMatrix();		
		scene.camera().computeViewMatrix();
		scene.camera().computeProjectionViewMatrix();
		
		Vector3D pos = scene.camera().position();
		Quaternion quat = (Quaternion) scene.camera().frame().orientation();
		Vector3D axis = quat.axis();
		
	  switch (scene.camera().type()) {
	  case PERSPECTIVE:
	  	pg3d().perspective(scene.camera().fieldOfView(), scene.camera().aspectRatio(), scene.camera().zNear(), scene.camera().zFar());
	  	
	  	break;
	  case ORTHOGRAPHIC:
	  	//TODO broken
			float cameraZ = (pg3d().height/2.0f) / PApplet.tan( scene().camera().fieldOfView() /2.0f);
		  float cameraNear = cameraZ / 2.0f;
		  float cameraFar = cameraZ * 2.0f;
		    
		  //pg3d().ortho(0, pg3d().width, 0, pg3d().height, cameraNear, cameraFar);	
		    
			float[] wh = scene.camera().getOrthoWidthHeight();					
			//pg3d().ortho(0, 2*wh[0], 0, 2*wh[1], scene.camera().zNear(), scene.camera().zFar());
			//pg3d().ortho(0, pg3d().width, 0, pg3d().height, cameraNear, cameraFar);	
			pg3d().ortho(0, pg3d().width, 0, pg3d().height, scene.camera().zNear(), scene.camera().zFar());
			
			break;
		}		
		
		translate(scene.width() / 2, scene.height() / 2,  (scene.height() / 2) / (float) Math.tan(PI / 6));
		
		if(scene.isRightHanded()) scale(1,-1,1);
		
		rotate(-quat.angle(), axis.x(), axis.y(), axis.z());		
		translate(-pos.x(), -pos.y(), -pos.z());		
	}	
	// */

	@Override
	public void setProjection(Matrix3D source) {
		PMatrix3D pM = new PMatrix3D();
    pM.set(source.getTransposed(new float[16]));
    pg3d().setProjection(pM);
	}
	
	@Override
	public void setMatrix(Matrix3D source) {
		PMatrix3D pM = new PMatrix3D();
		pM.set(source.getTransposed(new float[16]));
		pg3d().setMatrix(pM);//needs testing in screen drawing
	}	
	
	//---

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
	  scene.camera().getProjectionMatrix(mat, true);
	  mat.transpose();
	  float[] target = new float[16];
	  pg3d().projection.set(mat.get(target));
	  // */	

	  /**
	  // Option 2
	  pg3d().projection.set(scene.camera().getProjectionMatrix(true).getTransposed(new float[16]));
	  // */

	  // /**
	  // option 3 (new, Andres suggestion)
	  //TODO: optimize me set per value basis
	  //proj.set((scene.camera().getProjectionMatrix(true).getTransposed(new float[16])));
	  proj = scene.camera().getProjectionMatrix(true);
	  pg3d().setProjection(new PMatrix3D( proj.mat[0], proj.mat[4], proj.mat[8], proj.mat[12],
	  																		proj.mat[1], proj.mat[5], proj.mat[9], proj.mat[13],
	  																		proj.mat[2], proj.mat[6], proj.mat[10], proj.mat[14],
	  																		proj.mat[3], proj.mat[7], proj.mat[11], proj.mat[15] ));
	  // */

	  /**
	  proj = scene.camera().getProjectionMatrix(true);
	  pg3d().flush();
	  pg3d().projection.set( proj.mat[0], proj.mat[4], proj.mat[8], proj.mat[12],
	  proj.mat[1], isLeftHanded() ? proj.mat[5] : -proj.mat[5], proj.mat[9], proj.mat[13],
	  proj.mat[2], proj.mat[6], proj.mat[10], proj.mat[14],
	  proj.mat[3], proj.mat[7], proj.mat[11], proj.mat[15] );
	  pg3d().updateProjmodelview();//only in P5-head
	  // */	

	  /**
	  // Option 4
	  // compute the processing camera projection matrix from our camera() parameters
	  switch (scene.camera().type()) {
	  case PERSPECTIVE:
	  pg3d().perspective(scene.camera().fieldOfView(), scene.camera().aspectRatio(), scene.camera().zNear(), scene.camera().zFar());
	  break;
	  case ORTHOGRAPHIC:
	  float[] wh = scene.camera().getOrthoWidthHeight();
	  pg3d().ortho(-wh[0], wh[0], -wh[1], wh[1], scene.camera().zNear(), scene.camera().zFar());
	  break;
	  }
	  // hack:
	  //if(this.isRightHanded())
	  //pg3d().projection.m11 = -pg3d().projection.m11;
	  // We cache the processing camera projection matrix into our camera()
	  scene.camera().setProjectionMatrix( pg3d().projection.get(new float[16]), true ); // set it transposed
	  // */
	}

	/**
	* Sets the processing camera matrix from {@link #camera()}. Simply calls
	* {@code PApplet.camera()}.
	*/	
	@Override
	protected void setModelViewMatrix() {
	  // All three options work seamlessly
	  /**
	  // Option 1
	  Matrix3D mat = new Matrix3D();
	  scene.camera().getViewMatrix(mat, true);
	  mat.transpose();// experimental
	  float[] target = new float[16];
	  pg3d().modelview.set(mat.get(target));
	  //caches projmodelview
	  pg3d().projmodelview.set(scene.camera().getProjectionViewMatrix(true).getTransposed(new float[16]));
	  // */

	  /**
	  // Option 2
	  pg3d().modelview.set(scene.camera().getViewMatrix(true).getTransposed(new float[16]));
	  // Finally, caches projmodelview
	  //pg3d().projmodelview.set(scene.camera().getProjectionViewMatrix(true).getTransposed(new float[16]));
	  Matrix3D.mult(proj, scene.camera().view(), scene.camera().projectionView());
	  pg3d().projmodelview.set(scene.camera().getProjectionViewMatrix(false).getTransposed(new float[16]));
	  // */	

	  // /**
	  // Option 3
	  // compute the processing camera modelview matrix from our camera() parameters
	  at = scene.camera().at();
	  pg3d().camera(scene.camera().position().x(), scene.camera().position().y(), scene.camera().position().z(),
	  //scene.camera().at().x(), scene.camera().at().y(), scene.camera().at().z(),
	  at.x(), at.y(), at.z(),
	  scene.camera().upVector().x(), scene.camera().upVector().y(), scene.camera().upVector().z());
	  // We cache the processing camera modelview matrix into our camera()
	  scene.camera().setViewMatrix( pg3d().modelview.get(new float[16]), true );// set it transposed
	  // We cache the processing camera projmodelview matrix into our camera()
	  scene.camera().setProjectionViewMatrix( pg3d().projmodelview.get(new float[16]), true );// set it transposed
	  // */
	}	
}
