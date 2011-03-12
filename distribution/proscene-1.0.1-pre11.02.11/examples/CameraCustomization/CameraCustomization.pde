/**
 * Camera Customization.
 * by Jean Pierre Charalambos.
 * 
 * This example shows all the different aspects of proscene that
 * can be customized and how to do it.
 * 
 * Read the commented lines of the sketch code for details.
 *
 * Press 'h' to display the global shortcuts in the console.
 * Press 'H' to display the current camera profile keyboard shortcuts
 * and mouse bindings in the console.
 */

import remixlab.proscene.*;
import remixlab.proscene.Scene.*;

Scene scene;
WeirdCameraProfile wProfile;

void setup() {
  size(640, 360, P3D);
  scene = new Scene(this);
  // A Scene has a single InteractiveFrame (null by default). We set it
  // here.
  scene.setInteractiveFrame(new InteractiveFrame(scene));
  scene.interactiveFrame().translate(new PVector(30, 30, 0));

  // 1. Perform some keyboard configuration:
  // Note that there are some defaults set (soon to be  documented ;)
  // change interaction between camera an interactive frame:
  scene.setShortcut('f', Scene.KeyboardAction.FOCUS_INTERACTIVE_FRAME);
  // draw frame selection hint
  scene.setShortcut(Scene.Modifier.ALT.ID, 'i', Scene.KeyboardAction.DRAW_FRAME_SELECTION_HINT);
  // change the camera projection
  scene.setShortcut('z', Scene.KeyboardAction.CAMERA_TYPE);

  // 2. Customized camera profile:
  wProfile = new WeirdCameraProfile(scene, "MY_PROFILE");
  scene.registerCameraProfile(wProfile);
  // Unregister the  first-person camera profile (i.e., leave WHEELED_ARCBALL
  // and MY_PROFILE):
  scene.unregisterCameraProfile("FIRST_PERSON");
}

void draw() {
  // Proscene sets the background to black by default. If you need to change
  // it, don't call background() directly but use scene.background() instead.
  fill(204, 102, 0);
  box(20, 20, 40);
  // Save the current model view matrix
  pushMatrix();
  // Multiply matrix to get in the frame coordinate system.
  // applyMatrix(scene.interactiveFrame().matrix()) is handy but
  // inefficient
  scene.interactiveFrame().applyTransformation(); // optimum
  // Draw an axis using the Scene static function
  scene.drawAxis(20);
  // Draw a second box attached to the interactive frame
  if (scene.interactiveFrame().grabsMouse()) {
    fill(255, 0, 0);
    box(12, 17, 22);
  } 
  else if (scene.interactiveFrameIsDrawn()) {
    fill(0, 255, 255);
    box(12, 17, 22);
  } 
  else {
    fill(0, 0, 255);
    box(10, 15, 20);
  }
  popMatrix();
}