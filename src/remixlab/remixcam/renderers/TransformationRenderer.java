package remixlab.remixcam.renderers;

import remixlab.remixcam.core.*;
import remixlab.remixcam.geom.*;

public abstract class TransformationRenderer extends Renderer {
	public TransformationRenderer(AbstractScene scn) {
		super(scn);
	}
	
	public TransformationRenderer(AbstractScene scn, Drawerable dw) {
		super(scn, dw);
	}
	
	@Override	
	public void bindMatrices() {
		scene.pinhole().computeProjectionMatrix();
		scene.pinhole().computeViewMatrix();
		scene.pinhole().computeProjectionViewMatrix();

		Vector3D pos = scene.pinhole().position();
		Orientable quat = scene.pinhole().frame().orientation();

		if( scene.is2D() ) {
			translate(scene.width() / 2, scene.height() / 2);
			if(scene.isRightHanded()) scale(1,-1);		
			scale(scene.pinhole().frame().inverseMagnitude().x(), 
				    scene.pinhole().frame().inverseMagnitude().y());		
			rotate(-quat.angle());		
			translate(-pos.x(), -pos.y());
		}
		else {
			setProjection();
			Vector3D axis = ((Quaternion)quat).axis();
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
		Vector3D pos = scene.pinhole().position();
		Orientable quat = scene.pinhole().frame().orientation();		
		
		pushMatrix();
		
		if( scene.is2D() ) {
		  translate(pos.x(), pos.y());
		  rotate(quat.angle());		
		  scale(scene.viewWindow().frame().magnitude().x(),
		        scene.viewWindow().frame().magnitude().y());
	    if(scene.isRightHanded()) scale(1,-1);
		  translate(-scene.width()/2, -scene.height()/2);			
		}
		else {
			Vector3D axis = ((Quaternion)quat).axis();
			translate(pos.x(), pos.y(), pos.z());
			rotate(quat.angle(), axis.x(), axis.y(), axis.z());
			//projection
			unsetProjection();
		  if(scene.isRightHanded()) scale(1,-1,1);
		  translate(-scene.width() / 2, -scene.height() / 2,  -(scene.height() / 2) / (float) Math.tan(PI / 6));
		}
	}
	
	@Override
	public void endScreenDrawing() {
		popMatrix();
	}
	
  public abstract void setProjection();
	
	public abstract void unsetProjection();	

	@Override
	public void pushMatrix() {
		AbstractScene.showMethodWarning("pushMatrix");
	}

	@Override
	public void popMatrix() {
		AbstractScene.showMethodWarning("popMatrix");
	}

	@Override
	public void pushProjection() {
		AbstractScene.showMethodWarning("pushProjection");
	}

	@Override
	public void popProjection() {
		AbstractScene.showMethodWarning("popProjection");
	}

	@Override
	public void resetMatrix() {
		AbstractScene.showMethodWarning("resetMatrix");
	}

	@Override
	public void resetProjection() {
		AbstractScene.showMethodWarning("resetProjection");
	}

	@Override
	public void loadMatrix(Matrix3D source) {
		AbstractScene.showMethodWarning("loadMatrix");
	}

	@Override
	public void loadProjection(Matrix3D source) {
		AbstractScene.showMethodWarning("loadProjection");
	}

	@Override
	public void multiplyMatrix(Matrix3D source) {
		AbstractScene.showMethodWarning("multiplyMatrix");
	}

	@Override
	public void multiplyProjection(Matrix3D source) {
		AbstractScene.showMethodWarning("multiplyProjection");
	}

	@Override
	public void applyMatrix(Matrix3D source) {
		AbstractScene.showMethodWarning("applyMatrix");
	}

	@Override
	public void applyProjection(Matrix3D source) {
		AbstractScene.showMethodWarning("applyProjection");
	}

	@Override
	public void applyMatrixRowMajorOrder(float n00, float n01, float n02,
			float n03, float n10, float n11, float n12, float n13, float n20,
			float n21, float n22, float n23, float n30, float n31, float n32,
			float n33) {
		AbstractScene.showMethodWarning("applyMatrixRowMajorOrder");
	}

	@Override
	public void applyProjectionRowMajorOrder(float n00, float n01, float n02,
			float n03, float n10, float n11, float n12, float n13, float n20,
			float n21, float n22, float n23, float n30, float n31, float n32,
			float n33) {
		AbstractScene.showMethodWarning("applyProjectionRowMajorOrder");
	}

	@Override
	public Matrix3D getMatrix() {
		AbstractScene.showMethodWarning("getMatrix");
		return null;
	}

	@Override
	public Matrix3D getMatrix(Matrix3D target) {
		AbstractScene.showMethodWarning("getMatrix");
		return null;
	}

	@Override
	public Matrix3D getProjection() {
		AbstractScene.showMethodWarning("getProjection");
		return null;
	}

	@Override
	public Matrix3D getProjection(Matrix3D target) {
		AbstractScene.showMethodWarning("getProjection");
		return null;
	}

	@Override
	public void setMatrix(Matrix3D source) {
		AbstractScene.showMethodWarning("setMatrix");
	}

	@Override
	public void printMatrix() {
		AbstractScene.showMethodWarning("printMatrix");
	}

	@Override
	public void setProjection(Matrix3D source) {
		AbstractScene.showMethodWarning("setProjection");
	}

	@Override
	public void printProjection() {
		AbstractScene.showMethodWarning("printProjection");
	}
	
	/**
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
	 */
}
