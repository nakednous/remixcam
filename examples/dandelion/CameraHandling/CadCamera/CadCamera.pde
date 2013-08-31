/**
 * Cad Camera.
 * by Jean Pierre Charalambos.
 * 
 * This example illustrates how to add a CAD Camera type to your your scene.
 * 
 * Press 'h' to display the global shortcuts in the console.
 * Press 'H' to display the current camera profile keyboard shortcuts
 * and mouse bindings in the console.
 * Press 'u' to switch between right handed and left handed world frame.
 * Press the space bar to switch between camera profiles: CAD and CAD_CAM.
 * Press x, y or z to set the main rotation axis (defined in the world
 * coordinate system) used by the CAD Camera.
 */

import remixlab.proscene.*;
import remixlab.dandelion.geom.*;
import remixlab.dandelion.core.*;

Scene scene;

void setup() {
  size(640, 360, P3D);
  //Scene instantiation
  scene = new Scene(this);
  //Set right handed world frame (useful for architects...)
  scene.setRightHanded();
  scene.camera().frame().setCADAxis(new Vec(0, 1, 0));
  scene.camera().frame().setRotationSensitivity(1.5);
}

void draw() {
  background(0);
  fill(204, 102, 0);
  box(20, 30, 50);
}

void keyPressed() {
  if(key == ' ')
    if(scene.camera().isArcBallRotate())
      scene.camera().setCadRotate();
    else
      scene.camera().setArcBallRotate();
  if (key == 'u' || key == 'U')
    if ( scene.isRightHanded() )
      scene.setLeftHanded();    
    else
      scene.setRightHanded();
  else if (key == 'x' || key == 'X')
    scene.camera().frame().setCADAxis(new Vec(1, 0, 0));
  else if (key == 'y' || key == 'Y')
    scene.camera().frame().setCADAxis(new Vec(0, 1, 0));
  else if (key == 'z' || key == 'Z')
    scene.camera().frame().setCADAxis(new Vec(0, 0, 1));
}