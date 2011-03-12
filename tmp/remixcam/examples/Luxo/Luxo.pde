/**
 * Luxo.
 * by Jean Pierre Charalambos. 
 * 
 * A more complex example that combines InteractiveFrames, selection and constraints.
 * 
 * This example displays a famous luxo lamp (Pixar) that can be interactively
 * manipulated with the mouse. It illustrates the use of several InteractiveFrames
 * in the same scene.
 * 
 * Click on a frame visual hint to select a part of the lamp, and then move it with
 * the mouse.
 * 
 * Press 'h' to display the global shortcuts in the console.
 * Press 'H' to display the current camera profile keyboard shortcuts
 * and mouse bindings in the console.
 */

import remixlab.proscene.*;

Scene scene;
Lamp lamp;

void setup() {
  size(640, 360, P3D);
  scene = new Scene(this);
  scene.setAxisIsDrawn(false);
  scene.setGridIsDrawn(false);
  scene.setFrameSelectionHintIsDrawn(true);
  // press 'f' to display frame selection hints
  scene.setShortcut('f', Scene.KeyboardAction.DRAW_FRAME_SELECTION_HINT);
  lamp = new Lamp();
}

void draw() {
  //Proscene sets the background to black by default. If you need to change
  //it, don't call background() directly but use scene.background() instead.
  lights();
  lamp.draw();
  //draw the ground
  noStroke();
  fill(120, 120, 120);
  float nbPatches = 100;
  normal(0.0f,0.0f,1.0f);
  for (int j=0; j<nbPatches; ++j) {
  beginShape(QUAD_STRIP );
  for (int i=0; i<=nbPatches; ++i) {
    vertex((200*(float)i/nbPatches-100), (200*j/nbPatches-100));
    vertex((200*(float)i/nbPatches-100), (200*(float)(j+1)/nbPatches-100));
  }
  endShape();
  }
}