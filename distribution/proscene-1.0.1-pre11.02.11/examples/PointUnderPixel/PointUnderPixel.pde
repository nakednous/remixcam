/**
 * Point Under Pixel.
 * by Jean Pierre Charalambos.
 * 
 * This example illustrates 3D world point picking.
 * 
 * The click bindings below define some per-pixel user interactions,
 * such as "zoom on pixel" or "set the arcball reference point".
 * 
 * This example requires OpenGL to read the pixel depth. 
 * 
 * Press 'h' to display the global shortcuts in the console.
 * Press 'H' to display the current camera profile keyboard shortcuts
 * and mouse bindings in the console.
 */

import java.nio.FloatBuffer;
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import processing.opengl.*;
import remixlab.proscene.*;

Scene scene;
Box [] boxes;

void setup() {
  size(640, 360, OPENGL);
  scene = new Scene(this);
  scene.setShortcut('f', Scene.KeyboardAction.DRAW_FRAME_SELECTION_HINT);
  //add the click actions to all camera profiles
  CameraProfile [] camProfiles = scene.getCameraProfiles();
  for (int i=0; i<camProfiles.length; i++) {
    // left click will zoom on pixel:
    camProfiles[i].setClickBinding( Scene.Button.LEFT, Scene.ClickAction.ZOOM_ON_PIXEL );
    // middle click will show all the scene:
    camProfiles[i].setClickBinding( Scene.Button.MIDDLE, Scene.ClickAction.SHOW_ALL);
    // right click will will set the arcball reference point:
    camProfiles[i].setClickBinding( Scene.Button.RIGHT, Scene.ClickAction.ARP_FROM_PIXEL );
    // double click with the middle button while pressing SHIFT will reset the arcball reference point:
    camProfiles[i].setClickBinding( Scene.Modifier.SHIFT.ID, Scene.Button.MIDDLE, 2, Scene.ClickAction.RESET_ARP );
  }
  
  GLCamera glCam = new GLCamera(scene);
  scene.setCamera(glCam);
  scene.setGridIsDrawn(false);
  scene.setAxisIsDrawn(false);
  scene.setRadius(150);
  scene.showAll();
  boxes = new Box[50];
  // create an array of boxes with random positions, sizes and colors
  for (int i = 0; i < boxes.length; i++)
    boxes[i] = new Box();
}

void draw() {
  //Proscene sets the background to black by default. If you need to change
  //it, don't call background() directly but use scene.background() instead.
  for (int i = 0; i < boxes.length; i++)    
    boxes[i].draw();
}

class GLCamera extends Camera {
  protected PGraphicsOpenGL pgl;
  protected GL gl;
  protected GLU glu;
    
  public GLCamera(Scene scn) {
    super(scn);
    pgl = (PGraphicsOpenGL)pg3d;
    gl = pgl.gl;
    glu = pgl.glu;
  }
    
  protected WorldPoint pointUnderPixel(Point pixel) {
    float []depth = new float[1];
    pgl.beginGL();
    gl.glReadPixels((int)pixel.x, (screenHeight() - (int)pixel.y), 1, 1, GL.GL_DEPTH_COMPONENT, GL.GL_FLOAT, FloatBuffer.wrap(depth));
    pgl.endGL();
    PVector point = new PVector((int)pixel.x, (int)pixel.y, depth[0]);
    point = unprojectedCoordinatesOf(point);
    return new WorldPoint(point, (depth[0] < 1.0f));
  }
}