package remixlab.remixcam.core;

import remixlab.remixcam.geom.*;

public interface Renderable {
	// matrix
	public void pushMatrix();
	public void popMatrix();
  public void translate(float tx, float ty);
  public void translate(float tx, float ty, float tz);
  public void rotate(float angle);
  public void rotateX(float angle);
  public void rotateY(float angle);
  public void rotateZ(float angle);
  public void rotate(float angle, float vx, float vy, float vz);
  public void scale(float s);
  public void scale(float sx, float sy);
  public void scale(float x, float y, float z);
  public void shearX(float angle);
  public void shearY(float angle);
  public void loadIdentity();
  public void resetMatrix();
  public void loadMatrix(Matrix3D source);
  public void multiplyMatrix(Matrix3D source);
  public void applyMatrix(Matrix3D source);
  public void applyMatrix(float n00, float n01, float n02, float n03,
                          float n10, float n11, float n12, float n13,
                          float n20, float n21, float n22, float n23,
                          float n30, float n31, float n32, float n33);
  public void frustum(float left, float right, float bottom, float top, float znear, float zfar);
  public Matrix3D getMatrix();
  public Matrix3D getMatrix(Matrix3D target);
  public void setMatrix(Matrix3D source);
  public void printMatrix();
  public void matrixMode(int mode);
  // dimensions
  int getWidth();
  int getHeight();
  // TODO low-level drawing
  void beginShape(int kind);
  void endShape();
  void vertex(Vector3D v);
  void vertex(float x, float y, float z);  
  // TODO not so sure about these:
  void stroke(int color);
  void color(int r, int g, int b);
	void strokeWeight(int weight);
  // TODO should go?
  void pushStyle();
  void popStyle();  
}
