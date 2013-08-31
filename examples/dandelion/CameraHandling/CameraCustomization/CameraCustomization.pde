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
import remixlab.proscene.Scene.ProsceneKeyboard;
import remixlab.proscene.Scene.ProsceneMouse;
import remixlab.dandelion.geom.*;
import remixlab.dandelion.core.*;
import remixlab.dandelion.agent.*;
import remixlab.dandelion.core.Constants.DOF2Action;
import remixlab.dandelion.core.Constants.KeyboardAction;

import remixlab.tersehandling.generic.event.GenericDOF2Event;
import remixlab.tersehandling.generic.event.GenericKeyboardEvent;

Scene scene;
CustomizedMouseAgent mouseAgent;
CustomizedKeyboardAgent keyboardAgent;
InteractiveFrame iFrame;

void setup() {
  size(640, 360, P3D);
  scene = new Scene(this);
  iFrame = new InteractiveFrame(scene);
  iFrame.translate(new Vec(30, 30, 0));

  mouseAgent = new CustomizedMouseAgent(scene, "MyMouseAgent");
  scene.terseHandler().unregisterAgent(mouseAgent);
  keyboardAgent = new CustomizedKeyboardAgent(scene, "MyKeyboardAgent");
  scene.terseHandler().unregisterAgent(keyboardAgent);
}

void draw() {
  background(0);
  fill(204, 102, 0);
  box(20, 20, 40);
  // Save the current model view matrix
  pushMatrix();
  // Multiply matrix to get in the frame coordinate system.
  // applyMatrix(scene.interactiveFrame().matrix()) is handy but
  // inefficient
  iFrame.applyTransformation(); // optimum
  // Draw an axis using the Scene static function
  scene.drawAxis(20);
  // Draw a second box attached to the interactive frame
  if (iFrame.grabsAgent(scene.defaultMouseAgent())) {
    fill(255, 0, 0);
    box(12, 17, 22);
  }
  else {
    fill(0, 0, 255);
    box(10, 15, 20);
  }  
  popMatrix();
}

public void keyPressed() {
  if ( key != ' ') return;
  if ( !scene.terseHandler().isAgentRegistered(mouseAgent) ) {
    scene.terseHandler().registerAgent(mouseAgent);
    scene.parent.registerMethod("mouseEvent", mouseAgent);
    scene.disableDefaultMouseAgent();
  }
  else {
    scene.terseHandler().unregisterAgent(mouseAgent);
    scene.parent.unregisterMethod("mouseEvent", mouseAgent);
    scene.enableDefaultMouseAgent();
  }
  if ( !scene.terseHandler().isAgentRegistered(keyboardAgent) ) {
    scene.terseHandler().registerAgent(keyboardAgent);
    scene.parent.registerMethod("keyEvent", keyboardAgent);
    scene.disableDefaultKeyboardAgent();
  }
  else {
    scene.terseHandler().unregisterAgent(keyboardAgent);
    scene.parent.unregisterMethod("keyEvent", keyboardAgent);
    scene.enableDefaultKeyboardAgent();
  }
}

public class CustomizedMouseAgent extends ProsceneMouse {
  public CustomizedMouseAgent(AbstractScene scn, String n) {
    scene.super(scene, n);
    cameraProfile().setBinding(TH_LEFT, DOF2Action.TRANSLATE);
    cameraProfile().setBinding(TH_META, TH_RIGHT, DOF2Action.ROTATE);
  }
}

public class CustomizedKeyboardAgent extends ProsceneKeyboard {
  public CustomizedKeyboardAgent(Scene scene, String n) {
    scene.super(scene, n);
    keyboardProfile().setShortcut('g', KeyboardAction.DRAW_AXIS);
    keyboardProfile().setShortcut('z', KeyboardAction.DRAW_FRAME_SELECTION_HINT);
    keyboardProfile().setShortcut('a', KeyboardAction.DRAW_GRID);
  }
}
