/**
 * Standard Camera.
 * by Jean Pierre Charalambos.
 * 
 * A 'standard' Camera with fixed near and far planes.
 * 
 * Proscene supports a STANDARD camera kind which may be need by some applications.
 * Its near and far planes distances are set to fixed values, instead of being fit
 * to scene dimensions as is done in the PROSCENE camera kind (default).
 * 
 * Note, however, that the precision of the z-Buffer highly depends on how the zNear()
 * and zFar() values are fitted to your scene (as it is done with the PROSCENE camera
 * kind). Loose boundaries will result in imprecision along the viewing direction.
 *
 * This example requires the napplet library (http://github.com/acsmith/napplet)
 * (download it here: http://github.com/acsmith/napplet/downloads).
 * 
 * Press 'v' in the main viewer (the upper one) to toggle the camera kind.  
 * Press 'u'/'U' in the main viewer to change the frustum size (when the
 * camera kind is "STANDARD"). 
 * Press 'h' to display the global shortcuts in the console.
 * Press 'H' to display the current camera profile keyboard shortcuts
 * and mouse bindings in the console.
 */

import remixlab.proscene.*;
import napplet.*;

NAppletManager nappletManager;
NApplet mainNApplet;

void setup() {
  size(640, 720, P3D);
  // instantiate the viewers and embed them into a napplet manager
  nappletManager = new NAppletManager(this);
  mainNApplet = nappletManager.createNApplet("MainViewer", 0, 0);  
  nappletManager.createNApplet("AuxiliarViewer", 0, 360);
}

void mainDrawing(Scene s) {
  PApplet p = s.parent;
  p.noStroke();
  // the main viewer camera is used to cull the sphere object against its frustum
  Scene scn = ((MainViewer)(mainNApplet)).scene;
  switch (scn.camera().sphereIsVisible(new PVector(0,0,0), 40)) {
    case VISIBLE :
      p.fill(0, 255, 0);
      p.sphere(40);
    break;
    case SEMIVISIBLE :
      p.fill(255, 0, 0);
      p.sphere(40);
    break;
    case INVISIBLE :
    break;
  }
}

// same as the main drawing, but we also draw a representation of the main camera
void auxiliarDrawing(Scene s) {  
  mainDrawing(s);
  DrawingUtils.drawCamera(s.parent, ((MainViewer)(mainNApplet)).scene.camera());
}

void draw() {  
  background(50);
}