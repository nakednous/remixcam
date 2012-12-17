package remixlab.proscene;

import java.util.List;

import processing.core.*;
import processing.opengl.*;
/**
import remixlab.remixcam.core.*;
import remixlab.remixcam.geom.*;
// */

// /**
import remixlab.remixcam.core.AbstractScene;
import remixlab.remixcam.core.Camera;
//import remixlab.remixcam.geom.Vector3D;
import remixlab.remixcam.geom.Matrix3D;
import remixlab.remixcam.geom.Quaternion;
import remixlab.remixcam.geom.VFrame;
//import remixlab.remixcam.geom.Quaternion;
// */
import remixlab.remixcam.geom.Vector3D;

public class Renderer3D extends Renderer {
	Vector3D at;
	public Renderer3D(AbstractScene scn, PGraphics3D renderer) {
		super(scn, renderer);
		at = new Vector3D();
	}
	
	public PGraphics3D pg3d() {
	  return (PGraphics3D) pg();	
	}
	
	@Override
	public void bindMatrices() {
		setProjectionMatrix();
		setModelViewMatrix();
	}
	
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
		pg3d().setProjection(new PMatrix3D( proj.mat[0],  proj.mat[4], proj.mat[8],  proj.mat[12],
	                                      proj.mat[1],  isLeftHanded() ? proj.mat[5] : -proj.mat[5], proj.mat[9],  proj.mat[13],
	                                      proj.mat[2],  proj.mat[6], proj.mat[10], proj.mat[14],
	                                      proj.mat[3],  proj.mat[7], proj.mat[11], proj.mat[15] ));
		// */
		
	  /**
		proj = scene.camera().getProjectionMatrix(true);
		pg3d().flush();
		pg3d().projection.set( proj.mat[0], proj.mat[4],                                  proj.mat[8],  proj.mat[12],
	                         proj.mat[1], isLeftHanded() ? proj.mat[5] : -proj.mat[5], proj.mat[9],  proj.mat[13],
	                         proj.mat[2], proj.mat[6],                                  proj.mat[10], proj.mat[14],
	                         proj.mat[3], proj.mat[7],                                  proj.mat[11], proj.mat[15] );
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
	
	@Override
	public void beginScreenDrawing() {
		pg3d().hint(DISABLE_DEPTH_TEST);
		pg3d().pushProjection();
		float cameraZ = (pg3d().height/2.0f) / PApplet.tan( scene().camera().fieldOfView() /2.0f);
    float cameraNear = cameraZ / 2.0f;
    float cameraFar = cameraZ * 2.0f;
    pg3d().ortho(0, pg3d().width, 0, pg3d().height, cameraNear, cameraFar);		
    pg3d().pushMatrix();
	  // Camera needs to be reset!
    pg3d().camera();   
	}
	
	@Override
	public void endScreenDrawing() {
		pg3d().popProjection();  
		pg3d().popMatrix();		  
		pg3d().hint(ENABLE_DEPTH_TEST);
	}
	
	@Override
	public void pushProjection() {
		pg3d().pushProjection();		
	}

	@Override
	public void popProjection() {
		pg3d().popProjection();
	}

	@Override
	public void resetProjection() {
		pg3d().projection.reset();		
	}

	@Override
	public void loadProjection(Matrix3D source) {
		PMatrix3D pM = new PMatrix3D();
    pM.set(source.getTransposed(new float[16]));
    pg3d().setProjection(pM);
	}

	@Override
	public void applyProjection(Matrix3D source) {
		 PMatrix3D pM = new PMatrix3D();
     pM.set(source.getTransposed(new float[16]));
     pg3d().applyProjection(pM);
	}

	@Override
	public void applyProjectionRowMajorOrder(float n00, float n01, float n02,
			float n03, float n10, float n11, float n12, float n13, float n20,
			float n21, float n22, float n23, float n30, float n31, float n32,
			float n33) {
		pg3d().applyProjection(new PMatrix3D(n00, n01, n02, n03, n10, n11, n12, n13, n20, n21, n22, n23, n30, n31, n32, n33));		
	}

	@Override
	public Matrix3D getProjection() {
		PMatrix3D pM = pg3d().projection.get();
    return new Matrix3D(pM.get(new float[16]), true);// set it transposed
	}

	@Override
	public Matrix3D getProjection(Matrix3D target) {
		PMatrix3D pM = pg3d().projection.get();
    target.setTransposed(pM.get(new float[16]));
    return target;
	}
	
	@Override
	public void frustum(float left, float right, float bottom, float top,	float znear, float zfar) {
		pg3d().frustum(left, right, bottom, top, znear, zfar);
	}

	/**
	 * Overriding of {@link remixlab.remixcam.core.Rendarable#cylinder(float, float)}.
	 * <p>
	 * Code adapted from http://www.processingblogs.org/category/processing-java/ 
	 */
	@Override
	public void cylinder(float w, float h) {
		float px, py;
		
		pg3d().beginShape(QUAD_STRIP);
		for (float i = 0; i < 13; i++) {
			px = (float) Math.cos(PApplet.radians(i * 30)) * w;
			py = (float) Math.sin(PApplet.radians(i * 30)) * w;
			pg3d().vertex(px, py, 0);
			pg3d().vertex(px, py, h);
		}
		pg3d().endShape();
		
		pg3d().beginShape(TRIANGLE_FAN);
		pg3d().vertex(0, 0, 0);
		for (float i = 12; i > -1; i--) {
			px = (float) Math.cos(PApplet.radians(i * 30)) * w;
			py = (float) Math.sin(PApplet.radians(i * 30)) * w;
			pg3d().vertex(px, py, 0);
		}
		pg3d().endShape();
		
		pg3d().beginShape(TRIANGLE_FAN);
		pg3d().vertex(0, 0, h);
		for (float i = 0; i < 13; i++) {
			px = (float) Math.cos(PApplet.radians(i * 30)) * w;
			py = (float) Math.sin(PApplet.radians(i * 30)) * w;
			pg3d().vertex(px, py, h);
		}
		pg3d().endShape();
	}
	
	/**
	 * Convenience function that simply calls
	 * {@code hollowCylinder(20, w, h, new Vector3D(0,0,-1), new Vector3D(0,0,1))}.
	 * 
	 * @see #hollowCylinder(int, float, float, Vector3D, Vector3D)
	 * @see #cylinder(float, float)
	 */
	public void hollowCylinder(float w, float h) {
		this.hollowCylinder(20, w, h, new Vector3D(0,0,-1), new Vector3D(0,0,1));
	}
	
	/**
	 * Convenience function that simply calls
	 * {@code hollowCylinder(detail, w, h, new Vector3D(0,0,-1), new Vector3D(0,0,1))}.
	 * 
	 * @see #hollowCylinder(int, float, float, Vector3D, Vector3D)
	 * @see #cylinder(float, float)
	 */
	public void hollowCylinder(int detail, float w, float h) {
		this.hollowCylinder(detail, w, h, new Vector3D(0,0,-1), new Vector3D(0,0,1));
	}
 
	/**
	 * Draws a cylinder whose bases are formed by two cutting planes ({@code m}
	 * and {@code n}), along the {@link #renderer()} positive {@code z} axis.
	 * 
	 * @param detail
	 * @param w radius of the cylinder and h is its height
	 * @param h height of the cylinder
	 * @param m normal of the plane that intersects the cylinder at z=0
	 * @param n normal of the plane that intersects the cylinder at z=h
	 * 
	 * @see #cylinder(float, float)
	 */
	@Override
	public void hollowCylinder(int detail, float w, float h, Vector3D m, Vector3D n) {
		//eqs taken from: http://en.wikipedia.org/wiki/Line-plane_intersection
		Vector3D pm0 = new Vector3D(0,0,0);
		Vector3D pn0 = new Vector3D(0,0,h);
		Vector3D l0 = new Vector3D();		
		Vector3D l = new Vector3D(0,0,1);
		Vector3D p = new Vector3D();
		float x,y,d;		
		
		pg3d().noStroke();
		pg3d().beginShape(QUAD_STRIP);
		
		for (float t = 0; t <= detail; t++) {
			x = w * PApplet.cos(t * TWO_PI/detail);
			y = w * PApplet.sin(t * TWO_PI/detail);
			l0.set(x,y,0);
			
			d = ( m.dot(Vector3D.sub(pm0, l0)) )/( l.dot(m) );
			p =  Vector3D.add( Vector3D.mult(l, d), l0 );
			pg3d().vertex(p.x(), p.y(), p.z());
			
			l0.z(h);
			d = ( n.dot(Vector3D.sub(pn0, l0)) )/( l.dot(n) );
			p =  Vector3D.add( Vector3D.mult(l, d), l0 );
			pg3d().vertex(p.x(), p.y(), p.z());
		}
		pg3d().endShape();
	}

	/**
	 * Overriding of {@link remixlab.remixcam.core.Renderable#cone(int, float, float, float, float)}.
	 * <p>
	 * The code of this function was adapted from
	 * http://processinghacks.com/hacks:cone Thanks to Tom Carden.
	 * 
	 * @see #cone(int, float, float, float, float, float)
	 */
	@Override
	public void cone(int detail, float x, float y, float r, float h) {
		float unitConeX[] = new float[detail + 1];
		float unitConeY[] = new float[detail + 1];

		for (int i = 0; i <= detail; i++) {
			float a1 = PApplet.TWO_PI * i / detail;
			unitConeX[i] = r * (float) Math.cos(a1);
			unitConeY[i] = r * (float) Math.sin(a1);
		}

		pg3d().pushMatrix();
		pg3d().translate(x, y);
		pg3d().beginShape(TRIANGLE_FAN);
		pg3d().vertex(0, 0, h);
		for (int i = 0; i <= detail; i++) {
			pg3d().vertex(unitConeX[i], unitConeY[i], 0.0f);
		}
		pg3d().endShape();
		pg3d().popMatrix();
	}

	/**
	 * Overriding of {@link remixlab.remixcam.core.Renderable#cone(int, float, float, float, float, float)}.
	 */
	@Override
	public void cone(int detail, float x, float y, float r1, float r2, float h) {
		float firstCircleX[] = new float[detail + 1];
		float firstCircleY[] = new float[detail + 1];
		float secondCircleX[] = new float[detail + 1];
		float secondCircleY[] = new float[detail + 1];

		for (int i = 0; i <= detail; i++) {
			float a1 = TWO_PI * i / detail;
			firstCircleX[i] = r1 * (float) Math.cos(a1);
			firstCircleY[i] = r1 * (float) Math.sin(a1);
			secondCircleX[i] = r2 * (float) Math.cos(a1);
			secondCircleY[i] = r2 * (float) Math.sin(a1);
		}

		pg3d().pushMatrix();
		pg3d().translate(x, y);
		pg3d().beginShape(QUAD_STRIP);
		for (int i = 0; i <= detail; i++) {
			pg3d().vertex(firstCircleX[i], firstCircleY[i], 0);
			pg3d().vertex(secondCircleX[i], secondCircleY[i], h);
		}
		pg3d().endShape();
		pg3d().popMatrix();		
	}

	@Override
	public void drawAxis(float length) {
		final float charWidth = length / 40.0f;
		final float charHeight = length / 30.0f;
		final float charShift = 1.04f * length;

		// pg3d().noLights();

		pg3d().pushStyle();
		
		pg3d().beginShape(LINES);		
		pg3d().strokeWeight(2);
		// The X
		pg3d().stroke(200, 0, 0);
		pg3d().vertex(charShift, charWidth, -charHeight);
		pg3d().vertex(charShift, -charWidth, charHeight);
		pg3d().vertex(charShift, -charWidth, -charHeight);
		pg3d().vertex(charShift, charWidth, charHeight);
		// The Y
		pg3d().stroke(0, 200, 0);
		pg3d().vertex(charWidth, charShift, charHeight);
		pg3d().vertex(0.0f, charShift, 0.0f);
		pg3d().vertex(-charWidth, charShift, charHeight);
		pg3d().vertex(0.0f, charShift, 0.0f);
		pg3d().vertex(0.0f, charShift, 0.0f);
		pg3d().vertex(0.0f, charShift, -charHeight);
		// The Z
		pg3d().stroke(0, 100, 200);
		
		//left_handed
		if( isLeftHanded() ) {
			pg3d().vertex(-charWidth, -charHeight, charShift);
			pg3d().vertex(charWidth, -charHeight, charShift);
			pg3d().vertex(charWidth, -charHeight, charShift);
			pg3d().vertex(-charWidth, charHeight, charShift);
			pg3d().vertex(-charWidth, charHeight, charShift);
			pg3d().vertex(charWidth, charHeight, charShift);
		}
		else {
			pg3d().vertex(-charWidth, charHeight, charShift);
			pg3d().vertex(charWidth, charHeight, charShift);
			pg3d().vertex(charWidth, charHeight, charShift);
			pg3d().vertex(-charWidth, -charHeight, charShift);
			pg3d().vertex(-charWidth, -charHeight, charShift);
			pg3d().vertex(charWidth, -charHeight, charShift);
		}
		
		pg3d().endShape();
		
	  /**
		// Z axis
		pg3d().noStroke();
		pg3d().fill(0, 100, 200);
		drawArrow(length, 0.01f * length);

		// X Axis
		pg3d().fill(200, 0, 0);
		pg3d().pushMatrix();
		pg3d().rotateY(HALF_PI);
		drawArrow(length, 0.01f * length);
		pg3d().popMatrix();

		// Y Axis
		pg3d().fill(0, 200, 0);
		pg3d().pushMatrix();
		pg3d().rotateX(-HALF_PI);
		drawArrow(length, 0.01f * length);
		pg3d().popMatrix();
		// */
		
	  // X Axis
		pg3d().stroke(200, 0, 0);
		pg3d().line(0, 0, 0, length, 0, 0);
	  // Y Axis
		pg3d().stroke(0, 200, 0);		
		pg3d().line(0, 0, 0, 0, length, 0);
		// Z Axis
		pg3d().stroke(0, 100, 200);
		pg3d().line(0, 0, 0, 0, 0, length);		

		pg3d().popStyle();
	}

	/**
	@Override
	public void drawGrid(float size, int nbSubdivisions) {
		// TODO Auto-generated method stub
		
	}
	*/

	@Override
	public void drawCamera(Camera camera, boolean drawFarPlane, float scale) {
		pg3d().pushMatrix();
		
		//applyMatrix(camera.frame().worldMatrix());
		// same as the previous line, but maybe more efficient
		/**
		VFrame tmpFrame = new VFrame(scene.is3D());
		tmpFrame.fromMatrix(camera.frame().worldMatrix());
		scene().applyTransformation(tmpFrame);
		// */
		//same as above but easier
		
		//fails due to scaling!
		//scene().applyTransformation(camera.frame());
		
		translate( camera.frame().translation().vec[0], camera.frame().translation().vec[1], camera.frame().translation().vec[2] );
		rotate( camera.frame().rotation().angle(), ((Quaternion)camera.frame().rotation()).axis().vec[0], ((Quaternion)camera.frame().rotation()).axis().vec[1], ((Quaternion)camera.frame().rotation()).axis().vec[2]);

		// 0 is the upper left coordinates of the near corner, 1 for the far one
		PVector[] points = new PVector[2];
		points[0] = new PVector();
		points[1] = new PVector();

		points[0].z = scale * camera.zNear();
		points[1].z = scale * camera.zFar();

		switch (camera.type()) {
		case PERSPECTIVE: {
			points[0].y = points[0].z * PApplet.tan(camera.fieldOfView() / 2.0f);
			points[0].x = points[0].y * camera.aspectRatio();
			float ratio = points[1].z / points[0].z;
			points[1].y = ratio * points[0].y;
			points[1].x = ratio * points[0].x;
			break;
		}
		case ORTHOGRAPHIC: {
			float[] wh = camera.getOrthoWidthHeight();
			points[0].x = points[1].x = scale * wh[0];
			points[0].y = points[1].y = scale * wh[1];
			break;
		}
		}

		int farIndex = drawFarPlane ? 1 : 0;
		
	  // Frustum lines
		pg3d().pushStyle();		
		pg3d().strokeWeight(2);
		//pg3d().stroke(255,255,0);
		switch (camera.type()) {
			case PERSPECTIVE:
				pg3d().beginShape(PApplet.LINES);
				pg3d().vertex(0.0f, 0.0f, 0.0f);
				pg3d().vertex(points[farIndex].x, points[farIndex].y, -points[farIndex].z);
				pg3d().vertex(0.0f, 0.0f, 0.0f);
				pg3d().vertex(-points[farIndex].x, points[farIndex].y, -points[farIndex].z);
				pg3d().vertex(0.0f, 0.0f, 0.0f);
				pg3d().vertex(-points[farIndex].x, -points[farIndex].y,	-points[farIndex].z);
				pg3d().vertex(0.0f, 0.0f, 0.0f);
				pg3d().vertex(points[farIndex].x, -points[farIndex].y, -points[farIndex].z);
				pg3d().endShape();
				break;
			case ORTHOGRAPHIC:
				if (drawFarPlane) {
					pg3d().beginShape(PApplet.LINES);
					pg3d().vertex(points[0].x, points[0].y, -points[0].z);
					pg3d().vertex(points[1].x, points[1].y, -points[1].z);
					pg3d().vertex(-points[0].x, points[0].y, -points[0].z);
					pg3d().vertex(-points[1].x, points[1].y, -points[1].z);
					pg3d().vertex(-points[0].x, -points[0].y, -points[0].z);
					pg3d().vertex(-points[1].x, -points[1].y, -points[1].z);
					pg3d().vertex(points[0].x, -points[0].y, -points[0].z);
					pg3d().vertex(points[1].x, -points[1].y, -points[1].z);
					pg3d().endShape();
				}
		}
		
		// Near and (optionally) far plane(s)		
		pg3d().noStroke();
		//pg3d().fill(255,255,0,160);
		pg3d().beginShape(PApplet.QUADS);
		for (int i = farIndex; i >= 0; --i) {
			pg3d().normal(0.0f, 0.0f, (i == 0) ? 1.0f : -1.0f);			
			pg3d().vertex(points[i].x, points[i].y, -points[i].z);
			pg3d().vertex(-points[i].x, points[i].y, -points[i].z);
			pg3d().vertex(-points[i].x, -points[i].y, -points[i].z);
			pg3d().vertex(points[i].x, -points[i].y, -points[i].z);
		}
		pg3d().endShape();

		// Up arrow
		float arrowHeight = 1.5f * points[0].y;
		float baseHeight = 1.2f * points[0].y;
		float arrowHalfWidth = 0.5f * points[0].x;
		float baseHalfWidth = 0.3f * points[0].x;

		// pg3d().noStroke();
		// Base
		pg3d().beginShape(PApplet.QUADS);
		
		if( isLeftHanded() ) {
			pg3d().vertex(-baseHalfWidth, -points[0].y, -points[0].z);
			pg3d().vertex(baseHalfWidth, -points[0].y, -points[0].z);
			pg3d().vertex(baseHalfWidth, -baseHeight, -points[0].z);
			pg3d().vertex(-baseHalfWidth, -baseHeight, -points[0].z);
		}
		else {
			pg3d().vertex(-baseHalfWidth, points[0].y, -points[0].z);
			pg3d().vertex(baseHalfWidth, points[0].y, -points[0].z);
			pg3d().vertex(baseHalfWidth, baseHeight, -points[0].z);
			pg3d().vertex(-baseHalfWidth, baseHeight, -points[0].z);
		}
		pg3d().endShape();

		// Arrow
		pg3d().beginShape(PApplet.TRIANGLES);
		
		if( isLeftHanded() ) {
			pg3d().vertex(0.0f, -arrowHeight, -points[0].z);
			pg3d().vertex(-arrowHalfWidth, -baseHeight, -points[0].z);
			pg3d().vertex(arrowHalfWidth, -baseHeight, -points[0].z);
		}
		else {
			pg3d().vertex(0.0f, arrowHeight, -points[0].z);
			pg3d().vertex(-arrowHalfWidth, baseHeight, -points[0].z);
			pg3d().vertex(arrowHalfWidth, baseHeight, -points[0].z);
		}
		pg3d().endShape();
		
		pg3d().popStyle();
		pg3d().popMatrix();
	}

	@Override
	public void drawKFIViewport(float scale) {
		float halfHeight = scale * 0.07f;
		float halfWidth = halfHeight * 1.3f;
		float dist = halfHeight / (float) Math.tan(PApplet.PI / 8.0f);

		float arrowHeight = 1.5f * halfHeight;
		float baseHeight = 1.2f * halfHeight;
		float arrowHalfWidth = 0.5f * halfWidth;
		float baseHalfWidth = 0.3f * halfWidth;

		// Frustum outline
		pg3d().pushStyle();

		pg3d().noFill();		
		pg3d().beginShape();
		pg3d().vertex(-halfWidth, halfHeight, -dist);
		pg3d().vertex(-halfWidth, -halfHeight, -dist);
		pg3d().vertex(0.0f, 0.0f, 0.0f);
		pg3d().vertex(halfWidth, -halfHeight, -dist);
		pg3d().vertex(-halfWidth, -halfHeight, -dist);
		pg3d().endShape();
		pg3d().noFill();
		pg3d().beginShape();
		pg3d().vertex(halfWidth, -halfHeight, -dist);
		pg3d().vertex(halfWidth, halfHeight, -dist);
		pg3d().vertex(0.0f, 0.0f, 0.0f);
		pg3d().vertex(-halfWidth, halfHeight, -dist);
		pg3d().vertex(halfWidth, halfHeight, -dist);
		pg3d().endShape();

		// Up arrow
		pg3d().noStroke();
		pg3d().fill(170);
		// Base
		pg3d().beginShape(PApplet.QUADS);
		
		if( isLeftHanded() ) {
			pg3d().vertex(baseHalfWidth, -halfHeight, -dist);
			pg3d().vertex(-baseHalfWidth, -halfHeight, -dist);
			pg3d().vertex(-baseHalfWidth, -baseHeight, -dist);
			pg3d().vertex(baseHalfWidth, -baseHeight, -dist);
		}
		else {
			pg3d().vertex(-baseHalfWidth, halfHeight, -dist);
			pg3d().vertex(baseHalfWidth, halfHeight, -dist);
			pg3d().vertex(baseHalfWidth, baseHeight, -dist);
			pg3d().vertex(-baseHalfWidth, baseHeight, -dist);
		}
		
		pg3d().endShape();
		// Arrow
		pg3d().beginShape(PApplet.TRIANGLES);
		
		if( isLeftHanded() ) {
			pg3d().vertex(0.0f, -arrowHeight, -dist);
			pg3d().vertex(arrowHalfWidth, -baseHeight, -dist);
			pg3d().vertex(-arrowHalfWidth, -baseHeight, -dist);
		}
		else {
		  pg3d().vertex(0.0f, arrowHeight, -dist);
		  pg3d().vertex(-arrowHalfWidth, baseHeight, -dist);
		  pg3d().vertex(arrowHalfWidth, baseHeight, -dist);
		}
		
		pg3d().endShape();

		pg3d().popStyle();
	}
	
	@Override
	public void drawPath(List<VFrame> path, int mask, int nbFrames, int nbSteps, float scale) {
		if (mask != 0) {
			pg3d().pushStyle();
			pg3d().strokeWeight(2);
			pg3d().noFill();
			pg3d().stroke(170);
			
			if (((mask & 1) != 0) && path.size() > 1 ) {				
				pg3d().beginShape();
				for (VFrame myFr : path)
					pg3d().vertex(myFr.position().x(), myFr.position().y(), myFr.position().z());
				pg3d().endShape();
			}
			if ((mask & 6) != 0) {
				int count = 0;
				if (nbFrames > nbSteps)
					nbFrames = nbSteps;
				float goal = 0.0f;

				for (VFrame myFr : path)
					if ((count++) >= goal) {
						goal += nbSteps / (float) nbFrames;
						pg3d().pushMatrix();
											  
						scene.applyTransformation(myFr);						

						if ((mask & 2) != 0)
							drawKFIViewport(scale);
						if ((mask & 4) != 0)
							drawAxis(scale / 10.0f);

						pg3d().popMatrix();
					}
			}
			pg3d().popStyle();
		}
	}	
}
