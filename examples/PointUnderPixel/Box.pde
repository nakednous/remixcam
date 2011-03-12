/**
 * Box. 
 * by Jean Pierre Charalambos.
 * 
 * This class is part of the Mouse Grabber example.
 *
 * Any object that needs to be "pickable" (such as the Box), should be
 * attached to its own InteractiveFrame. That's all there is to it.
 *
 * The built-in picking proscene mechanism actually works as follows. At
 * instantiation time all InteractiveFrame objects are added to a mouse
 * grabber pool. Scene parses this pool every frame to check if the mouse
 * grabs a InteractiveFrame by projecting its origin onto the screen.
 * If the mouse position is close enough to that projection (default
 * implementation defines a 10x10 pixel square centered at it), the object
 * will be picked. 
 *
 * Override InteractiveFrame.checkIfGrabsMouse if you need a more
 * sophisticated picking mechanism.
 *
 * Observe that this class is used among many examples, such as MouseGrabber
 * CajasOrientadas, PointUnderPixel and ScreenDrawing. Hence, it's quite
 * complete, but its functionality is not totally exploited by this example.
 */

public class Box {
  InteractiveFrame iFrame;
  float w, h, d;
  int c;

  Box() {
    iFrame = new InteractiveFrame(scene);
    setSize();
    setColor();
    setPosition();
  }
  
  // don't draw local axis
  public void draw() {
    draw(false);
  }

  public void draw(boolean drawAxis) {
    pushMatrix();
    pushStyle();
    // Multiply matrix to get in the frame coordinate system.
    // scene.parent.applyMatrix(iFrame.matrix()) is handy but inefficient
    iFrame.applyTransformation(); //optimum
    if(drawAxis) scene.drawAxis(max(w,h,d)*1.3f);
    noStroke();
    if (iFrame.grabsMouse())
      fill(255, 0, 0);
    else
      fill(getColor());
    //Draw a box
    box(w,h,d);
    popStyle();
    popMatrix();
  }
  
  // sets size randomly
  public void setSize() {
    w = random(10, 40);
    h = random(10, 40);
    d = random(10, 40);
  }
  
  public void setSize(float myW, float myH, float myD) {
    w=myW; h=myH; d=myD;
  }
  
  public int getColor() {
    return c;
  }
  
  // sets color randomly
  public void setColor() {
    c = color(random(0, 255), random(0, 255), random(0, 255));
  }
  
  public void setColor(int myC) {
    c = myC;
  }
  
  public PVector getPosition() {
    return iFrame.position();
  }
  
  // sets position randomly
  public void setPosition() {
    float low = -100;
    float high = 100;
    iFrame.setPosition(new PVector(random(low, high), random(low, high), random(low, high)));
  }
  
  public void setPosition(PVector pos) {
    iFrame.setPosition(pos);
  }
  
  public Quaternion getOrientation() {
    return iFrame.orientation();
  }
  
  public void setOrientation(PVector v) {
    PVector to = PVector.sub(v, iFrame.position());
    iFrame.setOrientation(new Quaternion(new PVector(0,1,0), to));
  }
}
